package ch.uzh.ifi.hase.soprafs21.moves;

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
        Field targetField = (Field) game.getPlayingBoard().getField(16, game.getCurrentRound().getCurrentPlayer().getColor());
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        possibleTargetFieldKeys.add(targetField.getFieldKey());
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
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Marble marbleToMove, Field targetField, Game game) {
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        game.getPlayingBoard().makeStartMove(marbleToMove.getColor());
        log.info("marble start successful");
        MarbleIdAndTargetFieldKey result = game.getGameService().eat(targetField,game);
        if(!(result == null)){
            marbleIdAndTargetFieldKeys.add(result);
        }
        result = new MarbleIdAndTargetFieldKey(marbleToMove.getMarbleNr(), targetField.getFieldKey());
        marbleIdAndTargetFieldKeys.add(result);
        return marbleIdAndTargetFieldKeys;
    }
}
