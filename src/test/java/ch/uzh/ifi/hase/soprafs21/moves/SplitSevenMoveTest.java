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
public class SplitSevenMoveTest extends AbstractMoveTest {

    @Test
    public void testSplitSeven() {
        Game game = setupGame();
        Marble marble1 = goToStart(game);
        String targetFieldKey = "12BLUE";
        INormalMove fourBackwards = new FourBackwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble1.getMarbleId(), targetFieldKey));
        fourBackwards.executeMove(game, marbleIdAndTargetFieldKeys);
        Marble marble2 = goToStart(game);

        ArrayList<MarbleIdAndTargetFieldKey> sevenMoves = new  ArrayList<>();

        ISplitMove splitSeven = new SplitSeven();

        List<Marble> playableMarbles = splitSeven.getPlayableMarbles(game, game.getGameService(),sevenMoves);
        assertTrue(playableMarbles.contains(marble1));
        assertTrue(playableMarbles.contains(marble2));
        //possibleTargetFields Marble1 (is 4 back)
        String targetFieldKey1Marble1 = "13BLUE";
        String targetFieldKey2Marble1 = "14BLUE";
        String targetFieldKey3Marble1 = "15BLUE";
        List<String> possibleTargetFields1 = splitSeven.getPossibleTargetFields(game, marble1, sevenMoves);
        assertEquals(possibleTargetFields1.size(), 3);
        assertEquals(possibleTargetFields1.get(0), targetFieldKey1Marble1);
        assertEquals(possibleTargetFields1.get(1), targetFieldKey2Marble1);
        assertEquals(possibleTargetFields1.get(2), targetFieldKey3Marble1);
        //possibleTargetFields Marble 2 (On start)
        String targetFieldKey1 = "1GREEN";
        String targetFieldKey2 = "2GREEN";
        String targetFieldKey3 = "3GREEN";
        String targetFieldKey4 = "4GREEN";
        String targetFieldKey5 = "5GREEN";
        String targetFieldKey6 = "6GREEN";
        String targetFieldKey7 = "7GREEN";
        List<String> possibleTargetFields2 = splitSeven.getPossibleTargetFields(game, marble2, sevenMoves);
        assertEquals(possibleTargetFields2.size(), 7);
        assertEquals(possibleTargetFields2.get(0), targetFieldKey1);
        assertEquals(possibleTargetFields2.get(1), targetFieldKey2);
        assertEquals(possibleTargetFields2.get(2), targetFieldKey3);
        assertEquals(possibleTargetFields2.get(3), targetFieldKey4);
        assertEquals(possibleTargetFields2.get(4), targetFieldKey5);
        assertEquals(possibleTargetFields2.get(5), targetFieldKey6);
        assertEquals(possibleTargetFields2.get(6), targetFieldKey7);


        //Now Marble2 (on start) goes 1 step
        String targetFieldKeyStep1Marble2 = "1GREEN";
        sevenMoves.add(new MarbleIdAndTargetFieldKey(marble2.getMarbleId(), targetFieldKeyStep1Marble2));
        playableMarbles = splitSeven.getPlayableMarbles(game, game.getGameService(),sevenMoves);
        assertTrue(playableMarbles.contains(marble1));
        assertTrue(playableMarbles.contains(marble2));

        //possibleTargetFields Marble1 (is 4 back)
        targetFieldKey1Marble1 = "13BLUE";
        targetFieldKey2Marble1 = "14BLUE";
        targetFieldKey3Marble1 = "15BLUE";
        String targetFieldKey4Marble1 = "16BLUE";
        String targetFieldKey5Marble1 = "1GREEN";
        String targetFieldKey6Marble1 = "2GREEN";
        String targetFieldKey7Marble1 = "17BLUE";
        String targetFieldKey8Marble1 = "18BLUE";
        possibleTargetFields1 = splitSeven.getPossibleTargetFields(game, marble1, sevenMoves);
        assertEquals(possibleTargetFields1.size(), 8);

        assertEquals(possibleTargetFields1.get(0), targetFieldKey1Marble1);
        assertEquals(possibleTargetFields1.get(1), targetFieldKey2Marble1);
        assertEquals(possibleTargetFields1.get(2), targetFieldKey3Marble1);
        assertEquals(possibleTargetFields1.get(3), targetFieldKey4Marble1);
        assertEquals(possibleTargetFields1.get(4), targetFieldKey5Marble1);
        assertEquals(possibleTargetFields1.get(5), targetFieldKey6Marble1);
        assertEquals(possibleTargetFields1.get(6), targetFieldKey7Marble1);
        assertEquals(possibleTargetFields1.get(7), targetFieldKey8Marble1);

        //possibleTargetFields Marble 2 (On start)
        targetFieldKey1 = "2GREEN";
        targetFieldKey2 = "3GREEN";
        targetFieldKey3 = "4GREEN";
        targetFieldKey4 = "5GREEN";
        targetFieldKey5 = "6GREEN";
        targetFieldKey6 = "7GREEN";

        possibleTargetFields2 = splitSeven.getPossibleTargetFields(game, marble2, sevenMoves);
        assertEquals(possibleTargetFields2.size(), 6);
        assertEquals(possibleTargetFields2.get(0), targetFieldKey1);
        assertEquals(possibleTargetFields2.get(1), targetFieldKey2);
        assertEquals(possibleTargetFields2.get(2), targetFieldKey3);
        assertEquals(possibleTargetFields2.get(3), targetFieldKey4);
        assertEquals(possibleTargetFields2.get(4), targetFieldKey5);
        assertEquals(possibleTargetFields2.get(5), targetFieldKey6);
        // 1. step Marble 2 goes to 1GREEN
        //2. Marble2 (on start) goes to 2GREEN
        // Marble 1 (4Back) eats marble 2 -> 22BLUE (HOME)
        // Marble 1 goes to 2GREEN
        String targetFieldKeyStep2Marble2 = "2GREEN";
        sevenMoves.add(new MarbleIdAndTargetFieldKey(marble2.getMarbleId(), targetFieldKeyStep2Marble2));
        String targetFieldKeyStep2Marble1 = "1GREEN";
        sevenMoves.add(new MarbleIdAndTargetFieldKey(marble1.getMarbleId(), targetFieldKeyStep2Marble1));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = splitSeven.executeMove(game, sevenMoves);
        assertEquals(executedMoves.size(), 3);
        assertEquals(executedMoves.get(0).getMarbleId(), marble2.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), "1GREEN");
        assertEquals(executedMoves.get(1).getMarbleId(), marble2.getMarbleId());
        assertEquals(executedMoves.get(1).getFieldKey(), "2GREEN");
        assertEquals(executedMoves.get(2).getMarbleId(), marble1.getMarbleId());
        assertEquals(executedMoves.get(2).getFieldKey(), "1GREEN");
    }

    @Test
    public void testSevenEat_sameTargetField_marbleEaten() {
        //Set UP
        //Marble 1 goes one step to 1GREEN;
        //Marble2 goes to start

        Game game = setupGame();
        Marble marble1 = goToStart(game);
        String targetFieldKey = "1GREEN";
        INormalMove oneForwards = new OneForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble1.getMarbleId(), targetFieldKey));
        oneForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        Marble marble2 = goToStart(game);
        ArrayList<MarbleIdAndTargetFieldKey> sevenMoves = new  ArrayList<>();

        ISplitMove splitSeven = new SplitSeven();
        List<Marble> playableMarbles = splitSeven.getPlayableMarbles(game, game.getGameService(),sevenMoves);
        assertTrue(playableMarbles.contains(marble1));
        assertTrue(playableMarbles.contains(marble2));

        String targetFieldKey1Marble1 = "2GREEN";
        String targetFieldKey2Marble1 = "3GREEN";
        String targetFieldKey3Marble1 = "4GREEN";
        String targetFieldKey4Marble1 = "5GREEN";
        String targetFieldKey5Marble1 = "6GREEN";
        String targetFieldKey6Marble1 = "7GREEN";
        String targetFieldKey7Marble1 = "8GREEN";
        List<String> possibleTargetFields1 = splitSeven.getPossibleTargetFields(game, marble1, sevenMoves);
        assertEquals(possibleTargetFields1.size(), 7);
        assertEquals(possibleTargetFields1.get(0), targetFieldKey1Marble1);
        assertEquals(possibleTargetFields1.get(1), targetFieldKey2Marble1);
        assertEquals(possibleTargetFields1.get(2), targetFieldKey3Marble1);
        assertEquals(possibleTargetFields1.get(3), targetFieldKey4Marble1);
        assertEquals(possibleTargetFields1.get(4), targetFieldKey5Marble1);
        assertEquals(possibleTargetFields1.get(5), targetFieldKey6Marble1);
        assertEquals(possibleTargetFields1.get(6), targetFieldKey7Marble1);
        //possibleTargetFields Marble 2 (On start)
        String targetFieldKey1 = "1GREEN";
        String targetFieldKey2 = "2GREEN";
        String targetFieldKey3 = "3GREEN";
        String targetFieldKey4 = "4GREEN";
        String targetFieldKey5 = "5GREEN";
        String targetFieldKey6 = "6GREEN";
        String targetFieldKey7 = "7GREEN";
        List<String> possibleTargetFields2 = splitSeven.getPossibleTargetFields(game, marble2, sevenMoves);
        assertEquals(possibleTargetFields2.size(), 7);
        assertEquals(possibleTargetFields2.get(0), targetFieldKey1);
        assertEquals(possibleTargetFields2.get(1), targetFieldKey2);
        assertEquals(possibleTargetFields2.get(2), targetFieldKey3);
        assertEquals(possibleTargetFields2.get(3), targetFieldKey4);
        assertEquals(possibleTargetFields2.get(4), targetFieldKey5);
        assertEquals(possibleTargetFields2.get(5), targetFieldKey6);
        assertEquals(possibleTargetFields2.get(6), targetFieldKey7);
        // now marble1 goes 3 steps -> Field: 4GREEN
        // marble2 goes 4 steps and eats 3 -> Marble1 Field 22BLUE
        // marble2 Field: 4 GREEN
        String targetFieldKeyMarble1 = "4GREEN";
        sevenMoves.add(new MarbleIdAndTargetFieldKey(marble1.getMarbleId(), targetFieldKeyMarble1));
        String targetFieldKeyMarble2 = "4GREEN";
        sevenMoves.add(new MarbleIdAndTargetFieldKey(marble2.getMarbleId(), targetFieldKeyMarble2));

        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = splitSeven.executeMove(game, sevenMoves);
        assertEquals(executedMoves.size(), 3);
        assertEquals(executedMoves.get(0).getMarbleId(), marble1.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), "4GREEN");
        assertEquals(executedMoves.get(1).getMarbleId(), marble2.getMarbleId());
        assertEquals(executedMoves.get(1).getFieldKey(), "4GREEN");
        assertEquals(executedMoves.get(2).getMarbleId(), marble1.getMarbleId());
        assertEquals(executedMoves.get(2).getFieldKey(), "22BLUE");


    }

    @Test
    public void testSevenEat_jumpedOverAnotherMarble_marbleEaten() {
        Game game = setupGame();
        Marble marble1 = goToStart(game);
        String targetFieldKey = "1GREEN";
        INormalMove oneForwards = new OneForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble1.getMarbleId(), targetFieldKey));
        oneForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        Marble marble2 = goToStart(game);
        ArrayList<MarbleIdAndTargetFieldKey> sevenMoves = new  ArrayList<>();

        ISplitMove splitSeven = new SplitSeven();
        // marble 1 makes One step to 2 Green
        // marble 2 makes 6 steps to 6 Green
        String targetFieldKeyMarble1 = "2GREEN";
        sevenMoves.add(new MarbleIdAndTargetFieldKey(marble1.getMarbleId(), targetFieldKeyMarble1));
        String targetFieldKeyMarble2 = "6GREEN";
        sevenMoves.add(new MarbleIdAndTargetFieldKey(marble2.getMarbleId(), targetFieldKeyMarble2));


        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = splitSeven.executeMove(game, sevenMoves);
        assertEquals(executedMoves.size(), 3);
        assertEquals(executedMoves.get(0).getMarbleId(), marble1.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), "2GREEN");
        assertEquals(executedMoves.get(1).getMarbleId(), marble2.getMarbleId());
        assertEquals(executedMoves.get(1).getFieldKey(), "6GREEN");
        assertEquals(executedMoves.get(2).getMarbleId(), marble1.getMarbleId());
        assertEquals(executedMoves.get(2).getFieldKey(), "22BLUE");

    }
}
