package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;
import ch.uzh.ifi.hase.soprafs21.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class ElevenForwards extends AbstractForwards {
    private static final int NUMBER_TO_GO_FORWARDS = 11;

    @Override
    public String getName() {
        return super.getName(String.valueOf(NUMBER_TO_GO_FORWARDS));
    }

    @Override
    public List<Marble> getPlayableMarbles(Game game, GameService gameService) {
        return super.getPlayableMarbles(game, gameService, NUMBER_TO_GO_FORWARDS);
    }

    @Override
    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove) {
        return super.getPossibleTargetFields(game, marbleToMove, NUMBER_TO_GO_FORWARDS);
    }
    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove (Game game, ArrayList < MarbleIdAndTargetFieldKey > marbleIdAndTargetFieldKeyArrayList){
        return super.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }

}
