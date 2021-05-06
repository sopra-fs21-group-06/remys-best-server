package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.*;

import java.util.ArrayList;
import java.util.List;

public class FourBackwards implements IMove {

    @Override
    public String getName() {
        return "4 Backwards";
    }

    @Override
    public boolean isMoveExecutable() {
        return false;
    }

    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Marble marbleToMove, Field targetField, Game game) {
        return null;
    }

}
