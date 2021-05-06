package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.moves.IMove;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


// TODO makeMOve(FIeldKez and Marble) return (Marbleid targetfieldkey) -> new Pair( getLeft, getRight) return List<Pair>

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
    public void initiateRound(Game game){
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

    public Boolean canPlay(Player p, Game game){
        List<Card> hand = p.getHand().getHandDeck();
        int count = 0;
        for (Card c: hand){
            List<String> moves = c.getMovesToDisplay();
            for(String m: moves){
                if (!(getPlayableMarble(c,m,game).isEmpty())){
                    count++;
                }
            }
        }
        if(count == 0){
            log.info("Current Player can't Play");
            return FALSE;
        }
        log.info("Player can play");
        return TRUE;
    }


    private void checkValidityOfMove(String playerName, Card cardToPlay, String targetFieldKey, Marble marbleToMove, String moveName, Player currentPlayer, Game game) throws Exception{
        List<Card> hand = currentPlayer.getHand().getHandDeck();
        // not your turn
        if(!playerName.equals(currentPlayer.getPlayerName())) {
            throw new Exception("Invalid Move: It's not your turn");
        }

        // not your marble
        if (!(getPlayableMarble(cardToPlay, moveName,game).contains(marbleToMove))){
            throw new Exception("Invalid Move: Not your marble");
        }

        // card not in your hand
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
        // card does not have this move
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
        // invalid targetFieldKey
        if (!(getPossibleTargetFields(marbleToMove, moveName, cardToPlay, game).contains(targetFieldKey))){
            throw new Exception("Invalid Move: This target field can not be reached with the selected marble and card");
        }
    }
    // return playabe Marble for Player
    public List<Marble> getPlayableMarble(Card cardToPlay, String moveName, Game game) {
        //TODO checks card is in hand, move matches card
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
        //TODO checks card is in hand, move matches card,
        IMove moveToGetPlayableMarbles = null;
        for(IMove imove : cardToPlay.getMoves()) {
            if (imove.getName().equals(moveName)) {
                moveToGetPlayableMarbles = imove;
                break;
            }
        }
        return moveToGetPlayableMarbles.getPossibleTargetFields(game, marbleToMove);
    }

    public ArrayList<MarbleIdAndTargetFieldKey> makeMove(String playerName, String cardCodeToPlay, String targetFieldKey, int marbleIdToMove, String moveName, Game game) throws Exception {
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        Marble marbleToMove = getMarbleByGameIdMarbleIdPlayerName(game, playerName, marbleIdToMove);
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

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKeys = moveToExecute.executeMove(marbleToMove, game.getPlayingBoard().getFieldByFieldKey(targetFieldKey),game);

        // TODO finished player checks

        return marbleIdsAndTargetFieldKeys;
    }

    //returns possible moves after player clicks on cards
    public List<String> sendCardMove(Card c){
        List<String> movesToDisplay= c.getMovesToDisplay();
        for (String s: movesToDisplay) {
            log.info(s);
        }
        return movesToDisplay;
    }




    // return nr to next start block if the next one is blocked else return 100
    public int nrStepsToNextStartFieldBlock(Field startingFieldMove, int numberToGoForwards, Game game){
        Color currentColor = startingFieldMove.getColor();
        if(game.getPlayingBoard().getNextStartFieldIsBlocked(currentColor) && !startingFieldMove.getFieldStatus().equals(FieldStatus.BLOCKED)){
            return 16 - startingFieldMove.getFieldValue();
        } else {
            return 100;
        }
    }
    // Start is at first HomeField if all are interesting, first check new fieldKey is bigger than old and if the field is occupierd
    public int nrStepsToNextFreeFinishSpot(Field startField, Game game){
        Color c = startField.getColor();
        List<Field> finishFields = game.getPlayingBoard().getFinishFields(c);
        int count = 0;
        for(Field f: finishFields){
            if(f.getFieldValue() > startField.getFieldValue()){
                if(f.getFieldStatus().equals(FieldStatus.OCCUPIED)){
                    return count;
                }
                count++;
            }
        }
        return count;
    }
    public boolean finishFieldIsFinishMoveField(Field finishField, Game game){
        List<Field> finishFields = game.getPlayingBoard().getFinishFields(finishField.getColor());
        int stepsCount = nrStepsToNextFreeFinishSpot(finishField,game);
        int positionField = 20 - finishField.getFieldValue();
        int countMarble = 0;
        for(Field f: finishFields) {
            if (f.getFieldValue() > finishField.getFieldValue()) {
                if (f.getFieldStatus().equals(FieldStatus.BLOCKED)) {
                    countMarble++;
                }
            }
        }
        if(stepsCount == 0 && positionField == countMarble){
            log.info("Finishfield" + finishField.getFieldKey() + "is actual finishfield in finsifieldmove");
            return TRUE;
        } else {
            log.info("Finishfield" + finishField.getFieldKey() + "is not finishfield in finsifieldmove");
            return TRUE;
        }
    }

    public Boolean startFieldIsPossibleEndFieldMove(Field endField){
        if(endField.getFieldStatus().equals(FieldStatus.BLOCKED)){
            log.info("Your starting field is blocked by your own Marble");
            return FALSE;
        } else {
            log.info("Your starting field is free");
            return TRUE;
        }
    }

    // first get the currentfield and set as start Filed of current move.
    // iterate over possible cardmovevalues and see if marble can make one of the moves return TRUE;
    // if the card value is 4, set startmove Field back 4.
    // check how marble can make move: first find current field, then make as many steps as the card value. if one field is blocking no count++
    // if count is eqaul value the marble can make the move
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
            newStartFieldVal = marble.getCurrentField().getFieldValue() - 4;
        }
        Field startingFieldMove = game.getPlayingBoard().getField(newStartFieldVal,c);
        int count = nrStepsToNextStartFieldBlock(startingFieldMove, numberToGoForwards, game);
        if (4 <= count) {
            log.info("CheckMOve: Marble : " + marble.getColor() + "FieldVal: " + marble.getCurrentField().getFieldValue() + "ishome: " + marble.getHome());
            return TRUE;
        }
        log.info("-4 not playeble checkmove");
        return FALSE;

    }
    public Boolean checkMoveForward(Marble marble, int numberToGoForwards, Game game){
        int count = 0;
        Field startingFieldMove = marble.getCurrentField();
        Color c = marble.getCurrentField().getColor();
        // Check if marble is in finishfield, count is difference to next
        if(marble.getCurrentField() instanceof FinishField){
            count = nrStepsToNextFreeFinishSpot(startingFieldMove,game);
        } else {
            count = nrStepsToNextStartFieldBlock(startingFieldMove, numberToGoForwards, game);
        }
        if (numberToGoForwards <= count) {
            log.info("CheckMOve: Marble : " + marble.getColor() + "FieldVal: " + marble.getCurrentField().getFieldValue() + "ishome: " + marble.getHome());
            return TRUE;
        }
        log.info("Cardvalue not playeble checkmove");
        return FALSE;
    }

    public void endTurn(Game game){
        //CHeck if player is finished if yes change marbles
        game.getCurrentRound().changeCurrentPlayer();
        log.info("Turn over, next Players turn");
    }
    public void endRound(Game game){
        updateRoundStats(game);
    }




     public void eat(Field endField, Game game) {
         if (endField.getFieldStatus().equals(FieldStatus.OCCUPIED)) {
             Marble marbleToEat = endField.getMarble();
             game.getPlayingBoard().sendHome(marbleToEat);
         }
     }

     public Marble getMarbleByGameIdMarbleIdPlayerName (Game game, String playerName,int marbleId){
         for (Player p : game.getPlayerList()) {
             if (p.getPlayerName().equals(playerName)) {
                 for (Marble m : p.getMarbleList()) {
                     if (m.getMarbleNr() == marbleId) {
                         log.info(String.valueOf(m.getMarbleNr()));
                         return m;
                     }
                 }
             }
         }
         log.info("Not good with Marbleconversion");
         return null;
     }
}



