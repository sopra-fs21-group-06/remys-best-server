package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;

import java.util.ArrayList;

public class TenForwards extends AbstractForwards {

    @Override
    public String getName() {
        return super.getName("10");
    }

    @Override
    public boolean isMoveExecutable() {
        return false;
    }

    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove() {
        return null;
    }


}
