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
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        Field marbleCurrentField = marbleToMove.getCurrentField();
        Field startField = game.getPlayingBoard().getRightColorStartField(marbleToMove.getColor());
        for (MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey : sevenMoves) {
            if (marbleIdAndTargetFieldKey.getMarbleId() == marbleToMove.getMarbleNr() && countToRemainSeven!= 7) {
                marbleCurrentField = game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKey.getFieldKey());
            }
            if (!startField.getFieldKey().equals(marbleIdAndTargetFieldKey.getFieldKey()) && startField.getFieldStatus().equals(FieldStatus.BLOCKED) && marbleIdAndTargetFieldKey.getMarbleId() == startField.getMarble().getMarbleNr() && countToRemainSeven != 7) {
                game.getPlayingBoard().getRightColorStartField(marbleToMove.getColor()).setFieldStatus(FieldStatus.FREE);
            }
        }
        Boolean conditionMarbleInFinishSector = marbleCurrentField instanceof FinishField;
        Boolean conditionMarbleIsOnStartSecondTime = marbleCurrentField instanceof StartField &&  !marbleCurrentField.equals(FieldStatus.BLOCKED);
        Boolean conditionMarbleCanMakeItIntoFinish = marbleCurrentField.getColor().equals(marbleToMove.getColor()) && 16 - marbleCurrentField.getFieldValue() < countToRemainSeven && 16 - marbleCurrentField.getFieldValue() > 0 ;
        //Add all finishsector field
        if(conditionMarbleCanMakeItIntoFinish || conditionMarbleIsOnStartSecondTime || conditionMarbleInFinishSector){
            // count to remain seven changes into the remaining size of steps after going to start
            int remainSeven = countToRemainSeven;
            if (conditionMarbleCanMakeItIntoFinish){
                remainSeven = countToRemainSeven - 16 + marbleCurrentField.getFieldValue();
            }
            List<Field> finishFields= game.getPlayingBoard().getFinishFields(marbleToMove.getColor());
            int count = 0;
            for(Field f: finishFields){
                // field is further away
                if(marbleCurrentField.getFieldValue() < f.getFieldValue()){
                    //case if next finishfield is blocked
                    if(f.getFieldStatus().equals(FieldStatus.OCCUPIED)){
                        count = remainSeven + 1;
                    } else {
                        // if remain seven is bigger than count add field key else there are still free spots but no more seven to move
                        if(count < remainSeven){
                            possibleTargetFieldKeys.add(f.getFieldKey());
                            count++;
                        }
                    }
                }
        }
        List<Field> playingFields = game.getPlayingBoard().getListPlayingFields();
        // case of marbles outside finishsector, countrestseven zählt wieviel noch
        int countRestSeven = countToRemainSeven;
        Boolean fieldIsFound = FALSE;
        if(!(marbleToMove.getCurrentField() instanceof FinishField))
            for(Field f: playingFields){
                if(f.getFieldKey().equals(marbleToMove.getCurrentField().getFieldKey())){
                    fieldIsFound = TRUE;
                }
                if (fieldIsFound) {
                    // wenn ganz am schluss vom playingfield
                    if (f instanceof StartField && f.getColor().equals(Color.YELLOW)) {
                        for (Field field : playingFields) {
                            if (field.getFieldStatus().equals(FieldStatus.BLOCKED) && !field.getFieldKey().equals(marbleCurrentField.getFieldKey())) {
                                countRestSeven = 0;
                            }
                            if (!field.getFieldKey().equals(marbleCurrentField.getFieldKey()) && countRestSeven > 0)
                                possibleTargetFieldKeys.add(field.getFieldKey());
                            countRestSeven--;

                        }
                    }
                    else {
                        if (f.getFieldStatus().equals(FieldStatus.BLOCKED) && !f.getFieldKey().equals(marbleCurrentField.getFieldKey())) {
                            countRestSeven = 0;
                        }
                        if (countRestSeven > 0 && !f.getFieldKey().equals(marbleCurrentField.getFieldKey())) {
                            possibleTargetFieldKeys.add(f.getFieldKey());
                            countRestSeven--;
                        }
                    }
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
        if(game.getGameService().getRemainingSevenMoves(game, marbleIdAndTargetFieldKey) != 0){
            //more or less than 7 moves
            return marbleIdAndTargetFieldKeys;
        }
        for(MarbleIdAndTargetFieldKey marbleIdandTargetKey : marbleIdAndTargetFieldKey) {
            Marble marble = game.getGameService().getMarbleByMarbleId(game, marbleIdandTargetKey.getMarbleId());
            Field targetField = game.getPlayingBoard().getFieldByFieldKey(marbleIdandTargetKey.getFieldKey());
            Field startField = marble.getCurrentField();
            marbleIdAndTargetFieldKeys.addAll(eatSeven(targetField, startField, game));
            MarbleIdAndTargetFieldKey result = new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetField.getFieldKey());
            marbleIdAndTargetFieldKeys.add(result);
            game.getPlayingBoard().makeMove(targetField, marble);
        }
        return marbleIdAndTargetFieldKeys;
    }

    private ArrayList<MarbleIdAndTargetFieldKey> eatSeven(Field targetField, Field startField, Game game){
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        MarbleIdAndTargetFieldKey result = null;
        Boolean fieldIsFound = FALSE;
        for(Field f: game.getPlayingBoard().getListPlayingFields()){
            if(fieldIsFound){
                // wenn ganz am schluss vom playingfield
                if(f instanceof StartField && f.getColor().equals(Color.YELLOW)){
                    for(Field field: game.getPlayingBoard().getListPlayingFields()){
                        if(field.getFieldKey().equals(targetField.getFieldKey())){
                            result = game.getGameService().eat(targetField, game);
                            if(!(result == null)){
                                marbleIdAndTargetFieldKeys.add(result);
                            }
                            return marbleIdAndTargetFieldKeys;
                        } else {
                            result = game.getGameService().eat(field,game);
                            if(!(result == null)){
                                marbleIdAndTargetFieldKeys.add(result);
                            }
                        }
                    }
                } else {
                    if(f.getFieldKey().equals(targetField.getFieldKey())){
                        result = game.getGameService().eat(targetField, game);
                        if(!(result == null)){
                            marbleIdAndTargetFieldKeys.add(result);
                        }
                        return marbleIdAndTargetFieldKeys;
                    } else {
                        if(!(result == null)){
                            marbleIdAndTargetFieldKeys.add(result);
                        }
                    }
                }
            }
            if(f.getFieldKey().equals(startField.getFieldKey())){
                fieldIsFound = TRUE;
            }
        }
        return marbleIdAndTargetFieldKeys;
    }

}