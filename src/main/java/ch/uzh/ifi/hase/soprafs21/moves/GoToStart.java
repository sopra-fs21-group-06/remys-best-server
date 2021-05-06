package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleAndTargetField;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;
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
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(List<MarbleAndTargetField> marblesAndTargetFields, Game game) {
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();

        for(MarbleAndTargetField marbleAndTargetField : marblesAndTargetFields) {
            Marble marble = marbleAndTargetField.getMarble();
            String targetFieldKey = game.getPlayingBoard().marbleGoesToStart(marble.getColor());
            marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleNr(), targetFieldKey))
        }

        return marbleIdAndTargetFieldKeys;
    }
}
