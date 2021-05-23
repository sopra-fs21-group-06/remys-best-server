package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class SplitSeven implements ISplitMove {
    Logger log = LoggerFactory.getLogger(SplitSeven.class);

    @Override
    public String getName() {
        return "Split 7";
    }
    @Override
    //Cases done: Marble ist im finishsector (alle die drin sind)
    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove, ArrayList<MarbleIdAndTargetFieldKey> sevenMoves) {
        int countToRemainSeven = game.getGameService().getRemainingSevenMoves(game, sevenMoves);
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        Field marbleCurrentField = marbleToMove.getCurrentField();
        log.info("cunrremaint" + String.valueOf(countToRemainSeven));
        boolean changesStartField = FALSE;
        boolean changeFinishField = FALSE;
        List<String> toChangeBack = new ArrayList<>();
        for (MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey : sevenMoves) {
            Marble marble = game.getGameService().getMarbleByMarbleIdForSeven(game, marbleIdAndTargetFieldKey.getMarbleId());
            if (marbleIdAndTargetFieldKey.getMarbleId() == marbleToMove.getMarbleId()) {
                marbleCurrentField = game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKey.getFieldKey());
            }
            if (marble.getCurrentField().getFieldStatus().equals(FieldStatus.BLOCKED)) {
                changesStartField = TRUE;
                game.getPlayingBoard().getRightColorStartField(marble.getColor()).setFieldStatus(FieldStatus.FREE);
            }
            if (game.getPlayingBoard().finishFieldIsFinishMoveField(game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKey.getFieldKey()))) {
                changeFinishField = TRUE;
                toChangeBack.add(marbleIdAndTargetFieldKey.getFieldKey());
                game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKey.getFieldKey()).setFieldStatus(FieldStatus.OCCUPIED);
            }

        }

        if (!(marbleCurrentField instanceof FinishField)) {
            possibleTargetFieldKeys.addAll(getNormalTargetFieldsKeys(game, marbleToMove, marbleCurrentField, countToRemainSeven));
        }
        boolean isInFinish = marbleCurrentField instanceof FinishField;
        boolean marbleCanGetToFinishSpot = game.getPlayingBoard().marbleCanGetToFinishSpot(marbleToMove, marbleCurrentField, countToRemainSeven);
        boolean moveToFinish = game.getPlayingBoard().marbleCanMoveInFinishFieldWithLeftValueCard(marbleToMove, marbleCurrentField, countToRemainSeven);

        if (isInFinish || marbleCanGetToFinishSpot || moveToFinish) {
            possibleTargetFieldKeys.addAll(getFinishFieldKeys(game, marbleToMove, marbleCurrentField, countToRemainSeven));
        }
        if (changesStartField) {
            game.getPlayingBoard().getRightColorStartField(marbleToMove.getColor()).setFieldStatus(FieldStatus.BLOCKED);
        }

        if (changeFinishField) {
            for (String s : toChangeBack) {
                game.getPlayingBoard().getFieldByFieldKey(s).setFieldStatus(FieldStatus.FREE);
            }
        }

        return possibleTargetFieldKeys;

    }
    private List<String> getFinishFieldKeys(Game game, Marble marbleToMove, Field marbleCurrentField, int countToRemainSeven){
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        List<Field> finishFields= game.getPlayingBoard().getFinishFields(marbleToMove.getColor());
        boolean condition = TRUE;
        int countSteps = 1;
        if(marbleCurrentField.getFieldValue() < 17){
            countSteps = 16 - marbleCurrentField.getFieldValue();
        }
        for (Field f: finishFields) {
            if (f.getFieldStatus().equals(FieldStatus.OCCUPIED) && !(f.getFieldValue() == marbleCurrentField.getFieldValue())) {
                condition = FALSE;
            }
            if (condition && countSteps < countToRemainSeven && marbleCurrentField.getFieldValue() < f.getFieldValue()) {
                possibleTargetFieldKeys.add(f.getFieldKey());
                countSteps++;
            }
            else {
                break;
            }

        }
        return  possibleTargetFieldKeys;
    }

    private List<String> getNormalTargetFieldsKeys(Game game, Marble marbleToMove, Field marbleCurrentField, int countToRemainSeven){
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        int fieldValCurrentField = marbleCurrentField.getFieldValue();
        Color colorCurrentField = marbleCurrentField.getColor();
        for(int i = 1; i <= countToRemainSeven; i++){
            int fieldValToCheck = fieldValCurrentField + i;
            Color colorFieldToCheck = colorCurrentField;
            if(fieldValToCheck > 16){
                fieldValToCheck = fieldValToCheck - 16;
                colorFieldToCheck = DogUtils.getNextColor(colorCurrentField);
            }
            Field fieldToCheck = game.getPlayingBoard().getField(fieldValToCheck, colorFieldToCheck);
            if(!fieldToCheck.getFieldStatus().equals(FieldStatus.BLOCKED)){
                possibleTargetFieldKeys.add(fieldToCheck.getFieldKey());
            } else {
                break;
            }
        }
        return possibleTargetFieldKeys;
    }

    public List<Marble> getPlayableMarbles(Game game, GameService gameService, ArrayList<MarbleIdAndTargetFieldKey> sevenMoves) {
        List<Marble> possibleMarbles = new ArrayList<>();
        //Case Player can make the Move by Himself
        List<Marble> marblePlayer = game.getCurrentRound().getCurrentPlayer().getMarblesOnFieldAndNotFinished();
        List<Marble> marbleMate = game.getCurrentRound().getCurrentPlayer().getTeamMate().getMarblesOnFieldAndNotFinished();

        List<Marble> possiblePlayerMarble = getNrMarblesCanMakeRemainingSevenMoves(game, gameService, sevenMoves, game.getCurrentRound().getCurrentPlayer(), marblePlayer);
        List<Marble> possibleTeamMateMarble = getNrMarblesCanMakeRemainingSevenMoves(game, gameService, sevenMoves, game.getCurrentRound().getCurrentPlayer().getTeamMate(), marbleMate);
        if(possibleTeamMateMarble.isEmpty() && possiblePlayerMarble.isEmpty()){
            //case kann sieben nicht spielen
            log.info("RETURN POSSIBLE MARBE 2");
            return possibleMarbles;
        }else if(!possiblePlayerMarble.isEmpty()){
            log.info("RETURN POSSIBLE MARBE 1");
            log.info((game.getCurrentRound().getCurrentPlayer().getPlayerName()));
            return possiblePlayerMarble;
        } else if (!possibleTeamMateMarble.isEmpty()){
            log.info("RETURN POSSIBLE MARBE 3");
            return possibleTeamMateMarble;
        }
        return possibleMarbles;
    }

    private List<Marble> getNrMarblesCanMakeRemainingSevenMoves(Game game, GameService gameService, ArrayList<MarbleIdAndTargetFieldKey> sevenMoves, Player player, List<Marble> stillMovable) {
        int countToRemainSeven = game.getGameService().getRemainingSevenMoves(game, sevenMoves);
        List<Marble> possibleMarbles = new ArrayList<>();
        List<Marble> marbleToDelete = new ArrayList<>();
        List<Marble> marbleAreFinished = new ArrayList<>();
        int countPossibleStepsAllMarbles = 0;
        for (Marble m : stillMovable) {
            log.info("MARBLE NR und COlor NOT FINISHED :" + String.valueOf(m.getColor()) + String.valueOf(m.getMarbleId()));
            for (MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey : sevenMoves) {
                if (marbleIdAndTargetFieldKey.getMarbleId() == m.getMarbleId()) {
                    Field marbleCurrentField = game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKey.getFieldKey());
                    if (game.getPlayingBoard().finishFieldIsFinishMoveField(marbleCurrentField)) {
                        marbleToDelete.add(m);
                        marbleAreFinished.add(m);

                    } else if (game.getPlayingBoard().nrStepsToNextFreeFinishSpot(marbleCurrentField) == 1){
                        marbleToDelete.add(m);
                    }
                    if (game.getPlayingBoard().nrStepsToNextStartFieldBlock(marbleCurrentField) == 1) {
                        marbleToDelete.add(m);
                    }
                }
            }
            if (m.getCurrentField() instanceof FinishField) {
                countPossibleStepsAllMarbles += game.getPlayingBoard().nrStepsToNextFreeFinishSpot(m.getCurrentField());
            } else {
                countPossibleStepsAllMarbles += game.getPlayingBoard().nrStepsToNextStartFieldBlock(m.getCurrentField());
                //neu weil die die geblockt sind weg
                if(game.getPlayingBoard().nrStepsToNextStartFieldBlock(m.getCurrentField()) == 1){
                    marbleToDelete.add(m);

                }
            }
        }
        log.info("COUNTREMAINSVEN" + String.valueOf(countToRemainSeven) + "DINSTANCEALL " + String.valueOf(countPossibleStepsAllMarbles));
        //Case player can make move only with his marbles
        if (countPossibleStepsAllMarbles >= countToRemainSeven) {
            possibleMarbles = stillMovable;
            for (Marble marbleTodel : marbleToDelete) {
                if (possibleMarbles.contains(marbleTodel)){
                    possibleMarbles.remove(marbleTodel);
                    log.info("Mabrle to del:" + String.valueOf(marbleTodel.getMarbleId())+String.valueOf(marbleTodel.getColor()));
                }
            }
        }

        for(Marble m: possibleMarbles){
            log.info("HERE" + player.getPlayerName()+ String.valueOf(m.getMarbleId())+String.valueOf(m.getColor()));
        }
        return possibleMarbles;
    }
    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Game game, ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKey) {
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeysEating = new ArrayList<>();
        //find out nr marbles in array list and minus getRemaining seven
        if(game.getGameService().getRemainingSevenMoves(game, marbleIdAndTargetFieldKey) != 0){
            return marbleIdAndTargetFieldKeys;
        }
        for(MarbleIdAndTargetFieldKey marbleIdandTargetKey : marbleIdAndTargetFieldKey) {
            Marble marble = game.getGameService().getMarbleByMarbleIdForSeven(game, marbleIdandTargetKey.getMarbleId());
            Field targetField = game.getPlayingBoard().getFieldByFieldKey(marbleIdandTargetKey.getFieldKey());
            Field startField = marble.getCurrentField();
            marbleIdAndTargetFieldKeysEating.addAll(eatSeven(targetField, startField, game));
            MarbleIdAndTargetFieldKey result = new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetField.getFieldKey());
            marbleIdAndTargetFieldKeys.add(result);
            marbleIdAndTargetFieldKeys.addAll(marbleIdAndTargetFieldKeysEating);
            game.getPlayingBoard().makeMove(targetField, marble);
        }
        return marbleIdAndTargetFieldKeys;
    }

    private ArrayList<MarbleIdAndTargetFieldKey> eatSeven(Field targetField, Field startField, Game game){
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        MarbleIdAndTargetFieldKey result = null;
        int fieldValCurrentField = startField.getFieldValue();
        Color colorCurrentField = startField.getColor();
        int distanceFields = game.getGameService().getDistanceBetweenFields(startField,targetField);
        for(int i = 1; i <= distanceFields; i++){
            int fieldValToCheck = fieldValCurrentField + i;
            Color colorFieldToCheck = colorCurrentField;
            if(fieldValToCheck > 16){
                fieldValToCheck = fieldValToCheck - 16;
                colorFieldToCheck = DogUtils.getNextColor(colorCurrentField);
            }
            Field fieldToCheck = game.getPlayingBoard().getField(fieldValToCheck, colorFieldToCheck);
            result = game.getGameService().eat(fieldToCheck, game);
            if(!(result == null)){
                marbleIdAndTargetFieldKeys.add(result);
            }
        }
        return marbleIdAndTargetFieldKeys;
    }

}