package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SplitSevenMoveTest extends AbstractMoveTest {
    Logger log = LoggerFactory.getLogger(SplitSevenMoveTest.class);
    // Enabling move of any moveable marble of team using SEVEN #90
   // Move 7 fields forward using SEVEN #89
    @Test
    public void testSeven() {
        Game game = setupGame();
        Marble marble1 = goToStart(game);


        ArrayList<MarbleIdAndTargetFieldKey> sevenMoves = new  ArrayList<>();

        ISplitMove splitSeven = new SplitSeven();

        List<Marble> playableMarbles = splitSeven.getPlayableMarbles(game, game.getGameService(),sevenMoves);
        assertTrue(playableMarbles.contains(marble1));
        assertEquals(playableMarbles.size(), 1);

        //possibleTargetFields Marble1 (is 4 back)
        String targetFieldKey1Marble1 = "1GREEN";
        String targetFieldKey2Marble1 = "2GREEN";
        String targetFieldKey3Marble1 = "3GREEN";
        String targetFieldKey4Marble1 = "4GREEN";
        String targetFieldKey5Marble1 = "5GREEN";
        String targetFieldKey6Marble1 = "6GREEN";
        String targetFieldKey7Marble1 = "7GREEN";

        List<String> possibleTargetFields1 = splitSeven.getPossibleTargetFields(game, marble1, sevenMoves);
        assertEquals(possibleTargetFields1.size(), 7);
        assertEquals(possibleTargetFields1.get(0), targetFieldKey1Marble1);
        assertEquals(possibleTargetFields1.get(1), targetFieldKey2Marble1);
        assertEquals(possibleTargetFields1.get(2), targetFieldKey3Marble1);
        assertEquals(possibleTargetFields1.get(3), targetFieldKey4Marble1);
        assertEquals(possibleTargetFields1.get(4), targetFieldKey5Marble1);
        assertEquals(possibleTargetFields1.get(5), targetFieldKey6Marble1);
        assertEquals(possibleTargetFields1.get(6), targetFieldKey7Marble1);


        sevenMoves.add(new MarbleIdAndTargetFieldKey(marble1.getMarbleId(), targetFieldKey7Marble1));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = splitSeven.executeMove(game, sevenMoves);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble1.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), "7GREEN");

    }
    // Partition total number of moves using SEVEN #91
    // Enabling move of any moveable marble of team using SEVEN #90
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
    //Passed marbles get sent home using SEVEN #93
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
    // Enabling move of any moveable marble of team using SEVEN #90
    // Test checks if one player can finish with seven that the rest of the seven is given to teammate
    @Test
    public void testSeven_GetTeamMateMarbles(){
        Game game = setupGame();
        //Send all Marbles except for one to finish
        Player player = game.getCurrentRound().getCurrentPlayer();
        Player teammate = player.getTeamMate();
        List<Marble> playerMarbles = player.getMarbleList();
        Marble marble1 = playerMarbles.get(3);
        Marble marble2 = playerMarbles.get(2);
        Marble marble3 = playerMarbles.get(1);
        Marble marble4 = playerMarbles.get(0);
        String finishField1 = "12BLUE";
        String finishField2 = "13BLUE";
        String finishField3 = "14BLUE";
        String finishField4 = "15BLUE";
        marble1.setCurrentField(game.getPlayingBoard().getFieldByFieldKey(finishField1));
        marble2.setCurrentField(game.getPlayingBoard().getFieldByFieldKey(finishField2));
        marble3.setCurrentField(game.getPlayingBoard().getFieldByFieldKey(finishField3));
        marble4.setCurrentField(game.getPlayingBoard().getFieldByFieldKey(finishField4));
        finishField1 = "20BLUE";
        finishField2 = "19BLUE";
        finishField3 = "18BLUE";
        finishField4 = "16BLUE";
        Field field1 = game.getPlayingBoard().getFieldByFieldKey(finishField1);
        Field field2 = game.getPlayingBoard().getFieldByFieldKey(finishField2);
        Field field3 = game.getPlayingBoard().getFieldByFieldKey(finishField3);
        Field field4 = game.getPlayingBoard().getFieldByFieldKey(finishField4);
        game.getPlayingBoard().makeMove(field1, marble1);
        game.getPlayingBoard().makeMove(field2, marble2);
        game.getPlayingBoard().makeMove(field3, marble3);
        game.getPlayingBoard().makeMove(field4, marble4);
        marble1.setHome(Boolean.FALSE);
        marble2.setHome(Boolean.FALSE);
        marble3.setHome(Boolean.FALSE);
        marble4.setHome(Boolean.FALSE);
        assertTrue(marble1.getFinish());
        assertTrue(marble2.getFinish());
        assertTrue(marble3.getFinish());
        assertFalse(marble4.getFinish());


        Marble marbleTeamMate =  game.getPlayingBoard().makeStartMove(teammate.getColor());
        ArrayList<MarbleIdAndTargetFieldKey> sevenMoves = new  ArrayList<>();

        ISplitMove splitSeven = new SplitSeven();
        List<Marble> playableMarbles = splitSeven.getPlayableMarbles(game, game.getGameService(),sevenMoves);
        assertEquals(playableMarbles.size(),1);
        assertEquals(playableMarbles.get(0), marble4);
        //Marble is on homefield and possible TargetFIelds are last finishspot or 7 steps into green part
        String targetFieldKey1 = "1GREEN";
        String targetFieldKey2 = "2GREEN";
        String targetFieldKey3 = "3GREEN";
        String targetFieldKey4 = "4GREEN";
        String targetFieldKey5 = "5GREEN";
        String targetFieldKey6 = "6GREEN";
        String targetFieldKey7 = "7GREEN";
        String finalTargetField = "17BLUE";
        List<String> possibleTargetFields = splitSeven.getPossibleTargetFields(game, marble4, sevenMoves);
        assertEquals(possibleTargetFields.size(), 8);
        assertEquals(possibleTargetFields.get(0), targetFieldKey1);
        assertEquals(possibleTargetFields.get(1), targetFieldKey2);
        assertEquals(possibleTargetFields.get(2), targetFieldKey3);
        assertEquals(possibleTargetFields.get(3), targetFieldKey4);
        assertEquals(possibleTargetFields.get(4), targetFieldKey5);
        assertEquals(possibleTargetFields.get(5), targetFieldKey6);
        assertEquals(possibleTargetFields.get(6), targetFieldKey7);
        assertEquals(possibleTargetFields.get(7), finalTargetField);

        //Now marble4 move into last finish spot
        sevenMoves.add(new MarbleIdAndTargetFieldKey(marble4.getMarbleId(),finalTargetField));
        // Now get Playable marbles after all Marbles player are finished
        playableMarbles = splitSeven.getPlayableMarbles(game, game.getGameService(),sevenMoves);
        assertEquals(playableMarbles.size(),1);
        assertEquals(playableMarbles.get(0), marbleTeamMate);
        //Marble teammate is on field at home -> 16 RED, remainseven is 6, so possible TargetFields:
        targetFieldKey1 = "1YELLOW";
        targetFieldKey2 = "2YELLOW";
        targetFieldKey3 = "3YELLOW";
        targetFieldKey4 = "4YELLOW";
        targetFieldKey5 = "5YELLOW";
        targetFieldKey6 = "6YELLOW";

        possibleTargetFields = splitSeven.getPossibleTargetFields(game, marbleTeamMate, sevenMoves);
        assertEquals(possibleTargetFields.size(), 6);
        assertEquals(possibleTargetFields.get(0), targetFieldKey1);
        assertEquals(possibleTargetFields.get(1), targetFieldKey2);
        assertEquals(possibleTargetFields.get(2), targetFieldKey3);
        assertEquals(possibleTargetFields.get(3), targetFieldKey4);
        assertEquals(possibleTargetFields.get(4), targetFieldKey5);
        assertEquals(possibleTargetFields.get(5), targetFieldKey6);
        sevenMoves.add(new MarbleIdAndTargetFieldKey(marbleTeamMate.getMarbleId(), targetFieldKey6));

        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = splitSeven.executeMove(game, sevenMoves);
        assertEquals(executedMoves.size(), 2);
        assertEquals(executedMoves.get(0).getMarbleId(), marble4.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), finalTargetField);
        assertEquals(executedMoves.get(1).getMarbleId(), marbleTeamMate.getMarbleId());
        assertEquals(executedMoves.get(1).getFieldKey(), targetFieldKey6);
        assertTrue(marble4.getFinish());
        game.getGameService().endTurn(game);
        assertTrue(player.isFinished());
        assertEquals(player.getMarbleList(), teammate.getMarbleList());

    }

}
