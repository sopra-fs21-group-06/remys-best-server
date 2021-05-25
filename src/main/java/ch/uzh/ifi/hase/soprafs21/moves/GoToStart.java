package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GoToStart implements INormalMove {
    Logger log = LoggerFactory.getLogger(GoToStart.class);

    @Override
    public String getName() {
        return "Go To Start";
    }


    @Override
    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove) {
        Field targetField = game.getPlayingBoard().getField(16,marbleToMove.getColor());
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        possibleTargetFieldKeys.add(targetField.getFieldKey());
        return possibleTargetFieldKeys;
    }

    @Override
    public List<Marble> getPlayableMarbles(Game game, GameService gameService) {
        List<Marble> possibleMarbles = new ArrayList<>();
        Color colorToLookAt = game.getCurrentRound().getCurrentPlayer().getColor();
        if(game.getCurrentRound().getCurrentPlayer().isFinished()){
            colorToLookAt =game.getCurrentRound().getCurrentPlayer().getTeamMate().getColor();
        }
        Boolean hasMarblesOnHomeField = game.getPlayingBoard().hasMarbleOnHomeStack(colorToLookAt);
        Boolean startingFieldIsBocked = game.getPlayingBoard().getNextStartFieldIsBlocked(colorToLookAt);
        if(!hasMarblesOnHomeField){
            log.info("No marbles at home at getPlayableMarbles start");
            return possibleMarbles;
        }
        if(startingFieldIsBocked){
            log.info("Startfield is blocked at getPlayableMarbles start");
            return possibleMarbles;
        }

        Marble m = game.getPlayingBoard().getFirstHomeMarble(colorToLookAt, false);
        possibleMarbles.add(m);
        return possibleMarbles;
    }

    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Game game, ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList) {
        Field targetField =game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKeyArrayList.get(0).getFieldKey());
        Marble marbleToMove = null;
        for(Marble m: game.getCurrentRound().getCurrentPlayer().getMarbleList()){
            if(m.getMarbleId() == marbleIdAndTargetFieldKeyArrayList.get(0).getMarbleId()){
                marbleToMove = m;
            }
        }
        assert marbleToMove != null;
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        MarbleIdAndTargetFieldKey resultEating = game.getGameService().eat(targetField,game);

        marbleToMove = game.getPlayingBoard().makeStartMove(marbleToMove.getColor());
        log.info("marble start successful");
        MarbleIdAndTargetFieldKey result = new MarbleIdAndTargetFieldKey(marbleToMove.getMarbleId(), targetField.getFieldKey());
        marbleIdAndTargetFieldKeys.add(result);
        if(!(resultEating == null)){
            marbleIdAndTargetFieldKeys.add(resultEating);
        }
        return marbleIdAndTargetFieldKeys;
    }
}
