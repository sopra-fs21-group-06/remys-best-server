package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleAndTargetField;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;

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
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(List<MarbleAndTargetField> marblesAndTargetFields, Game game) {
        return null;
    }

}
