package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.*;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class GoToStart implements IMove {

    @Override
    public String getName() {
        return "Go To Start";
    }

    @Override
    public boolean isMoveExecutable() {
        return false;
    }

    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Marble marbleToMove, Field targetField, Game game) {
        /*
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();

        for(MarbleAndTargetField marbleAndTargetField : marblesAndTargetFields) {
            Marble marble = marbleAndTargetField.getMarble();
            String targetFieldKey = game.getPlayingBoard().marbleGoesToStart(marble.getColor());
            marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleNr(), targetFieldKey))
        }

        return marbleIdAndTargetFieldKeys;*/
    }
}
