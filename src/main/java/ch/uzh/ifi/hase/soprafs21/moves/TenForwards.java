package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.*;

import java.util.ArrayList;
import java.util.List;

public class TenForwards extends AbstractForwards {

    private static final int NUMBER_TO_GO_FORWARDS = 10;

    @Override
    public String getName() {
        return super.getName(String.valueOf(NUMBER_TO_GO_FORWARDS));
    }

    @Override
    public boolean isMoveExecutable() {
        return false;
    }

    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Marble marbleToMove, Field targetField, Game game) {
        return super.executeMove(Marble marbleToMove, Field targetField, Game game, NUMBER_TO_GO_FORWARDS);
    }
}
