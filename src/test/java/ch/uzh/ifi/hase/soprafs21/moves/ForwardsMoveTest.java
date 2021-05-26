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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ForwardsMoveTest extends AbstractMoveTest {
    Logger log = LoggerFactory.getLogger(ForwardsMoveTest.class);
    // Reaching occupied field sends home occupying marble #94
    @Test
    public void testEat_sameTargetField_marbleEaten() {
        Game game = setupGame();
        Marble marbleBluePlayer = goToStart(game);
        INormalMove fourBackwards = new FourBackwards();
        String targetFieldKey = "12BLUE";
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys1 = new ArrayList<>();
        marbleIdAndTargetFieldKeys1.add(new MarbleIdAndTargetFieldKey(marbleBluePlayer.getMarbleId(), targetFieldKey));
        fourBackwards.executeMove(game, marbleIdAndTargetFieldKeys1);
        Player yellowPlayer = game.getPlayers().get(3);
        game.getCurrentRound().setCurrentPlayer(yellowPlayer);
        Marble marbleYellowPlayer = goToStart(game);
        INormalMove twelveForwards = new TwelveForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marbleYellowPlayer.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = twelveForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 2);
        assertEquals(executedMoves.get(0).getMarbleId(), marbleYellowPlayer.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), "12BLUE");
        assertEquals(executedMoves.get(1).getMarbleId(), marbleBluePlayer.getMarbleId());
        assertEquals(executedMoves.get(1).getFieldKey(), "21BLUE");


    }
    //Blocking marbles cannot be sent home #95
    @Test
    public void testEat_BlockingMarblesCannotBeSentHome() {
        Game game = setupGame();
        Marble marbleBlocked = goToStart(game);
        Player yellowPlayer = game.getPlayers().get(3);
        game.getCurrentRound().setCurrentPlayer(yellowPlayer);
        Marble marbleYellowPlayer = goToStart(game);
        INormalMove twelveForwards = new TwelveForwards();
        String targetFieldKey = "12BLUE";
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marbleYellowPlayer.getMarbleId(), targetFieldKey));
        twelveForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        //Now the yellowMarble want to move on 16BLUE Field with 4 Forwards, marbleBlocked doesnt allow any playableMarbles to be received;
        INormalMove fourForwards = new FourForwards();
        List<Marble> possibleMarble = fourForwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(possibleMarble.isEmpty());
    }
   // Basic moves correspond to card values #79 - moves
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
    // Move either 1 or 11 fields forward using ACE #81
    @Test
    public void testMoveOne() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        // two forwards
        INormalMove oneForwards = new OneForwards();
        String targetFieldKey = "1GREEN";

        List<Marble> playableMarbles = oneForwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = oneForwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = oneForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }

    @Test
    public void testMoveTwo() {
        Game game = setupGame();
        Marble marble = goToStart(game);

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
    }
    @Test
    public void testMoveThree() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        INormalMove threeForwards = new ThreeForwards();
        String targetFieldKey = "3GREEN";

        List<Marble> playableMarbles = threeForwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = threeForwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = threeForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }
    @Test
    public void testMoveFive() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        INormalMove fiveForwards = new FiveForwards();
        String targetFieldKey = "5GREEN";

        List<Marble> playableMarbles = fiveForwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = fiveForwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = fiveForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }
    @Test
    public void testMoveSix() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        INormalMove sixForward = new SixForwards();
        String targetFieldKey = "6GREEN";

        List<Marble> playableMarbles = sixForward.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = sixForward.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = sixForward.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }
    @Test
    public void testMoveEight() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        INormalMove eightForward = new EightForwards();
        String targetFieldKey = "8GREEN";

        List<Marble> playableMarbles = eightForward.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = eightForward.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = eightForward.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }
    @Test
    public void testMoveNine() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        INormalMove nineForwards = new NineForwards();
        String targetFieldKey = "9GREEN";

        List<Marble> playableMarbles = nineForwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = nineForwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = nineForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }
    @Test
    public void testMoveTen() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        INormalMove tenForwards = new TenForwards();
        String targetFieldKey = "10GREEN";

        List<Marble> playableMarbles = tenForwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = tenForwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = tenForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }
    // Move either 1 or 11 fields forward using ACE #81
    @Test
    public void testMoveEleven() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        INormalMove elevenForward = new ElevenForwards();
        String targetFieldKey = "11GREEN";

        List<Marble> playableMarbles = elevenForward.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = elevenForward.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = elevenForward.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }
    // Move 12 fields forward using Queen #84
    @Test
    public void testMoveTwelve() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        INormalMove twelveForwards = new TwelveForwards();
        String targetFieldKey = "12GREEN";

        List<Marble> playableMarbles = twelveForwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = twelveForwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = twelveForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }
    // Move 12 fields forward using Queen #84
    @Test
    public void testMoveThirteen() {
        Game game = setupGame();
        Marble marble = goToStart(game);

        INormalMove thirteenForwards = new ThirteenForwards();
        String targetFieldKey = "13GREEN";

        List<Marble> playableMarbles = thirteenForwards.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble));

        List<String> possibleTargetFields = thirteenForwards.getPossibleTargetFields(game, marble);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = thirteenForwards.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 1);
        assertEquals(executedMoves.get(0).getMarbleId(), marble.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKey);
    }


}
