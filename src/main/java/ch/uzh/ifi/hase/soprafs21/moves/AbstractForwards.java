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

public abstract class AbstractForwards implements INormalMove {
    Logger log = LoggerFactory.getLogger(AbstractForwards.class);

    public String getName(String number) {
        return number + " Forwards";
    }

    public List<Marble> getPlayableMarbles(Game game, GameService gameService, int numberToGoForwards) {
        List<Marble> possibleMarbles = new ArrayList<>();
        Player p = game.getCurrentRound().getCurrentPlayer();
        for (Marble m : p.getMarbleList()) {
            if (!m.getHome()) {
                if (gameService.checkMoveForward(m, numberToGoForwards, game)) {
                    possibleMarbles.add(m);
                }
            }
        }
        return possibleMarbles;
    }

    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove, int numberToGoForwards) {
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        int distanceNextStartField = 16 - marbleToMove.getCurrentField().getFieldValue();
        int valueFieldNew;
        Color colorFieldCurrentField = marbleToMove.getCurrentField().getColor();
        Color colorNextField;
        Field targetField;
        int stepsNextFreeSpot = 16 - marbleToMove.getCurrentField().getFieldValue() +game.getPlayingBoard().nrStepsToNextFreeFinishSpot(marbleToMove.getCurrentField());
        // check if possibleEndfield is on next Part of Game -> CHange color and Value
        // Case 2 fields into home and not
        if (stepsNextFreeSpot >= numberToGoForwards && marbleToMove.getCurrentField().getFieldValue() + numberToGoForwards < 21 && marbleToMove.getCurrentField().getFieldValue() + numberToGoForwards > 16 && marbleToMove.getCurrentField().getColor().equals(marbleToMove.getColor()) && !marbleToMove.getCurrentField().getFieldStatus().equals(FieldStatus.BLOCKED)){

            int valueFieldNew1 = numberToGoForwards - distanceNextStartField;
            int valueFieldNew2 = marbleToMove.getCurrentField().getFieldValue() + numberToGoForwards;
            Color cFieldNew1 = game.getPlayingBoard().getNextColor(colorFieldCurrentField);
            Color cFieldNew2 = marbleToMove.getColor();
            Field targetField1 = game.getPlayingBoard().getField(valueFieldNew1, cFieldNew1);
            possibleTargetFieldKeys.add(targetField1.getFieldKey());
            Field targetField2 = game.getPlayingBoard().getField(valueFieldNew2, cFieldNew2);
            possibleTargetFieldKeys.add(targetField2.getFieldKey());
        } else if ( marbleToMove.getCurrentField().getFieldValue() + numberToGoForwards > 16){
            colorNextField= game.getPlayingBoard().getNextColor(colorFieldCurrentField);
            valueFieldNew = numberToGoForwards - distanceNextStartField;
            targetField = game.getPlayingBoard().getField(valueFieldNew, colorNextField);
            possibleTargetFieldKeys.add(targetField.getFieldKey());
        } else {
            valueFieldNew = marbleToMove.getCurrentField().getFieldValue() + numberToGoForwards;
            colorNextField = colorFieldCurrentField;
            targetField = game.getPlayingBoard().getField(valueFieldNew, colorNextField);
            possibleTargetFieldKeys.add(targetField.getFieldKey());
        }
        return possibleTargetFieldKeys;
    }


    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Game game, ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList) {
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();

        Field targetField = game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKeyArrayList.get(0).getFieldKey());
        Marble marbleToMove = null;
        for(Marble m: game.getCurrentRound().getCurrentPlayer().getMarbleList()){
            if(m.getMarbleNr() == marbleIdAndTargetFieldKeyArrayList.get(0).getMarbleId()){
                marbleToMove = m;
            }
        }

        MarbleIdAndTargetFieldKey result = game.getGameService().eat(targetField,game);
        if(!(result == null)){
            marbleIdAndTargetFieldKeys.add(result);
        }

        game.getPlayingBoard().makeMove(targetField, marbleToMove);
        log.info("marble forward successful");
        result = new MarbleIdAndTargetFieldKey(marbleToMove.getMarbleNr(), targetField.getFieldKey());
        marbleIdAndTargetFieldKeys.add(result);
        return marbleIdAndTargetFieldKeys;
    }
}
