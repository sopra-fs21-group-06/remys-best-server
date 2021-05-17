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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BackwardsMoveTest extends AbstractMoveTest {

    @Test
    public void testFourBackwardsFromHomeBase() {
        Game game = setupGame();
        Marble marble = goToStart(game);
        String targetFieldKey = "12BLUE";

        INormalMove fourBackwards = new FourBackwards();
        List<Marble> playableMarbles = fourBackwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = fourBackwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = fourBackwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }

    @Test
    public void testFourBackwardsFromArbitraryField() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        // From field 8 four backwards
        goEightForwards(game, marble, "8GREEN");
        String targetFieldKey = "4GREEN";

        INormalMove fourBackwards = new FourBackwards();
        List<Marble> playableMarbles = fourBackwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = fourBackwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = fourBackwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);

        // From field 14 four backwards
        goTenForwards(game, marble, "14GREEN");
        targetFieldKey = "10GREEN";

        playableMarbles = fourBackwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        possibleTargetFields = fourBackwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        executedMoves = fourBackwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }

    @Test
    public void testFourBackwardsViaHomeBase() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        // From field 2 four backwards
        goTwoForwards(game, marble, "2GREEN");
        String targetFieldKey = "14BLUE";

        INormalMove fourBackwards = new FourBackwards();
        List<Marble> playableMarbles = fourBackwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = fourBackwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = fourBackwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }
}
