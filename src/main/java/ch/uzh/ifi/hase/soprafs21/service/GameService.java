package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.moves.IMove;
import ch.uzh.ifi.hase.soprafs21.objects.*;
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
    private final CardAPIService cardAPIService;
    private final UserService userService;
    private final WebSocketService webSocketService;

    Logger log = LoggerFactory.getLogger(GameService.class);

    @Autowired
    public GameService(CardAPIService cardAPIService, UserService userService, WebSocketService webSocketService) {
        this.cardAPIService = cardAPIService;
        this.userService = userService;
        this.webSocketService = webSocketService;
        instance = this;
    }

    public static GameService getInstance() {
        return instance;
    }

    // New Round initiated, then Send Card to Player and GameStats
    public void initiateRound(Game game) {
        Round currentRound = new Round(game.getPlayers(),game.getStartPlayer(),game.getCurrentCardAmountForRound(),game, cardAPIService,webSocketService, userService);
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
            for(IMove m: moves){
                List<Marble> possibleMarbles = m.getPlayableMarbles(game,this, 7);
                if (!(possibleMarbles.isEmpty())){
                    playableCardCodes.add(c.getCode());
                }
            }
        }
        if(playableCardCodes.isEmpty()) {
            webSocketService.broadcastThrowAway(game.getGameId(), p.getPlayerName(), handAsCardCode);
            webSocketService.broadcastNotificationMessage(p.getPlayerName(), "threw cards away", game.getGameId());
            p.getHand().throwAwayHand();
            game.getCurrentRound().changeCurrentPlayer();
        }
        return playableCardCodes;
    }

    private void checkIsYourTurn(String playerName, Player currentPlayer) throws Exception {
        if(!playerName.equals(currentPlayer.getPlayerName())) {
            throw new Exception("Invalid Move: It's not your turn");
        }
    }

    private void checkIsYourMarble(Card cardToPlay, String moveName, Game game, Marble marbleToMove) throws Exception {
        String playerName = game.getCurrentRound().getCurrentPlayer().getPlayerName();
        if (!(getPlayableMarble(game, playerName, cardToPlay, moveName, 7).contains(marbleToMove))){
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
        Field startField = marbleToMove.getCurrentField();
        Field targetFieldValue = game.getPlayingBoard().getFieldByFieldKey(targetFieldKey);
        int remainingSevenMoves = getDistanceBetweenFields(startField, targetFieldValue);
        if (!(getPossibleTargetFields(game, marbleToMove, moveName, cardToPlay, remainingSevenMoves).contains(targetFieldKey))){
            throw new Exception("Invalid Move: This target field can not be reached with the selected marble and card");
        }
    }


    private void checkValidityOfMove(String playerName, Card cardToPlay, String targetFieldKey, Marble marbleToMove, String moveName, Player currentPlayer, Game game) throws Exception {
        List<Card> hand = currentPlayer.getHand().getHandDeck();

        checkIsYourTurn(playerName, currentPlayer);
        checkIsYourMarble(cardToPlay, moveName, game, marbleToMove);
        checkIsCardInYourHand(hand, cardToPlay);
        checkHasCardThisMove(hand, moveName);
        if(!marbleToMove.getHome()) {
            checkTargetFieldValidity(marbleToMove, moveName, cardToPlay, game, targetFieldKey);
        }
    }

    public List<Marble> getPlayableMarble(Game game, String playerName, Card cardToPlay, String moveName, int remainingSevenMoves) {
        IMove moveToGetPlayableMarbles = null;
        for(IMove imove : cardToPlay.getMoves()) {
            if (imove.getName().equals(moveName)) {
                moveToGetPlayableMarbles = imove;
                break;
            }
        }
        return moveToGetPlayableMarbles.getPlayableMarbles(game, this, remainingSevenMoves);
    }

    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove, String moveName, Card cardToPlay, int remainingSevenMoves) {
        IMove moveToGetPlayableMarbles = null;
        for(IMove imove : cardToPlay.getMoves()) {
            if (imove.getName().equals(moveName)) {
                moveToGetPlayableMarbles = imove;
                break;
            }
        }
        return moveToGetPlayableMarbles.getPossibleTargetFields(game, marbleToMove, remainingSevenMoves);
    }


    public ArrayList<MarbleIdAndTargetFieldKey> makeMove(Game game, String playerName, String cardCodeToPlay, String moveName, ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys) throws Exception {

        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        Card cardToPlay = new Card(cardCodeToPlay);

        //for (MarbleIdAndTargetFieldKey mIdAndFieldKey: marbleIdAndTargetFieldKeys){
           // checkValidityOfMove(playerName, cardToPlay, mIdAndFieldKey.getFieldKey(), getMarbleByMarbleId(game, mIdAndFieldKey.getMarbleId()), moveName, currentPlayer, game);
        //}
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
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKeys = moveToExecute.executeMove(game,marbleIdAndTargetFieldKeys );
        game.getCurrentRound().getCurrentPlayer().layDownCard(cardToPlay);

        return marbleIdsAndTargetFieldKeys;
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
            c = game.getPlayingBoard().getPreviousColor(c);
        }
        else {
            newStartFieldVal = marble.getCurrentField().getFieldValue() + numberToGoForwards;
        }
        Field startingFieldMove = game.getPlayingBoard().getField(newStartFieldVal,c);
        int count = game.getPlayingBoard().nrStepsToNextStartFieldBlock(startingFieldMove);
        if (Math.abs(numberToGoForwards) <= count) {
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
        if (numberToGoForwards <= count) {
            log.info("CheckMOve: Marble : " + marble.getColor() + "FieldVal: " + marble.getCurrentField().getFieldValue() + "ishome: " + marble.getHome());
            return TRUE;
        }
        log.info("Cardvalue not playeble checkmove");
        return FALSE;
    }
    public void endGame(Game game){

            log.info("Game is finsihed");
            // EndGAME?

    }
    // Return True if one player and his teammate are finished


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
                endGame(game);
            }
            else {
                log.info("Change of Color and Marble for currentPlayer");
                currentPlayer.setMarbleList(teamMate.getMarbleList());
                currentPlayer.setColor(teamMate.getColor());
            }
        }
    }
    public boolean checkRoundIsFinished(Game game){
        int countCantPlay = 0;
        int countNoMoreCards = 0;
        for(Player p: game.getPlayers()){
            if(canPlay(p, game) == null){
                countCantPlay++;
            }
            if(p.getHand().getHandDeck() == null){
                countNoMoreCards++;
            }
        }
        if(countCantPlay == 4){
            log.info("No player has a playable Card anymore (checkroundisfinsined");
            return TRUE;
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
        webSocketService.sendExchangeFactsMessage(game.getCurrentRound().getCurrentPlayer().getPlayerName(), game.getGameId());
    }

    public MarbleIdAndTargetFieldKey eat(Field endField, Game game) {
        MarbleIdAndTargetFieldKey result = null;
        if (endField.getFieldStatus().equals(FieldStatus.OCCUPIED)) {
             Marble marbleToEat = endField.getMarble();
             game.getPlayingBoard().sendHome(marbleToEat);
             String colorInString = marbleToEat.getColor().getId();
             int newPositionFieldValue = 25 - game.getPlayingBoard().getNumberMarblesAtHome(game.getCurrentRound().getCurrentPlayer().getColor());
             String newPositionFieldValueAsString = String.valueOf(newPositionFieldValue);
             result = new MarbleIdAndTargetFieldKey(marbleToEat.getMarbleNr(), newPositionFieldValueAsString+colorInString);
        }
        return result;
    }

    public Marble getMarbleByMarbleId(Game game, int marbleId) {
        List<Marble> marbleList = game.getCurrentRound().getCurrentPlayer().getMarbleList();
        for (Marble m : marbleList) {
            if (m.getMarbleNr() == marbleId) {
                log.info(String.valueOf(m.getMarbleNr()));
                return m;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MarbleId not in current player's marbles");
    }

    public int getRemainingSevenMoves(Game game, ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKeys) {
        int fieldCounter = 0;

        for(MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey : marbleIdsAndTargetFieldKeys) {
            Marble marble = getMarbleByMarbleId(game, marbleIdAndTargetFieldKey.getMarbleId());
            Field startField = marble.getCurrentField();
            Field targetField = game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKey.getFieldKey());
            fieldCounter += getDistanceBetweenFields(startField, targetField);
        }

        return 7 - fieldCounter;
    }

    private int getDistanceBetweenFields(Field startField, Field targetField) {
        int startFieldValue = startField.getFieldValue();
        int targetFieldValue = targetField.getFieldValue();
        int distance = targetFieldValue - startFieldValue;
        if (targetFieldValue < startFieldValue){
            distance = 16 - startFieldValue + targetFieldValue;
        }
        // TODO finishing zone covered?
        return distance;
    }
}



