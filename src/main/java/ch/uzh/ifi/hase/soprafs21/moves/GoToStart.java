package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GoToStart implements IMove {
    Logger log = LoggerFactory.getLogger(GoToStart.class);

    @Override
    public String getName() {
        return "Go To Start";
    }


    @Override
    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove) {
        Field targetField = game.getPlayingBoard().getField(16, game.getCurrentRound().getCurrentPlayer().getColor());
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        if (!targetField.getFieldStatus().equals(FieldStatus.BLOCKED)) {
            possibleTargetFieldKeys.add(targetField.getFieldKey());
        }
        return possibleTargetFieldKeys;
    }

    @Override
    public List<Marble> getPlayableMarbles(Game game, GameService gameService) {
        List<Marble> possibleMarbles = new ArrayList<>();
        Boolean b = game.getPlayingBoard().hasMarbleOnHomeStack(game.getCurrentRound().getCurrentPlayer().getColor());
        if(!b){
            return possibleMarbles;
        }
        Marble m = game.getPlayingBoard().getFirstHomeMarble(game.getCurrentRound().getCurrentPlayer().getColor(), false);
        possibleMarbles.add(m);
        return possibleMarbles;
    }

    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Game game, MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey) {
        Field targetField = game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKey.getFieldKey());
        Marble marbleToMove = targetField.getMarble();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        MarbleIdAndTargetFieldKey result = game.getGameService().eat(targetField,game);
        if(!(result == null)){
            marbleIdAndTargetFieldKeys.add(result);
        }
        game.getPlayingBoard().makeStartMove(marbleToMove.getColor());
        log.info("marble start successful");
        result = new MarbleIdAndTargetFieldKey(marbleToMove.getMarbleNr(), targetField.getFieldKey());
        marbleIdAndTargetFieldKeys.add(result);
        return marbleIdAndTargetFieldKeys;
    }
}
