package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractForwards implements IMove {
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
        int moveToInt = numberToGoForwards;
        int distanceNextStartField = 16 - marbleToMove.getCurrentField().getFieldValue();
        int valueFieldNew = 0;
        Color colorFieldCurrentField = marbleToMove.getCurrentField().getColor();
        Color colorNextField = null;
        Field targetField = null;
        // check if possibleEndfield is on next Part of Game -> CHange color and Value
        // Case 2 fields into home and not
        if (marbleToMove.getCurrentField().getFieldValue() + moveToInt < 21 && marbleToMove.getCurrentField().getFieldValue() + moveToInt > 16 && marbleToMove.getCurrentField().getColor().equals(marbleToMove.getColor()) && !marbleToMove.getCurrentField().getFieldStatus().equals(FieldStatus.BLOCKED)){
            int valueFieldNew1 = moveToInt - distanceNextStartField;
            int valueFieldNew2 = marbleToMove.getCurrentField().getFieldValue() + moveToInt;
            Color cFieldNew1 = game.getPlayingBoard().getNextColor(colorFieldCurrentField);
            Color cFieldNew2 = marbleToMove.getColor();
            Field targetField1 = game.getPlayingBoard().getField(valueFieldNew1, cFieldNew1);
            possibleTargetFieldKeys.add(targetField1.getFieldKey());
            Field targetField2 = game.getPlayingBoard().getField(valueFieldNew2, cFieldNew2);
            possibleTargetFieldKeys.add(targetField2.getFieldKey());
        } else if ( marbleToMove.getCurrentField().getFieldValue() + moveToInt > 16){
            colorNextField= game.getPlayingBoard().getNextColor(colorFieldCurrentField);
            valueFieldNew = moveToInt - distanceNextStartField;
            targetField = game.getPlayingBoard().getField(valueFieldNew, colorNextField);
            possibleTargetFieldKeys.add(targetField.getFieldKey());
        } else {
            valueFieldNew = marbleToMove.getCurrentField().getFieldValue() + moveToInt;
            colorNextField = colorFieldCurrentField;
            targetField = game.getPlayingBoard().getField(valueFieldNew, colorNextField);
            possibleTargetFieldKeys.add(targetField.getFieldKey());
        }
        return possibleTargetFieldKeys;
    }

    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Marble marbleToMove, Field targetField, Game game) {
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        MarbleIdAndTargetFieldKey result = null;
        game.getPlayingBoard().makeMove(targetField, marbleToMove);
        log.info("marble forward successful");
        if(!(targetField instanceof FinishField)){
                //game.getGameService().eat(targetField,game);
        }
        result = new MarbleIdAndTargetFieldKey(marbleToMove.getMarbleNr(), targetField.getFieldKey());
        marbleIdAndTargetFieldKeys.add(result);
        return marbleIdAndTargetFieldKeys;
    }
}
