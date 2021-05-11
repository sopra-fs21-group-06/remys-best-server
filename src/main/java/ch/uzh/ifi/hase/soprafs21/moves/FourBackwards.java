package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FourBackwards implements IMove {
    private static final int NUMBER_TO_GO_FORWARDS = -4;
    Logger log = LoggerFactory.getLogger(FourBackwards.class);

    @Override
    public String getName() {
        return "4 Backwards";
    }

    @Override
    public List<Marble> getPlayableMarbles(Game game, GameService gameService) {
        List<Marble> possibleMarbles = new ArrayList<>();
        Player p = game.getCurrentRound().getCurrentPlayer();
        for (Marble m : p.getMarbleList()) {
            if (!m.getHome()) {
                if (gameService.checkMoveBackward(m, NUMBER_TO_GO_FORWARDS, game)) {
                    possibleMarbles.add(m);
                }
            }
        }
        return possibleMarbles;
    }

    @Override
    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove) {
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        int fieldNr = marbleToMove.getCurrentField().getFieldValue() - 4;
        Color colorEndField = marbleToMove.getCurrentField().getColor();
        if (fieldNr < 1){
            fieldNr = 16 + fieldNr;
            colorEndField = game.getPlayingBoard().getPreviousColor(marbleToMove.getColor());
        }
        Field possTargetField = game.getPlayingBoard().getField(fieldNr, colorEndField);
        possibleTargetFieldKeys.add(possTargetField.getFieldKey());
        return possibleTargetFieldKeys;
    }

    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Game game, MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey) {
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        Field targetField = game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKey.getFieldKey());
        Marble marbleToMove = targetField.getMarble();
        MarbleIdAndTargetFieldKey result = game.getGameService().eat(targetField,game);
        if(!(result == null)){
            marbleIdAndTargetFieldKeys.add(result);
        }
        game.getPlayingBoard().makeMove(targetField, marbleToMove);
        log.info("marble move forward/backward successful");
        result = new MarbleIdAndTargetFieldKey(marbleToMove.getMarbleNr(), targetField.getFieldKey());
        marbleIdAndTargetFieldKeys.add(result);
        return marbleIdAndTargetFieldKeys;
    }
}



