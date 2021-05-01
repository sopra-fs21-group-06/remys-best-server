package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.logging.log4j.ThreadContext.isEmpty;
import static org.springframework.data.util.Pair.*;


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
    // New Round initiated, then Send Card to Player and GameStats
    public void InitiateRound(){
        Round currentRound = new Round(game.getPlayerList(),game.getStartPlayer(),game.getNrCards(),game, cardAPIService);
        game.setCurrentRound(currentRound);

    public static GameService getInstance() {
        return instance;
    }

    // New Round initiated, then Send Card to Player and GameStats
    public void initiateRound(Game game){
        Round currentRound = new Round(game.getPlayerList(),game.getStartPlayer(),game.getNrCards(),game, cardAPIService,webSocketService, userService);
        game.setRoundCount(1);
        game.setCurrentRound(currentRound);
    }

    public List<Player> getPlayers(Game game){
        return game.getPlayerList();
    }
    public void sendHandToPlayer(){
        // each player already has a hand!
        log.info("Hand sent to player");
    }
    public void sendCardExchange(){
        //Send currentPlayer and Card Exchange
        log.info("Card Exchange");
    }
    public void updateRoundStats(){
        int indexCurrent;
        indexCurrent = this.game.getPlayerList().indexOf(this.game.getStartPlayer());
        int indexNext = (indexCurrent + 1) % 4;
        this.game.setStartPlayer(this.game.getPlayerList().get(indexNext));
        this.game.addToRoundCount();
        this.game.changeNrCards();
    }
    public void changeCardsWithTeamMate(){
        //Make change
        sendHandToPlayer();
        log.info("Hand Change worked");
        triggerNewRound();

    public void updateRoundStats(Game game){
        game.changeCurrentPlayer();
        game.addToRoundCount();
        game.changeNrCards();
    }


    public Boolean triggerNewRound(Game game){
        // send Roundnr, Active Player, next Card Amount and next Beginner
        //send CurrentPlayer if he can play

        return canPlay(game.getStartPlayer(), game);
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


    public Pair<Integer, String> makeMove(String fieldKey, Marble marble, Game game) {
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        Card c = null;
        String moveToDo = currentPlayer.getCurrentMove();
        String cardCodeCurrent = currentPlayer.getCurrentCardCode();
        List<Card> hand = currentPlayer.getHand().getHandDeck();
        Pair <Integer, String> result = Pair.of(0,"");

        for(Card card: hand){
            if(card.getCode().equals(cardCodeCurrent)){
                c = card;
            }

        }

        // marble can nicht bewegt werden resp falsche
        if (!(getPlayableMarble(c, moveToDo,game).contains(marble))){
            log.info("Wrong marble (makeMOve)");

        }
        // falscher endfield key
        if (!(getPossibleTargetFields(marble, moveToDo,currentPlayer.getCurrentCardCode(), game).contains(fieldKey))){
            log.info("Wrong endFieldKey (makeMOve)");

        }
        Field endField = game.getPlayingBoard().getFieldWithFieldKey(fieldKey);
        if(moveToDo.contains("Start")){
            eat(endField, game);
            game.getPlayingBoard().marbleGoesToStart(marble.getColor());
            log.info("marble start successful");
            result = Pair.of(marble.getMarbleNr(), fieldKey);
        } else if (moveToDo.contains("Exchange")){
            game.getPlayingBoard().marbleMoveJack(endField, marble);
            log.info("marble exchange successful");
            result = Pair.of(marble.getMarbleNr(), fieldKey);
        // Case forward and into finishSector
        } else if (endField instanceof FinishField){
            int distance = nrStepsToNextFreeFinishSpot(endField, game);
            // if marble filled up
            if(distance == 0){
                game.getPlayingBoard().makeFinishMove(endField, marble);
                log.info("marble finished successful");
            } else {
                game.getPlayingBoard().makeMove(endField, marble);
                log.info("marble moved into finished sector successful");
            }
            result = Pair.of(marble.getMarbleNr(), fieldKey);
            // normal back forward
        } else {
            eat(endField, game);
            game.getPlayingBoard().makeMove(endField, marble);
            log.info("marble move forward/backward successful");
            result = Pair.of(marble.getMarbleNr(), fieldKey);
        }
        if(!result.getFirst().equals(marble.getMarbleNr())) {
            log.info("makeMove didnt work, still playersTurn");
            return null;
        }
        //Check if player has all his marbles in finished if true then set marbles to teammate
        List<Marble> playerMarble = currentPlayer.getMarbleList();
        int countMarbleFinished = 0;
        int countMarbleFinishedTeamMate = 0;
        List<Marble> mateMarble = currentPlayer.getTeamMate().getMarbleList();
        for(int i = 0; i < mateMarble.size(); i++) {
            if(playerMarble.get(i).getFinish()){
                countMarbleFinished++;
            }else if(mateMarble.get(i).getFinish()){
                countMarbleFinishedTeamMate++;
            }
        }
        if(countMarbleFinishedTeamMate == mateMarble.size() && countMarbleFinished ==playerMarble.size()){
            log.info("makeMove worked, and player and Teammate have finished");
        }
        if (countMarbleFinished == 4){
            log.info("makeMove worked, and player has finished, teammate hasnt, change of marbles");
            currentPlayer.setMarbleList(mateMarble);
        }


        log.info("makeMove worked, next players turn or new round");
        //Chekck for new turn (if at least one player can play -> new turn
        int count = 0;
        int handCount = 0;
        for(Player p: game.getPlayerList()){
            if(canPlay(p, game)) {
                count++;
            }
            if(!p.getHand().getHandDeck().isEmpty()){
                handCount++;
            }
        }
        if (count > 0 && handCount >0){
            log.info("makeMove worked, next players turn");
            game.getCurrentRound().changeCurrentPlayer();

        } else if (handCount == 0){
            log.info("makeMove worked, no Player has cards in his hands, new round");
            updateRoundStats(game);
            initiateRound(game);
        } else if(count == 0){
            log.info("makeMove worked, no Player can play but still cards in the game, new round");
            updateRoundStats(game);
            initiateRound(game);
        }
        currentPlayer.setCurrentMove("");
        currentPlayer.setCurrentCardCode("");
        return result;
    }

    //returns possible moves after player clicks on cards
    public List<String> sendCardMove(Card c){
        List<String> movesToDisplay= c.getMovesToDisplay();
        for (String s: movesToDisplay) {
            log.info(s);
        }
        return movesToDisplay;
    }

    // return playabe Marble for Player
    public List<Marble> getPlayableMarble(Card c, String move, Game game){
        List<Marble> possibleMarbles = new ArrayList<>();
        Player p = game.getCurrentRound().getCurrentPlayer();
        List<Marble> marblesOnField = p.getMarblesOnField();
        List<Marble> marblesOnFieldAndNotFinished = p.getMarblesOnFieldAndNotFinished();
        List<Marble> marblesFinished = p.getMarblesInFinishFieldAndFinished();
        List<Marble> marblesOnFieldNotHomeNotOnStart = p.getmarblesOnFieldNotHomeNotOnStart();
        String cardValue = c.getCardValue();
        //CHeck if player actually has corresponding card to move
        List<Card> hand = game.getCurrentRound().getCurrentPlayer().getHand().getHandDeck();
        int countMove = 0;
        for(Card card: hand){
            if(card.getCode().equals(c.getCode())){
                countMove++;
            }

        }
        if(countMove == 0){
            log.info("no card in his hand in getPlayablemarble");
            return possibleMarbles;
        }

        if ("J".equals(cardValue)){
            return marblesOnFieldNotHomeNotOnStart;
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
        } else if(c.getCardValue().equals("7")){
             checkSeven(marblesOnFieldAndNotFinished, marblesFinished, marblesOnField, game);
        } else if (c.getCardValue().equals("2")  || c.getCardValue().equals("3") ) {
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

        List<Marble> movableMarbles = new ArrayList<>();
        List<Marble> movableMarblesNotHome = new ArrayList<>();

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
                    Field fieldToCheck = game.getPlayingBoard().getField(fieldValToCheck, color);
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
    public int nrStepsToNextBlock(Field startingFieldMove, int moveValue, Game game){
        if(startingFieldMove instanceof FinishField){
            return nrStepsToNextFreeFinishSpot(startingFieldMove, game);
        } else if (!(startingFieldMove instanceof FinishField)){
            return nrStepsToNextStartFieldBlock(startingFieldMove,  moveValue,  game);
        } else {
            log.info(("YOu are home nrnextstop 0"));
            return 0;
        }
    }
    // return nr to next start block if the next one is blocked else return 100
    public int nrStepsToNextStartFieldBlock(Field startingFieldMove, int moveValue, Game game){
        LinkedList<Field> playingFields = game.getPlayingBoard().getListPlayingFields();
        Color currentColor = startingFieldMove.getColor();
        Color nextColor = game.getPlayingBoard().getNextColor(currentColor);
        if(game.getPlayingBoard().getNextStartFieldIsBlocked(currentColor) && !startingFieldMove.getFieldStatus().equals(FieldStatus.BLOCKED)){
            return 16 - startingFieldMove.getFieldValue();
        } else {
            return 100;
        }
    }
    // Start is at first HomeField if all are interesting
    public int nrStepsToNextFreeFinishSpot(Field startField, Game game){
        List<Field> finishField = game.getPlayingBoard().getFinishFields(startField.getColor());
        int count = 0;
        for (Field f: finishField){
            if(startField.getFieldValue() < f.getFieldValue()){
                if(f.getFieldStatus().equals(FieldStatus.FREE)){
                    count++;
                } else if (f.getFieldStatus().equals(FieldStatus.OCCUPIED)){
                    return count;
                }
            }
        }
        return count;
    }
    // Case 1: move is To go to start
    // Case2: Exchange
    // case 3: Forward
    // case 4: Backwards
    // TO DO case 5: seven
    public List<String> getPossibleTargetFields(Marble marble, String moveName, String cardCode, Game game){
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        currentPlayer.setCurrentMove(moveName);
        currentPlayer.setCurrentCardCode(cardCode);
        //If this marble is a marble of the player
        if(!(currentPlayer.getMarbleList().contains(marble))){
            log.info("Not your marble to play(getPossibleTargetFields)");
        }
        //find a card with corresponding move name and check if player has it

        List<Card> hand = currentPlayer.getHand().getHandDeck();
        Card c = null;

        for(Card card: hand){
            if(card.getCode().equals(cardCode)){
                c = card;
                break;
            }

        }
        if(c.equals(null)){
            log.info("Player doesnt have Card to make this move ((getPossibleTargetFields)");
        }
        // Check if marble is playable with this move
        List<Marble> possibleM = getPlayableMarble(c,moveName,game);
        Boolean cond = TRUE;
        for(Marble m: possibleM){
            if(m.getMarbleNr() == marble.getMarbleNr()){
                log.info("This Marble is Playable with this move (getPossibleTargetFields)");
                cond = FALSE;

            }
        }
        if (cond){
            log.info("This Marble isnt Playable with this move (getPossibleTargetFields)");
            return possibleTargetFieldKeys;
        }

        // case1: Check for start, first get the StartingFIeld of this marble.
        // If startfield is not blocked add startfield to possible fields
        if(moveName.contains("Start")){
            Field targetField = (Field) game.getPlayingBoard().getField(16, marble.getColor());
           if (startFieldIsPossibleEndFieldMove(targetField)){
               possibleTargetFieldKeys.add(targetField.getFieldKey());
           }
       // case2: jack: get all marbles from player and teammate onfield, not infinish and not blocking.
        } else if (moveName.contains("Exchange")){
            List<Marble> toChangeWith = getMarblesToChangeWithJack(currentPlayer.getMarblesOnFieldAndNotFinished(), currentPlayer);
            for(Marble m: toChangeWith){
                if(!(m.equals(marble))){
                    possibleTargetFieldKeys.add(m.getCurrentField().getFieldKey());
                }
            }
        //case3 Forward x: if x is smaller than the distance to next startfield, value of endfield: currentfieldval + moveToint

        } else if (moveName.contains("Forward")){
            int moveToInt = game.getPlayingBoard().changeForwardMoveToValue(moveName);
            int distanceNextStartField = 16 - marble.getCurrentField().getFieldValue();
            int valueFieldNew = 0;
            Color colorFieldCurrentField = marble.getCurrentField().getColor();
            Color colorNextField = null;
            // check if nextStartField is Blocked and distance to block is smaller than move
            if(game.getPlayingBoard().getNextStartFieldIsBlocked(colorFieldCurrentField) && moveToInt > distanceNextStartField && !marble.getCurrentField().getFieldStatus().equals(FieldStatus.BLOCKED)){
                log.info("Forward not possible with this move because move is bigger than distance to ext blocking startfield(getPossibleTargetFields)");
                return null;
            }
            // check if possibleEndfield is on next Part of Game -> CHange color and Value
            // Case 2 fields into home and not
            if (marble.getCurrentField().getFieldValue() + moveToInt < 21 && marble.getCurrentField().getFieldValue() + moveToInt > 17 && marble.getCurrentField().getColor().equals(marble.getColor()) && !marble.getCurrentField().getFieldStatus().equals(FieldStatus.BLOCKED)){
                int valueFieldNew1 = moveToInt - distanceNextStartField;
                int valueFieldNew2 = marble.getCurrentField().getFieldValue() + moveToInt;
                Color cFieldNew1 = game.getPlayingBoard().getNextColor(colorFieldCurrentField);
                Color cFieldNew2 = marble.getColor();
                Field targetField1 = game.getPlayingBoard().getField(valueFieldNew1, cFieldNew1);
                possibleTargetFieldKeys.add(targetField1.getFieldKey());
                Field targetField2 = game.getPlayingBoard().getField(valueFieldNew2, cFieldNew2);
                possibleTargetFieldKeys.add(targetField2.getFieldKey());
            } else if ( marble.getCurrentField().getFieldValue() + moveToInt > 16){
                colorNextField= game.getPlayingBoard().getNextColor(colorFieldCurrentField);
                valueFieldNew = moveToInt - distanceNextStartField;
                Field targetField = game.getPlayingBoard().getField(valueFieldNew, colorNextField);
                possibleTargetFieldKeys.add(targetField.getFieldKey());
            } else {
                valueFieldNew = marble.getCurrentField().getFieldValue() + moveToInt;
                colorNextField = colorFieldCurrentField;
                Field targetField = game.getPlayingBoard().getField(valueFieldNew, colorNextField);
                possibleTargetFieldKeys.add(targetField.getFieldKey());
            }
            // add the field that is after oder befor next startblock anyways

            // Check if finishfield is reachable with this move(wenn eigene farbe und distance ist kleiner als moveInt), also get coresponding field in finish
            // Field startField, int moveValue, Game game)
            /*Field startField = game.getPlayingBoard().getRightColorStartField(marble.getColor());
            int distanceHomeFieldToFirstFreeFinishSpot = nrStepsToNextFreeFinishSpot(startField, game);
            int distanceCurrentFieldToFirstFreeSpot = distanceNextStartField + distanceHomeFieldToFirstFreeFinishSpot;
            // M is in right part board,  and moveInt is between distance to StartField and first finishField that is free
            if (colorFieldCurrentField.equals(marble.getColor()) && (distanceNextStartField< moveToInt && moveToInt < distanceCurrentFieldToFirstFreeSpot )){
                Field possTargetField = game.getPlayingBoard().getField(moveToInt, marble.getColor());
                possibleTargetFieldKeys.add(possTargetField.getFieldKey());
            }*/
        } else if (moveName.contains("Back")){
            int fieldNr = marble.getCurrentField().getFieldValue() - 4;
            Color colorEndField = marble.getCurrentField().getColor();
            if (fieldNr < 1){
                fieldNr = 16 + fieldNr;
                colorEndField = game.getPlayingBoard().getPreviousColor(marble.getColor());
            }
            Field possTargetField = game.getPlayingBoard().getField(fieldNr, colorEndField);
            possibleTargetFieldKeys.add(possTargetField.getFieldKey());
        }
        return possibleTargetFieldKeys;

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
    // With Teammate Marbles
    // First get all Marbles onField. Then add all Marbles who are  allowed to be changed to possible Marbles(Not on home and blocking), otherwise delete
    // then Look at size of possible Marbles (2) and marblesPlayer(1) needs at least one marbles. BOth together at least two.
    //returns list of marbles of both players
    public List<Marble> getMarblesToChangeWithJack (List < Marble > marblesOnField, Player p){
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

        // first get the currentfield and set as start Filed of current move.
        // iterate over possible cardmovevalues and see if marble can make one of the moves return TRUE;
        // if the card value is 4, set startmove Field back 4.
        // check how marble can make move: first find current field, then make as many steps as the card value. if one field is blocking no count++
        // if count is eqaul value the marble can make the move
        public Boolean checkMove (Marble marble, List < Integer > values, Game game){
            int count;
            Field startingFieldMove = marble.getCurrentField();
            Color c = marble.getCurrentField().getColor();
            for (Integer v : values) {
                if (v == -4) {
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
                    startingFieldMove = game.getPlayingBoard().getField(newStartFieldVal,c);
                }
                count = nrStepsToNextStartFieldBlock(startingFieldMove, v, game);
                if (v < count) {
                    log.info("Marble : " + marble.getColor() + "FieldVal: " + marble.getCurrentField().getFieldValue() + "ishome: " + marble.getHome());
                    return TRUE;
                }
            }
            log.info("Cardvalue" + values + "not playeble");
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




        public void eat(Field endField, Game game){
            if (endField.getFieldStatus().equals(FieldStatus.OCCUPIED)){
                Marble marbleToEat = endField.getMarble();
                game.getPlayingBoard().sendHome(marbleToEat);
            }
        }

    public Marble getMarbleByGameIdMarbleIdPlayerName(Game game, String playerName, int marbleId){
        for(Player p: game.getPlayerList()){
            if(p.getPlayerName().equals(playerName)){
                for(Marble m : p.getMarbleList()){
                    if(m.getMarbleNr() == marbleId){
                        log.info(String.valueOf(m.getMarbleNr()));
                        return m;
                    }
                }
            }
        }
        log.info("Not good with Marbleconversion");
        return null;
    }



        /*
                // Returns all possible marbles for all possible values
        // Takes marblesOnfieldAndNotfinished(ALL Marbles who can still move) Check if these Marbles can make possible move values.
        // add all marbles who are at home and if startfield is free
        //add al marbles who can either make move 1 or/and 11 case ACE else 13

        private List<Marble> checkAceAndKing(List < Marble > marblesOnFieldAndNotFinished, List < Marble > marblesAtHome, Card c, Game game){
            List<Marble> possibleMarbles = null;
            Color color = null;
            for (Marble m : marblesAtHome) {
                color = m.getColor();
                if (game.getPlayingBoard().getStartFieldIsNotBlocked(color)) {
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
                public void makeMove(Player p, Card c, String move, Marble marble, Field endField, Game game){
            int moveInIntForward = c.changeForwardMoveToValue(move);
            //Check if distance to endfield is equal moveForward;
            if(move.contains("Forward")){
                if (endField instanceof FinishField){
                    int distanceToFreeFinishSpot = game.getPlayingBoard().distanceToNextFreeFinishSpot(marble.getColor());
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
                public int nrStepsToNextBlock(Field startField, int moveValue, Game game){
            LinkedList<Field> playingFields = game.getPlayingBoard().getListPlayingFields();
            Field startingField = startField;
            int fieldNrStart = startingField.getFieldValue();
            int valFieldToCheck = fieldNrStart + 1;
            for (Field f : playingFields) {
                if (f.equals(startingField)) {
                    for (int i = 0; i < moveValue; i++) {
                        if (valFieldToCheck > 16) {
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

         */

    public UserService getUserService() {
        return userService;
    }
}

