package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.moves.IMove;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement.CanPlayGetDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.GameThrowAwayDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.*;

import static java.lang.Boolean.*;



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
        Round currentRound = new Round(game.getPlayerList(),game.getStartPlayer(),game.getNrCards(),game, cardAPIService,webSocketService, userService);
        game.setRoundCount(1);
        game.setCurrentRound(currentRound);
    }

    public UserService getUserService() {
        return userService;
    }

    public void updateRoundStats(Game game){
        game.changeCurrentPlayer();
        game.addToRoundCount();
        game.changeNrCards();
    }

    // TODO new endpoint
    public List<String> canPlay(Player p, Game game){
        List<Card> hand = p.getHand().getHandDeck();
        List<String> playableCardCodes = new ArrayList<>();
        List<String> handAsCardCode = new ArrayList<>();
        for (Card c: hand){
            List<IMove> moves = c.getMoves();
            handAsCardCode.add(c.getCode());
            for(IMove m: moves){
                List<Marble> possibleMarbles = m.getPlayableMarbles(game,this);
                if (!(possibleMarbles.isEmpty())){
                    playableCardCodes.add(c.getCode());
                }
            }
        }
        if(playableCardCodes.isEmpty()) {
            webSocketService.broadcastThrowAway(game.getGameId(), p.getPlayerName(), handAsCardCode);
            p.getHand().throwAwayHand();
        }
        return playableCardCodes;
    }

    private void checkIsYourTurn(String playerName, Player currentPlayer) throws Exception {
        if(!playerName.equals(currentPlayer.getPlayerName())) {
            throw new Exception("Invalid Move: It's not your turn");
        }
    }

    private void checkIsYourMarble(Card cardToPlay, String moveName, Game game, Marble marbleToMove) throws Exception {
        if (!(getPlayableMarble(game.getCurrentRound().getCurrentPlayer().getPlayerName(), cardToPlay, moveName, game).contains(marbleToMove))){
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

    private void checkTargetFieldValidity(Marble marbleToMove, String moveName,Card cardToPlay, Game game, String targetFieldKey) throws Exception {
        if (!(getPossibleTargetFields(marbleToMove, moveName, cardToPlay, game).contains(targetFieldKey))){
            throw new Exception("Invalid Move: This target field can not be reached with the selected marble and card");
        }
    }


    private void checkValidityOfMove(String playerName, Card cardToPlay, String targetFieldKey, Marble marbleToMove, String moveName, Player currentPlayer, Game game) throws Exception {
        List<Card> hand = currentPlayer.getHand().getHandDeck();

        checkIsYourTurn(playerName, currentPlayer);
        checkIsYourMarble(cardToPlay, moveName, game, marbleToMove);
        checkIsCardInYourHand(hand, cardToPlay);
        checkHasCardThisMove(hand, moveName);
        checkTargetFieldValidity(marbleToMove, moveName, cardToPlay, game, targetFieldKey);
    }


    public List<Marble> getPlayableMarble(String playerName, Card cardToPlay, String moveName, Game game) {
        IMove moveToGetPlayableMarbles = null;
        for(IMove imove : cardToPlay.getMoves()) {
            if (imove.getName().equals(moveName)) {
                moveToGetPlayableMarbles = imove;
                break;
            }
        }
        return moveToGetPlayableMarbles.getPlayableMarbles(game, this);
    }

    public List<String> getPossibleTargetFields(Marble marbleToMove, String moveName, Card cardToPlay, Game game) {
        IMove moveToGetPlayableMarbles = null;
        for(IMove imove : cardToPlay.getMoves()) {
            if (imove.getName().equals(moveName)) {
                moveToGetPlayableMarbles = imove;
                break;
            }
        }
        return moveToGetPlayableMarbles.getPossibleTargetFields(game, marbleToMove);
    }

    public ArrayList<MarbleIdAndTargetFieldKey> makeMove(String playerName, String cardCodeToPlay,  String moveName, Game game, MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey) throws Exception {
        String targetFieldKey = "";
        int marbleIdToMove = 0;
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        Marble marbleToMove = getMarbleByMarbleId(game, marbleIdToMove);
        Card cardToPlay = new Card(cardCodeToPlay);

        // will throw exception and exit if the move is invalid
        checkValidityOfMove(playerName, cardToPlay, targetFieldKey, marbleToMove, moveName, currentPlayer, game);

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

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKeys = moveToExecute.executeMove(game, marbleIdAndTargetFieldKey);
        game.getCurrentRound().getCurrentPlayer().layDownCard(cardToPlay);
        // TODO finished player checks
        game.changeCurrentPlayer();
        return marbleIdsAndTargetFieldKeys;
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
    public List<Marble> getMarblesToChangeWithJack (List < Marble > marblesOnField, Player p){
        List<Marble> marblesMate = p.getTeamMate().getMarblesOnField();
        List<Marble> marblesPlayer = marblesOnField;
        List<Marble> possibleMarbles = null;
        for (int i = 0; i < 4; i++) {
            if (!(marblesMate.get(i).getMarbleIsBlockingAndOnStart())) {
                possibleMarbles.add(marblesMate.get(i));
            }
            if (!(marblesPlayer.get(i).getMarbleIsBlockingAndOnStart())) {
                marblesPlayer.add(marblesMate.get(i));
            }
        }
        if (marblesPlayer.size() > 0 && possibleMarbles.size() > 1) {
            for (Marble m : possibleMarbles) {
                Color color = m.getColor();
                int i = m.getCurrentField().getFieldValue();
                log.info("Marble C: " + color + "FieldVal" + i);
            }
            return possibleMarbles;
        }
        else {
            log.info("No marble possible with this card(JACK");
            return null;
        }
    }

    public void endTurn(Game game){
        //CHeck if player is finished if yes change marbles
        game.getCurrentRound().changeCurrentPlayer();
        log.info("Turn over, next Players turn");

    }

    public void endRound(Game game){
        updateRoundStats(game);
    }

    public MarbleIdAndTargetFieldKey eat(Field endField, Game game) {
        MarbleIdAndTargetFieldKey result = null;
        if (endField.getFieldStatus().equals(FieldStatus.OCCUPIED)) {
             Marble marbleToEat = endField.getMarble();
             game.getPlayingBoard().sendHome(marbleToEat);
             String colorInString = marbleToEat.getColor().getId();
             int newPositionFieldValue = 25 - game.getPlayingBoard().getFinishFields(marbleToEat.getColor()).size();
             String newPositionFieldValueAsString = String.valueOf(newPositionFieldValue);
             result = new MarbleIdAndTargetFieldKey(marbleToEat.getMarbleNr(), newPositionFieldValueAsString+colorInString);
        }
        return result;
    }

    public Marble getMarbleByMarbleId(Game game, int marbleId) throws Exception {
        List<Marble> marbleList = game.getCurrentRound().getCurrentPlayer().getMarbleList();
        for (Marble m : marbleList) {
            if (m.getMarbleNr() == marbleId) {
                log.info(String.valueOf(m.getMarbleNr()));
                return m;
            }
        }
        throw new Exception("MarbleId not in current player's marbles");
    }


}



