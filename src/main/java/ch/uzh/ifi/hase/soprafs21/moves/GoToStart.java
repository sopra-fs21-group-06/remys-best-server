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
<<<<<<< Updated upstream
    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove) {
        Field targetField = game.getPlayingBoard().getField(16, game.getCurrentRound().getCurrentPlayer().getColor());
        List<String> possibleTargetFieldKeys = new ArrayList<>();
=======
    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove, int remainSeven) {
        Field targetField = game.getPlayingBoard().getField(16, game.getCurrentRound().getCurrentPlayer().getColor());
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        possibleTargetFieldKeys.add(targetField.getFieldKey());
>>>>>>> Stashed changes
        if (!targetField.getFieldStatus().equals(FieldStatus.BLOCKED)) {
            possibleTargetFieldKeys.add(targetField.getFieldKey());
        }
        return possibleTargetFieldKeys;
    }

    @Override
    public List<Marble> getPlayableMarbles(Game game, GameService gameService, int remainSeven) {
        List<Marble> possibleMarbles = new ArrayList<>();
        Boolean hasMarblesOnHomeField = game.getPlayingBoard().hasMarbleOnHomeStack(game.getCurrentRound().getCurrentPlayer().getColor());
        Boolean startingFieldIsBocked = game.getPlayingBoard().getNextStartFieldIsBlocked(game.getCurrentRound().getCurrentPlayer().getColor());
        if(!hasMarblesOnHomeField){
            log.info("No marbles at home at getPlayableMarbles start");
            return possibleMarbles;
        }
        if(startingFieldIsBocked){
            log.info("Startfield is blocked at getPlayableMarbles start");
            return possibleMarbles;
        }

        Marble m = game.getPlayingBoard().getFirstHomeMarble(game.getCurrentRound().getCurrentPlayer().getColor(), false);
        possibleMarbles.add(m);
        return possibleMarbles;
    }

    @Override
<<<<<<< Updated upstream
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Game game, MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey) {
        Field targetField = game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKey.getFieldKey());
        Marble marbleToMove = targetField.getMarble();
=======
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Game game, ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList) {
        Field targetField =game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKeyArrayList.get(0).getFieldKey());
        Marble marbleToMove = null;
        for(Marble m: game.getCurrentRound().getCurrentPlayer().getMarbleList()){
            if(m.getMarbleNr() == marbleIdAndTargetFieldKeyArrayList.get(0).getMarbleId()){
                marbleToMove = m;
            }
        }
>>>>>>> Stashed changes
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
