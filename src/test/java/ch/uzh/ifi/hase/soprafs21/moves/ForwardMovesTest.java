package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ForwardMovesTest extends AbstractMoveTest {

    @Test
    public void testGoForwardsFromHomeBase() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        // two forwards
        INormalMove twoForwards = new TwoForwards();
        String targetFieldKey = "2GREEN";

        List<Marble> playableMarbles = twoForwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = twoForwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = twoForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);

        // eleven forwards
        INormalMove elevenForwards = new ElevenForwards();
        targetFieldKey = "13GREEN";

        playableMarbles = elevenForwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        possibleTargetFields = elevenForwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        executedMoves = elevenForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }
}
