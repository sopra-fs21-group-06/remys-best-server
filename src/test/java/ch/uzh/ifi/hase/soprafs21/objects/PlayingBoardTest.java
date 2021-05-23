package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.AbstractTest;
import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.moves.GoToStart;
import ch.uzh.ifi.hase.soprafs21.moves.INormalMove;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PlayingBoardTest extends AbstractTest {

    @Test
    public void testMarbleOnHomeStack(){
        Game game = setupGame();

        assertTrue(game.getPlayingBoard().hasMarbleOnHomeStack(Color.RED));
        assertTrue(game.getPlayingBoard().hasMarbleOnHomeStack(Color.YELLOW));
        assertTrue(game.getPlayingBoard().hasMarbleOnHomeStack(Color.GREEN));
        assertTrue(game.getPlayingBoard().hasMarbleOnHomeStack(Color.BLUE));
    }

    @Test
    public void testNumberMarblesAtHome(){
        Game game = setupGame();

        //assertEquals(4, game.getPlayingBoard().getNumberMarblesAtHome(Color.RED));
        //assertEquals(4, game.getPlayingBoard().getNumberMarblesAtHome(Color.YELLOW));
        //assertEquals(4, game.getPlayingBoard().getNumberMarblesAtHome(Color.GREEN));
        //assertEquals(4, game.getPlayingBoard().getNumberMarblesAtHome(Color.BLUE));

        Color currentPlayerColor = game.getCurrentRound().getCurrentPlayer().getColor();
        Marble marbleToGoOut = game.getCurrentRound().getCurrentPlayer().getMarbleList().get(0);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        String targetFieldKey = "16" + currentPlayerColor;
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marbleToGoOut.getMarbleId(), targetFieldKey));
        INormalMove goToStart = new GoToStart();

        goToStart.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
        //assertEquals(3, game.getPlayingBoard().getNumberMarblesAtHome(currentPlayerColor));

        goToStart.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
        //assertEquals(2, game.getPlayingBoard().getNumberMarblesAtHome(currentPlayerColor));

        goToStart.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
        //assertEquals(1, game.getPlayingBoard().getNumberMarblesAtHome(currentPlayerColor));

        goToStart.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
        //assertEquals(0, game.getPlayingBoard().getNumberMarblesAtHome(currentPlayerColor));
    }

    @Test
    public void testGetField(){
        Game game = setupGame();

        assertEquals("16BLUE", game.getPlayingBoard().getField(16, Color.BLUE).getFieldKey());
        assertEquals("1GREEN", game.getPlayingBoard().getField(1, Color.GREEN).getFieldKey());
        assertEquals("20YELLOW", game.getPlayingBoard().getField(20, Color.YELLOW).getFieldKey());
        assertEquals("5RED", game.getPlayingBoard().getField(5, Color.RED).getFieldKey());
    }

    @Test
    public void testGetFieldByFieldKey(){
        Game game = setupGame();

        assertEquals("16BLUE", game.getPlayingBoard().getFieldByFieldKey("16BLUE").getFieldKey());
        assertEquals("1GREEN", game.getPlayingBoard().getFieldByFieldKey("1GREEN").getFieldKey());
        assertEquals("20YELLOW", game.getPlayingBoard().getFieldByFieldKey("20YELLOW").getFieldKey());
        assertEquals("5RED", game.getPlayingBoard().getFieldByFieldKey("5RED").getFieldKey());
    }

    @Test
    public void testSendHome(){
        Game game = setupGame();

        addCardsToHand(game.getPlayers().get(0), List.of("2D"));
        addCardsToHand(game.getPlayers().get(1), List.of("2H"));
        addCardsToHand(game.getPlayers().get(2), List.of("2S"));
        addCardsToHand(game.getPlayers().get(3), List.of("2C"));

        Color currentPlayerColor = game.getCurrentRound().getCurrentPlayer().getColor();
        goOutWithMarbleOfCurrentPlayer(game);
        //assertEquals(3, game.getPlayingBoard().getNumberMarblesAtHome(currentPlayerColor));
        game.getPlayingBoard().sendHome(game.getCurrentRound().getCurrentPlayer().getMarbleList().get(0));
        //assertEquals(4, game.getPlayingBoard().getNumberMarblesAtHome(currentPlayerColor));

        game.getCurrentRound().changeCurrentPlayer();
        currentPlayerColor = game.getCurrentRound().getCurrentPlayer().getColor();
        goOutWithMarbleOfCurrentPlayer(game);
        //assertEquals(3, game.getPlayingBoard().getNumberMarblesAtHome(currentPlayerColor));
        game.getPlayingBoard().sendHome(game.getCurrentRound().getCurrentPlayer().getMarbleList().get(0));
        //assertEquals(4, game.getPlayingBoard().getNumberMarblesAtHome(currentPlayerColor));

        game.getCurrentRound().changeCurrentPlayer();
        currentPlayerColor = game.getCurrentRound().getCurrentPlayer().getColor();
        goOutWithMarbleOfCurrentPlayer(game);
        //assertEquals(3, game.getPlayingBoard().getNumberMarblesAtHome(currentPlayerColor));
        game.getPlayingBoard().sendHome(game.getCurrentRound().getCurrentPlayer().getMarbleList().get(0));
        //assertEquals(4, game.getPlayingBoard().getNumberMarblesAtHome(currentPlayerColor));

        game.getCurrentRound().changeCurrentPlayer();
        currentPlayerColor = game.getCurrentRound().getCurrentPlayer().getColor();
        goOutWithMarbleOfCurrentPlayer(game);
        //assertEquals(3, game.getPlayingBoard().getNumberMarblesAtHome(currentPlayerColor));
        game.getPlayingBoard().sendHome(game.getCurrentRound().getCurrentPlayer().getMarbleList().get(0));
        //assertEquals(4, game.getPlayingBoard().getNumberMarblesAtHome(currentPlayerColor));
    }

    private void goOutWithMarbleOfCurrentPlayer(Game game) {
        Color currentPlayerColor = game.getCurrentRound().getCurrentPlayer().getColor();
        Marble marbleToGoOut = game.getCurrentRound().getCurrentPlayer().getMarbleList().get(0);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        String targetFieldKey = "16" + currentPlayerColor;
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marbleToGoOut.getMarbleId(), targetFieldKey));
        INormalMove goToStart = new GoToStart();

        goToStart.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }
}
