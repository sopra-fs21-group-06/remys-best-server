package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.service.GameService;

import java.util.List;

public interface INormalMove extends IMove {

    List<Marble> getPlayableMarbles(Game game, GameService gameService);

    List<String> getPossibleTargetFields(Game game, Marble marbleToMove);
}
