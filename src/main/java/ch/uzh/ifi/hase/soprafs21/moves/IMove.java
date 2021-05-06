package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleAndTargetField;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface IMove {
    String getName();

    List<Marble> getPlayableMarbles(Game game);

    List<String> getPossibleTargetFields(Game game); // TODO seven needs to track already selected moves List<MarbleAndTargetField>??

    ArrayList<MarbleIdAndTargetFieldKey> executeMove(List<MarbleAndTargetField> marblesAndTargetFields, Game game);
}
