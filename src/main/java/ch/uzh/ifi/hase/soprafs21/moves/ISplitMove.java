package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;
import ch.uzh.ifi.hase.soprafs21.service.GameService;

import java.util.ArrayList;
import java.util.List;

public interface ISplitMove extends IMove {

    List<Marble> getPlayableMarbles(Game game, GameService gameService, ArrayList<MarbleIdAndTargetFieldKey> sevenMoves);

    List<String> getPossibleTargetFields(Game game, Marble marbleToMove, ArrayList<MarbleIdAndTargetFieldKey> sevenMoves);
}
