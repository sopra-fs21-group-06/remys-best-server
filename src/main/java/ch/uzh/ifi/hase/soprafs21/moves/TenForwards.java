package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class TenForwards extends AbstractForwards {
    private static final int NUMBER_TO_GO_FORWARDS = 10;

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
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Marble marbleToMove, Field targetField, Game game) {
        return super.executeMove(marbleToMove, targetField, game);
    }
}