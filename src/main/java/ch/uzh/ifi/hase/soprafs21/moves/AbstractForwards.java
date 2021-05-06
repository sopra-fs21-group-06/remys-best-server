package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.Field;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractForwards implements IMove {

    public String getName(String number) {
        return number + " Forwards";
    }

    public List<Marble> getPlayableMarbles(Game game, int numberToGoForwards) {
        // TODO implement
    }

    public List<String> getPossibleTargetFields(Game game, int numberToGoForwards) {
        // TODO impoement
    }

    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Marble marbleToMove, Field targetField, Game game, int numberToGoForwards) {
        // TODO implement with generic number
        return null;
    }
}
