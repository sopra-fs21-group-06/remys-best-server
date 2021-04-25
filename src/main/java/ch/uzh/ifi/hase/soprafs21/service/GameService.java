package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;
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
    public void initiateRound(Game game){
        Round currentRound = new Round(game.getPlayerList(),game.getStartPlayer(),game.getNrCards(),game, cardAPIService,webSocketService, userService);
        game.setCurrentRound(currentRound);
    }

    public List<Player> getPlayers(Game game){
        return game.getPlayerList();
    }

    public void updateRoundStats(Game game){
        int indexCurrent;
        indexCurrent = game.getPlayerList().indexOf(game.getStartPlayer());
        int indexNext = (indexCurrent + 1) % 4;
        game.setStartPlayer(game.getPlayerList().get(indexNext));
        game.addToRoundCount();
        game.changeNrCards();
    }


    public Boolean triggerNewRound(Game game){
        // send Roundnr, Active Player, next Card Amount and next Beginner
        //send CurrentPlayer if he can play

        return canPlayCard(game.getStartPlayer(), game);
    }
    public Boolean canPlayCard(Player p, Game game){
        if (p.canPlay(game.getPlayingBoard().getBlockedFieldsValue())) {
            log.info("Current Player can Play");
            return TRUE;
        } else {
            log.info("Current Player can't Play");
            return FALSE;
        }
    }

    //Method for RoundStats
    public void sendRoundStats(Game game){
        int roundNr = game.getRoundCount();
        String name = game.getCurrentRound().getCurrentPlayer().getPlayerName();
        int cardAmountNext = game.getNextCardAmount();
        int indexCurrent = game.getPlayerList().indexOf(game.getCurrentRound().getCurrentPlayer());
        int indexNext = (indexCurrent + 1) % 4;
        String nameNext = game.getPlayerList().get(indexNext).getPlayerName();
        log.info("Roundnr" + roundNr + name + "cardamountnext" + cardAmountNext + nameNext);
    }

    //returns possible moves after player clicks on cards
    public List<String> sendCardMove(Card c){
        List<String> movesToDisplay= c.getMovesToDisplay();
        for (String s: movesToDisplay) {
            log.info(s);
        }
        return movesToDisplay;
    }


    public List<Marble> getPlayableMarble(Card c, String move, Game game){
        Player p = game.getCurrentRound().getCurrentPlayer();
        List<Marble> marblesOnField = p.getMarblesOnField();
        List<Marble> marblesOnFieldAndNotFinished = p.getMarblesOnFieldAndNotFinished();
        List<Marble> marblesFinished = p.getMarblesInFinishFieldAndFinished();

        List<Marble> possibleMarbles = new ArrayList<>();
        String cardValue = c.getCardValue();
        // check hand for card but with code
        if ("J".equals(cardValue)){
            return checkJack(marblesOnField,p);
        } else if ("A".equals(cardValue) || "K".equals(cardValue) ){
            if(move.contains("Go")){
                Boolean b = game.getPlayingBoard().hasMarbleOnHomeStack(p.getColor());
                if(!b){
                    log.info("No Marbles at home to go out");
                    return possibleMarbles;
                }
                Marble m = game.getPlayingBoard().getFirstHomeMarble(p.getColor(), false);
                possibleMarbles.add(m);
                return possibleMarbles;
            } else {
                List<Integer> list = new ArrayList<>();
                if(move.contains("1")){
                    list.add(1);
                    for (Marble m : marblesOnFieldAndNotFinished) {
                        if (checkMove(m, list, game)) {
                            possibleMarbles.add(m);
                        }
                    }
                } else if(move.contains("11")){
                    list.add(11);
                    for (Marble m : marblesOnField) {
                        if (checkMove(m, list, game)) {
                            possibleMarbles.add(m);
                        }
                    }
                } else if(move.contains("13")){
                    list.add(13);
                    for (Marble m : marblesOnField) {
                        if (checkMove(m, list, game)) {
                            possibleMarbles.add(m);
                        }
                    }
                }
            }
        } else if(c.getCardValue() == "7"){
             checkSeven(marblesOnFieldAndNotFinished, marblesFinished, marblesOnField, game);
        } else if (c.getCardValue() == "2"  || c.getCardValue() == "3" ) {
            for (Marble m : marblesOnFieldAndNotFinished) {
                if (checkMove(m, c.getCardMoveValue(), game)) {
                    possibleMarbles.add(m);
                }
            }
        } else {
            for (Marble m: marblesOnField){
                if(!m.getHome() && !m.getFinish()){
                    if (checkMove(m ,c.getCardMoveValue(), game)){
                        possibleMarbles.add(m);
                    }
                }
            }
        }
        return possibleMarbles;
    }
    // First get all movable Marbles (On Field and not finished)
    // Delete the ones being blocked (next Field == StartField beeing blocked). They can't move anyways
    // Check if total moves is greater than 7 -> player can move (because even if he wants to finish he can go 7 around)
    // Case 2: total count of possible steps for all marble movables is greater than 7 -> all marbles return
    // Case 3: Count < 7: Player cant play (It only less than 7 if he is stuck behind someone else)

    // Input: Fields who are finished and cant move anymore, Fields Onfield  && in finished fields, marbles on field
    //MOVABLE MARBLES : All marbles who can move (MarblesOnField who can move and in finished)

        /*
        falls mit Marbles Teammate:
        Check if totaldistance to go home is smaller 7
        (dont forget: Anzahl marbles im feld noch nicht im finishfeld, je nachdem verkleinert sich die anzahl steps home
        (z.b. 2 marbles -> -1, 3 Marbles -> -3)
        */
    private List<Marble> checkSeven(List<Marble> marblesOnFieldAndNotFinished, List<Marble> marblesFinished,List<Marble> marblesOnField, Game game ) {

        List<Marble> movableMarbles = null;
        List<Marble> movableMarblesNotHome = null;

        //remove blocked marbles
        for (Marble m : marblesOnFieldAndNotFinished) {
            // add all marbles who are in finish zone and can move to movable marbles
            if (m.getCurrentField() instanceof FinishField) {
                movableMarbles.add(m);
            }
            else {
                // add al marbles on field who dont have a chance of being blocked to both list
                if (!(m.distanceToNextStartField() == 1)) {
                    movableMarbles.add(m);
                    movableMarblesNotHome.add(m);
                    // check if marbles who are infront of startfield if it is blocking and if it is check for same color
                }
                else if (m.distanceToNextStartField() == 1) {
                    int currentFieldVal = m.getCurrentField().getFieldValue();
                    int fieldValToCheck = currentFieldVal + 1;
                    Color color = m.getColor();
                    if (fieldValToCheck == 68) {
                        fieldValToCheck = 4;
                    }
                    Field fieldToCheck = game.getPlayingBoard().getField(fieldValToCheck);
                    if (!(fieldToCheck.getFieldStatus().equals(FieldStatus.BLOCKED) && !(fieldToCheck.getColor().equals(m.getColor())))) {
                        movableMarbles.add(m);
                        movableMarblesNotHome.add(m);
                    }
                }
            }}
            int totalCountStepsAllMarblesMovable = 0;
            for (Marble ma: movableMarbles){
                int steps = nrStepsToNextBlock(ma.getCurrentField(), 7, game);
                totalCountStepsAllMarblesMovable += steps;
            }
            //Case 7 cant be played
            if(totalCountStepsAllMarblesMovable < 7){
                log.info("7 cant be played");
                return null;

            } return movableMarbles;

    }

        // Returns all possible marbles for all possible values
        // Takes marblesOnfieldAndNotfinished(ALL Marbles who can still move) Check if these Marbles can make possible move values.
        // add all marbles who are at home and if startfield is free
        //add al marbles who can either make move 1 or/and 11 case ACE else 13

        private List<Marble> checkAceAndKing(List < Marble > marblesOnFieldAndNotFinished, List < Marble > marblesAtHome, Card c, Game game){
            List<Marble> possibleMarbles = null;
            Color color = null;
            for (Marble m : marblesAtHome) {
                color = m.getColor();
                if (game.getPlayingBoard().getHomeFieldIsNotBlocked(color)) {
                    possibleMarbles.add(m);
                }
            }
            for (Marble m : marblesOnFieldAndNotFinished) {
                if (checkMove(m, c.getCardMoveValue(), game)) {
                    possibleMarbles.add(m);
                }
            }
            //See which marbles
            if (possibleMarbles.isEmpty()) {
                log.info("No marble possible with this card (ACE OR KING)");
            }

            for (Marble m : possibleMarbles) {
                int i = m.getCurrentField().getFieldValue();
                Boolean isHome = m.getHome();
                log.info("Marble : " + color + "FieldVal: " + i + "ishome: " + isHome);
            }
            return possibleMarbles;
        }
        // With Teammate Marbles
        // First get all Marbles onField. Then add all Marbles who are  allowed to be changed to possible Marbles(Not on home and blocking), otherwise delete
        // then Look at size of possible Marbles (2) and marblesPlayer(1) needs at least one marbles. BOth together at least two.
        //returns list of marbles of both players
        public List<Marble> checkJack (List < Marble > marblesOnField, Player p){
            List<Marble> marblesMate = p.getTeamMate().getMarblesOnField();
            List<Marble> marblesPlayer = marblesOnField;
            List<Marble> possibleMarbles = null;
            for (int i = 0; i < 4; i++) {
                if (marblesMate.get(i).getMarbleIsBlockingAndOnStart()) {
                    marblesMate.remove(marblesMate.get(i));
                }
                else {
                    possibleMarbles.add(marblesMate.get(i));
                }
                if (marblesPlayer.get(i).getMarbleIsBlockingAndOnStart()) {
                    possibleMarbles.remove(marblesPlayer.get(i));
                }
                else {
                    possibleMarbles.add(marblesPlayer.get(i));
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
        public int nrStepsToNextBlock(Field startField, int moveValue, Game game){
            LinkedList<Field> playingFields = game.getPlayingBoard().getListPlayingFields();
            Field startingField = startField;
            int fieldNrStart = startingField.getFieldValue();
            int valFieldToCheck = fieldNrStart + 1;
            for (Field f : playingFields) {
                if (f.equals(startingField)) {
                    for (int i = 0; i < moveValue; i++) {
                        if (valFieldToCheck > 67) {
                            valFieldToCheck = valFieldToCheck - 67 + 3;
                        }
                        Field fieldToCheck = playingFields.get(valFieldToCheck);
                        if (!(fieldToCheck.getFieldStatus().equals(FieldStatus.BLOCKED))) {
                            return i;
                        } else if (fieldToCheck instanceof FinishField) {
                            if (!(fieldToCheck.getFieldStatus().equals(FieldStatus.OCCUPIED))) {
                                return i;
                            }
                        }
                    }
                }
            }
            return moveValue;
        }


        // first get the currentfield and set as start Filed of current move.
        // iterate over possible cardmovevalues and see if marble can make one of the moves return TRUE;
        // if the card value is 4, set startmove Field back 4.
        // check how marble can make move: first find current field, then make as many steps as the card value. if one field is blocking no count++
        // if count is eqaul value the marble can make the move
        public Boolean checkMove (Marble marble, List < Integer > values, Game game){
            int count = 0;
            Field startingFieldMove = marble.getCurrentField();
            for (Integer v : values) {
                if (v == -4) {
                    List<Integer> valueToCheck = null;
                    valueToCheck.add(4);
                    valueToCheck.add(5);
                    valueToCheck.add(6);
                    valueToCheck.add(7);
                    int newStartFieldVal;
                    if (valueToCheck.contains(marble.getCurrentField().getFieldValue())) {
                        newStartFieldVal = marble.getCurrentField().getFieldValue() + 60;
                    }
                    else {
                        newStartFieldVal = marble.getCurrentField().getFieldValue() - 4;
                    }
                    startingFieldMove = game.getPlayingBoard().getField(newStartFieldVal);
                }
                count = nrStepsToNextBlock(startingFieldMove, v, game);
                if (v == count) {
                    log.info("Marble : " + marble.getColor() + "FieldVal: " + marble.getCurrentField().getFieldValue() + "ishome: " + marble.getHome());
                    return TRUE;
                }
            }
            log.info("Cardvalue" + values + "not playeble");
            return FALSE;
        }
        public void makeMove(Player p, Card c, String move, Marble marble, Field endField, Game game){
            int moveInIntForward = c.changeForwardMoveToValue(move);
            //Check if distance to endfield is equal moveForward;
            if(move.contains("Forward")){
                if (endField instanceof FinishField){
                    int distanceToFreeFinishSpot = game.getPlayingBoard().distanceToNextFreeFinishSpot(marble.getColor(), marble.getCurrentField());
                    if(distanceToFreeFinishSpot < moveInIntForward){
                        log.info("you can't go home with this move");
                    }
                } else {
                    int distance = 0;
                    int fieldValCurrent = marble.getCurrentField().getFieldValue();
                    int fieldValEnd = endField.getFieldValue();
                    if (fieldValEnd < 15 && fieldValCurrent > 50) {
                        distance = marble.distanceToNextStartField() + fieldValEnd;
                    }
                    else {
                        distance = fieldValEnd - fieldValCurrent;
                    }
                    if (distance != moveInIntForward) {
                        log.info("Endfield not reachable with this move");
                    }
                }
            }

            if(!(p.equals(game.getCurrentRound().getCurrentPlayer()))){
                log.info("Not the right player");
                // return exception?
            }
            if (!(p.getHand().getHandDeck().contains(c))){
                log.info("player doesn't have this card");
                //return exception
            }
            if(!(c.getCardMoveValue().contains(move))){
                log.info("invalid move for this card");
                //return exception
            }
            if(!(getPlayableMarble(c, move, game).contains(marble))){
                log.info(("invalid move for marble "));
            }
            if(move.contains("Start")){
                if (chekForStart(marble, endField)){
                    eat(endField, game);
                    game.getPlayingBoard().marbleGoesToStart(marble.getColor());
                    log.info("marble start successful");
                }
            } else if (move.contains("Exchange")){
                if (checkForJackMove(marble, endField, p)){
                    game.getPlayingBoard().marbleMoveJack(endField, marble);
                    log.info("marble exchange successful");
                }
            } else {
                if (!(endField instanceof FinishField)){
                    eat(endField, game);
                    game.getPlayingBoard().makeMove(endField, marble);
                    log.info("marble move forward/backward successful");
                } else if (game.getPlayingBoard().distanceToNextFreeFinishSpot(marble.getColor(),endField)  == 0){
                    game.getPlayingBoard().makeMove(endField, marble);
                    log.info("marble move into finish sector successful");
                } else {
                    game.getPlayingBoard().makeFinishMove(endField, marble);
                    log.info("marble finished successful");
                }

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



        public Boolean checkForJackMove(Marble m, Field endField, Player p){
            if(endField.getFieldStatus().equals(FieldStatus.FREE)){
                log.info("NO marble here to change with");
                return FALSE;
            }
            if(endField.getFieldStatus().equals(FieldStatus.OCCUPIED)) {
                Marble marbleMate = endField.getMarble();
                if (!(p.getTeamMate().getMarblesOnFieldAndNotFinished().contains(marbleMate))) {
                    log.info("NO valid marble to change with");
                    return FALSE;
                }

            }
            log.info("Jack can be played");
            return TRUE;
        }
        public void eat(Field endField, Game game){
            if (endField.getFieldStatus().equals(FieldStatus.OCCUPIED)){
                Marble marbleToEat = endField.getMarble();
                game.getPlayingBoard().sendHome(marbleToEat);
            }
        }


        public Boolean chekForStart(Marble m, Field endField){
            if (!(endField instanceof StartField)){
                log.info("Move is Start but Endfield Move Not Start Field");
                return FALSE;
            }
            if(endField instanceof StartField){
                if(!(m.getColor().equals(endField.getColor()))){
                    log.info("Not you StartField");
                    return FALSE;
                } else if (endField.getFieldStatus().equals((FieldStatus.BLOCKED))){
                    log.info("Your StartField is blocked");
                    return FALSE;
                }
            }
            log.info("Your StartField and Move is start");
            return TRUE;
        }
}

