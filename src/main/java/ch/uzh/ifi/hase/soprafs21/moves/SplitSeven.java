package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;

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
        log.info("remains seven:" + String.valueOf(countToRemainSeven));
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        Field marbleCurrentField = marbleToMove.getCurrentField();
        for (MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey : sevenMoves) {
            Marble marble = game.getGameService().getMarbleByMarbleId(game, marbleIdAndTargetFieldKey.getMarbleId());
            if (marbleIdAndTargetFieldKey.getMarbleId() == marbleToMove.getMarbleId() && countToRemainSeven!= 7){
                marbleCurrentField = game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKey.getFieldKey());
            }
            if (marble.getCurrentField().getFieldStatus().equals(FieldStatus.BLOCKED)){
                game.getPlayingBoard().getRightColorStartField(marble.getColor()).setFieldStatus(FieldStatus.FREE);
            }
        }

        if (!(marbleCurrentField instanceof FinishField)){
            int fieldValCurrentField = marbleCurrentField.getFieldValue();
            Color colorCurrentField = marbleCurrentField.getColor();
            for(int i = 1; i <= countToRemainSeven; i++){
                int fieldValToCheck = fieldValCurrentField + i;
                Color colorFieldToCheck = colorCurrentField;
                if(fieldValToCheck > 16){
                    fieldValToCheck = fieldValToCheck - 16;
                    colorFieldToCheck = game.getPlayingBoard().getNextColor(colorCurrentField);
                }
                Field fieldToCheck = game.getPlayingBoard().getField(fieldValToCheck, colorFieldToCheck);
                if(!fieldToCheck.getFieldStatus().equals(FieldStatus.BLOCKED)){
                    possibleTargetFieldKeys.add(fieldToCheck.getFieldKey());
                } else {
                    break;
                }
            }
        }
        if(marbleCurrentField instanceof FinishField || game.getPlayingBoard().marbleCanGetToFinishSpot(marbleToMove, marbleCurrentField, countToRemainSeven)){
            List<Field> finishFields= game.getPlayingBoard().getFinishFields(marbleToMove.getColor());
            boolean condition = TRUE;
            int countSteps = 1;
            if(marbleCurrentField.getFieldValue() < 17){
                countSteps = 16 - marbleCurrentField.getFieldValue();
            }
            for (Field f: finishFields){
                if(f.getFieldStatus().equals(FieldStatus.BLOCKED)){
                    condition = FALSE;
                }
                if (condition && countSteps < countToRemainSeven){
                    possibleTargetFieldKeys.add(f.getFieldKey());
                    countSteps++;
                } else {
                    break;
                }
            }
        }
        game.getPlayingBoard().getRightColorStartField(marbleToMove.getColor()).setFieldStatus(FieldStatus.BLOCKED);
        return possibleTargetFieldKeys;
    }

    @Override
    public List<Marble> getPlayableMarbles(Game game, GameService gameService, ArrayList<MarbleIdAndTargetFieldKey> sevenMoves) {
        int countToRemainSeven = game.getGameService().getRemainingSevenMoves(game, sevenMoves);
        List<Marble> possibleMarbles = new ArrayList<>();
        Player p = game.getCurrentRound().getCurrentPlayer();
        int countPossibleStepsAllMarbles = 0;
        for (Marble m : p.getMarblesOnFieldAndNotFinished()) {
            if (m.getCurrentField() instanceof FinishField) {
                countPossibleStepsAllMarbles += game.getPlayingBoard().nrStepsToNextFreeFinishSpot(m.getCurrentField());
            }
            else {
                countPossibleStepsAllMarbles += game.getPlayingBoard().nrStepsToNextStartFieldBlock(m.getCurrentField());
            }
        }
        if (countPossibleStepsAllMarbles >= countToRemainSeven) {
            possibleMarbles = p.getMarblesOnFieldAndNotFinished();
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
            Marble marble = game.getGameService().getMarbleByMarbleId(game, marbleIdandTargetKey.getMarbleId());
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
                colorFieldToCheck = game.getPlayingBoard().getNextColor(colorCurrentField);
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