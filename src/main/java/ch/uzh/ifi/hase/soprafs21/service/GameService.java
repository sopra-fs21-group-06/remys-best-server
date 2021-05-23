package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.moves.IMove;
import ch.uzh.ifi.hase.soprafs21.moves.INormalMove;
import ch.uzh.ifi.hase.soprafs21.moves.ISplitMove;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.GameEndDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


@Service
@Transactional
public class GameService {

    private static GameService instance;
    private final UserService userService;
    private final WebSocketService webSocketService;

    Logger log = LoggerFactory.getLogger(GameService.class);

    @Autowired
    public GameService(UserService userService, WebSocketService webSocketService) {
        this.userService = userService;
        this.webSocketService = webSocketService;
        instance = this;
    }

    public static GameService getInstance() {
        return instance;
    }

    // New Round initiated, then Send Card to Player and GameStats
    public void initiateRound(Game game) {
        Round currentRound = new Round(game, webSocketService, userService, game.getCardAPIService());
        game.incrementRoundNumber();
        game.setCurrentRound(currentRound);
    }

    public UserService getUserService() {
        return userService;
    }

    public void updateRoundStats(Game game){
        game.changeStartingPlayer();
        game.incrementRoundNumber();
        game.incrementCardAmountForRound();

        // TODO set exchange mode ?
    }

    public List<String> canPlay(Player p, Game game){
        List<Card> hand = p.getHand().getHandDeck();
        List<String> playableCardCodes = new ArrayList<>();
        List<String> handAsCardCode = new ArrayList<>();
        for (Card c: hand){
            List<IMove> moves = c.getMoves();
            handAsCardCode.add(c.getCode());
            for(IMove move: moves){
                List<Marble> possibleMarbles = getPlayableMarblesOfMove(move, game, this, new ArrayList<>());
                if (!possibleMarbles.isEmpty()){
                    playableCardCodes.add(c.getCode());
                    log.info((c.getCode()));
                }
            }
        }
        if(playableCardCodes.isEmpty()) {
            log.info("Player cant Play");
            webSocketService.broadcastThrowAway(game.getGameId(), p.getPlayerName(), handAsCardCode);
            p.getHand().throwAwayHand();
            game.getCurrentRound().changeCurrentPlayer();
        }
        return playableCardCodes;
    }

    private List<Marble> getPlayableMarblesOfMove(IMove move, Game game, GameService gameService, ArrayList<MarbleIdAndTargetFieldKey> sevenMoves) {
        if(move instanceof ISplitMove) {
            return ((ISplitMove) move).getPlayableMarbles(game, gameService, sevenMoves);
        }
        return ((INormalMove) move).getPlayableMarbles(game, gameService);
    }

    private List<String> getPossibleTargetFieldsOfMove(IMove move, Game game, Marble marbleToMove, ArrayList<MarbleIdAndTargetFieldKey> sevenMoves) {
        if(move instanceof ISplitMove) {
            return ((ISplitMove) move).getPossibleTargetFields(game, marbleToMove, sevenMoves);
        }
        return ((INormalMove) move).getPossibleTargetFields(game, marbleToMove);
    }

    private void checkIsYourTurn(String playerName, Player currentPlayer) throws Exception {
        if(!playerName.equals(currentPlayer.getPlayerName())) {
            throw new Exception("Invalid Move: It's not your turn");
        }
    }

    private void checkIsYourMarble(Card cardToPlay, String moveName, Game game, Marble marbleToMove) throws Exception {
        String playerName = game.getCurrentRound().getCurrentPlayer().getPlayerName();

        if (!(getPlayableMarbles(game, playerName, cardToPlay, moveName).contains(marbleToMove))){
            throw new Exception("Invalid Move: Not your marble");
        }
    }

    private void checkIsCardInYourHand(List<Card> hand, Card cardToPlay) throws Exception {
        boolean hasCardInHand = false;
        for(Card card: hand){
            if (card.getCode().equals(cardToPlay.getCode())) {
                hasCardInHand = true;
                break;
            }
        }
        if(!hasCardInHand) {
            throw new Exception("Invalid Move: You don't have this card in your hand");
        }
    }

    private void checkHasCardThisMove(List<Card> hand, String moveName) throws Exception {
        boolean hasCardThisMove = false;
        for(Card card: hand){
            for(IMove move : card.getMoves()) {
                if (move.getName().equals(moveName)) {
                    hasCardThisMove = true;
                    break;
                }
            }
        }
        if(!hasCardThisMove) {
            throw new Exception("Invalid Move: You can not execute this move with this card");
        }
    }

    private void checkTargetFieldValidity(Marble marbleToMove, String moveName, Card cardToPlay, Game game, String targetFieldKey) throws Exception {
        if (!(getPossibleTargetFields(game, marbleToMove, moveName, cardToPlay).contains(targetFieldKey))){
            throw new Exception("Invalid Move: This target field can not be reached with the selected marble and card");
        }
    }

    private void checkValidityOfMove(String playerName, Card cardToPlay, String targetFieldKey, Marble marbleToMove, String moveName, Player currentPlayer, Game game) throws Exception {
        List<Card> hand = currentPlayer.getHand().getHandDeck();

        checkIsYourTurn(playerName, currentPlayer);
       /* if(!moveName.equals("Split 7")){
            checkIsYourMarble(cardToPlay, moveName, game, marbleToMove);
      }  */
        checkIsYourMarble(cardToPlay, moveName, game, marbleToMove);
        checkIsCardInYourHand(hand, cardToPlay);
        checkHasCardThisMove(hand, moveName);
        if(!marbleToMove.getHome() /*&& !moveName.equals("Split 7")*/) {
            checkTargetFieldValidity(marbleToMove, moveName, cardToPlay, game, targetFieldKey);
        }
    }

    public List<Marble> getPlayableMarbles(Game game, String playerName, Card cardToPlay, String moveName) {
        return getPlayableMarbles(game, playerName, cardToPlay, moveName, new ArrayList<>());
    }

    public List<Marble> getPlayableMarbles(Game game, String playerName, Card cardToPlay, String moveName, ArrayList<MarbleIdAndTargetFieldKey> sevenMoves) {
        IMove moveToGetPlayableMarbles = null;
        for(IMove imove : cardToPlay.getMoves()) {
            if (imove.getName().equals(moveName)) {
                moveToGetPlayableMarbles = imove;
                break;
            }
        }
        return getPlayableMarblesOfMove(moveToGetPlayableMarbles, game, this, sevenMoves);
    }

    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove, String moveName, Card cardToPlay) {
        return getPossibleTargetFields(game, marbleToMove, moveName, cardToPlay, new ArrayList<>());
    }

    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove, String moveName, Card cardToPlay, ArrayList<MarbleIdAndTargetFieldKey> sevenMoves) {
        IMove moveToGetPossibleTargetFields = null;
        for(IMove imove : cardToPlay.getMoves()) {
            if (imove.getName().equals(moveName)) {
                moveToGetPossibleTargetFields = imove;
                break;
            }
        }
        return getPossibleTargetFieldsOfMove(moveToGetPossibleTargetFields, game, marbleToMove, sevenMoves);
    }

    public ArrayList<MarbleIdAndTargetFieldKey> makeMove(Game game, String playerName, String cardCodeToPlay, String moveName, ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys) throws Exception {

        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        Card cardToPlay = new Card(cardCodeToPlay);

        // check move

        MarbleIdAndTargetFieldKey mIdAndFieldKey = marbleIdAndTargetFieldKeys.get(0);

        checkValidityOfMove(playerName, cardToPlay, mIdAndFieldKey.getFieldKey(), getMarbleByMarbleId(game, mIdAndFieldKey.getMarbleId()), moveName, currentPlayer, game);

        // execute move
        IMove moveToExecute = null;
        for(IMove move : cardToPlay.getMoves()) {
            if (move.getName().equals(moveName)) {
                moveToExecute = move;
                break;
            }
        }
        if(moveToExecute == null) {
            throw new Exception("Something strange happened");
        }
        ArrayList<MarbleIdAndTargetFieldKey> executedMarbleIdsAndTargetFieldKeys = moveToExecute.executeMove(game,marbleIdAndTargetFieldKeys );

        String s = String.valueOf(mIdAndFieldKey.getMarbleId());
        Field f = game.getPlayingBoard().getFieldByFieldKey(mIdAndFieldKey.getFieldKey());
        String status = f.getFieldStatus().name();
        Marble ma = null;
        for(Marble m: game.getCurrentRound().getCurrentPlayer().getMarbleList()){
            if(m.getMarbleId() == marbleIdAndTargetFieldKeys.get(0).getMarbleId()){
                ma = m;
            }
        }
        String mahome = String.valueOf(ma.getHome());
        String mafinish = String.valueOf(ma.getFinish());
        log.info("Marble :" + s + "Field :" + mIdAndFieldKey.getFieldKey() + "FieldStatus: "+ status +  "Marble is home :" + mahome + "Marble is finsih: " + mafinish);
        webSocketService.broadcastPlayedMessage(playerName, cardCodeToPlay, executedMarbleIdsAndTargetFieldKeys, game.getGameId());
        game.getCurrentRound().getCurrentPlayer().layDownCard(cardToPlay);
        checkEndTurnAndEndRound(game);
        return executedMarbleIdsAndTargetFieldKeys;
    }

    public void checkEndTurnAndEndRound(Game game){
        endTurn(game);
        if(checkRoundIsFinished(game)){
            log.info("round finsihed(checkEndTurnAndEndRound)");
            endRound(game);
        } else {

            game.getCurrentRound().changeCurrentPlayer();
        }
    }

    public Boolean checkMoveBackward(Marble marble, int numberToGoForwards, Game game){
        Color c = marble.getCurrentField().getColor();
        List<Integer> valueToCheck = new ArrayList<>();
        valueToCheck.add(1);
        valueToCheck.add(2);
        valueToCheck.add(3);
        valueToCheck.add(4);
        int newStartFieldVal;
        if (valueToCheck.contains(marble.getCurrentField().getFieldValue())) {
            newStartFieldVal = 20-marble.getCurrentField().getFieldValue();
            c = DogUtils.getPreviousColor(c);
        }
        else {
            newStartFieldVal = marble.getCurrentField().getFieldValue() + numberToGoForwards;
        }
        Field startingFieldMove = game.getPlayingBoard().getField(newStartFieldVal,c);
        int count = game.getPlayingBoard().nrStepsToNextStartFieldBlock(startingFieldMove)+1;
        if (Math.abs(numberToGoForwards) < count) {
            log.info("CheckMOve: Marble : " + marble.getColor() + "FieldVal: " + marble.getCurrentField().getFieldValue() + "ishome: " + marble.getHome());
            return TRUE;
        }
        log.info("-4 not playeble checkmove");
        return FALSE;
    }

    public Boolean checkMoveForward(Marble marble, int numberToGoForwards, Game game){
        int count = 0;
        Field startingFieldMove = marble.getCurrentField();
        // Check if marble is in finishfield, count is difference to next
        if(marble.getCurrentField() instanceof FinishField){
            count = game.getPlayingBoard().nrStepsToNextFreeFinishSpot(startingFieldMove);
        } else {
            count = game.getPlayingBoard().nrStepsToNextStartFieldBlock(startingFieldMove);
        }
        if (numberToGoForwards < count) {
            return TRUE;
        }
        return FALSE;
    }

    public void endGame(Game game, Player wonPlayer1, Player wonPlayer2) {
        log.info("Game is finsihed");
        GameEndDTO gameEndDTO = new GameEndDTO();
        List<String> wonUsernames = new ArrayList<>();
        wonUsernames.add(wonPlayer1.getPlayerName());
        wonUsernames.add(wonPlayer2.getPlayerName());
        gameEndDTO.setWon(wonUsernames);
        webSocketService.broadcastGameEndMessage(String.valueOf(game.getGameId()), gameEndDTO);
    }

    public boolean checkPlayerIsFinished(Player currentPlayer){
        int countChangeTeam = 0;
        for(Marble m: currentPlayer.getMarbleList()){
            if(m.getFinish()){
                countChangeTeam++;
            }
        }
        if(countChangeTeam == 4) {
            return TRUE;
        } else {
            return FALSE;
        }
    }
    public void endTurn(Game game) {
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        Player teamMate = currentPlayer.getTeamMate();
        if (checkPlayerIsFinished(currentPlayer)) {
            currentPlayer.setFinished(TRUE);
            if (currentPlayer.isFinished() && teamMate.isFinished()) {
                endGame(game, currentPlayer, teamMate);
            }
            else {
                log.info("Change of Color and Marble for currentPlayer");
                currentPlayer.setMarbleList(teamMate.getMarbleList());

            }
        }
    }
    public boolean checkRoundIsFinished(Game game){
        int countNoMoreCards = 0;
        for(Player p: game.getPlayers()){
            if(p.getHand().getHandDeck().isEmpty()){
                countNoMoreCards++;
            }
        }

        if(countNoMoreCards == 4){
            log.info("No more cards in game(checkroundisfinsined");
            return TRUE;
        }
        return FALSE;
    }
    public void endRound(Game game){
        updateRoundStats(game);
        initiateRound(game);
        webSocketService.broadcastExchangeFactsMessage(game.getCurrentRound().getCurrentPlayer().getPlayerName(), game.getGameId());
    }

    public MarbleIdAndTargetFieldKey eat(Field endField, Game game) {
        MarbleIdAndTargetFieldKey result = null;
        Color colorToLookAt = game.getCurrentRound().getCurrentPlayer().getColor();
        int nrMarlbesAtHome = game.getCurrentRound().getCurrentPlayer().getNrMarbleAtHome();
        if(game.getCurrentRound().getCurrentPlayer().isFinished()){
            colorToLookAt =game.getCurrentRound().getCurrentPlayer().getTeamMate().getColor();
            nrMarlbesAtHome = game.getCurrentRound().getCurrentPlayer().getNrMarbleAtHome();
        }
        if (endField.getFieldStatus().equals(FieldStatus.OCCUPIED)) {
             Marble marbleToEat = endField.getMarble();
             game.getPlayingBoard().sendHome(marbleToEat);
             String colorInString = marbleToEat.getColor().getId();
             int newPositionFieldValue = 24 - nrMarlbesAtHome;
             log.info("Field number eat" + endField.getFieldKey());
             String newPositionFieldValueAsString = String.valueOf(newPositionFieldValue);
             result = new MarbleIdAndTargetFieldKey(marbleToEat.getMarbleId(), newPositionFieldValueAsString+colorInString);
        }
        return result;
    }

    public Marble getMarbleByMarbleId(Game game, int marbleId) {
        List<Marble> marbleList = game.getCurrentRound().getCurrentPlayer().getMarbleList();
        for (Marble m : marbleList) {
            if (m.getMarbleId() == marbleId) {
                log.info(String.valueOf(m.getMarbleId()));
                return m;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MarbleId not in current player's marbles");
    }
    public Marble getMarbleByMarbleIdForSeven(Game game, int marbleId) {
        List<Marble> marbleList = game.getCurrentRound().getCurrentPlayer().getMarbleList();
        Marble marble = null;
        List<Marble> marbleListMate = game.getCurrentRound().getCurrentPlayer().getTeamMate().getMarbleList();
        for (Marble m : marbleList) {
            if (m.getMarbleId() == marbleId) {
                return marble =m;
            }
        }
        for (Marble m : marbleListMate) {
            if (m.getMarbleId() == marbleId) {
                return marble =m;
            }
        }
        return marble;
    }

    public int getRemainingSevenMoves(Game game, ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKeys) {
        int fieldCounter = 0;
        ArrayList<Marble> marbleList = new ArrayList<>();
        ArrayList<Field> fieldList = new ArrayList<>();
        for(MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey : marbleIdsAndTargetFieldKeys) {
            Marble marble = getMarbleByMarbleIdForSeven(game, marbleIdAndTargetFieldKey.getMarbleId());
            Field startField = marble.getCurrentField();
            for(int i = 0; i < marbleList.size(); i++){
                if(marbleList.get(i).getMarbleId() == marble.getMarbleId()){
                    startField = fieldList.get(i);
                }
            }
            Field targetField = game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKey.getFieldKey());
            fieldCounter += getDistanceBetweenFields(startField, targetField);
            marbleList.add(marble);
            fieldList.add(targetField);
        }
        return 7 - fieldCounter;
    }

    public int getDistanceBetweenFields(Field startField, Field targetField) {
        int startFieldValue = startField.getFieldValue();
        int targetFieldValue = targetField.getFieldValue();
        int distance = targetFieldValue - startFieldValue;
        if (targetFieldValue < startFieldValue){
            distance = 16 - startFieldValue + targetFieldValue;
        }
        return distance;
    }
}



