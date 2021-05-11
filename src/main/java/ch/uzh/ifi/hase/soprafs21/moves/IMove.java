package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.GameService;

import java.util.ArrayList;
import java.util.List;

public interface IMove {
    String getName();

    List<Marble> getPlayableMarbles(Game game, GameService gameService, int remainSeven);

    List<String> getPossibleTargetFields(Game game, Marble marbleToMove, int remainSeven); // TODO seven needs to track already selected moves List<MarbleAndTargetField>??

    ArrayList<MarbleIdAndTargetFieldKey> executeMove(Game game, ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList);

}
