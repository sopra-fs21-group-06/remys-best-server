package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.AbstractTest;
import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.ExecutePlayCardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameTest  extends AbstractTest  {
    private User user1;
    private Game game;

    @BeforeEach
    public void setup() {
        super.setup();
        this.game = setupGame();
        user1 = createTestUser("User 1", "user1@gmail.com");

    }

    @Test
    public void userToPlayerTest(){
        Player player1 = game.userToPlayer(user1);
        assertEquals(user1.getUsername(), player1.getPlayerName());
    }

    @Test
    public void updatePlayerColorTest(){
        game.updatePlayerColor(game.getPlayers().get(0).getPlayerName(), Color.RED);
        assertEquals(game.getPlayers().get(0).getColor(), Color.RED);
        //game.updatePlayerColor(game.getPlayers().get(0).getPlayerName(),null);
        //game.updatePlayerColor(game.getPlayers().get(1).getPlayerName(),null);
        //game.updatePlayerColor(game.getPlayers().get(0).getPlayerName(),Color.RED);
        //game.updatePlayerColor(game.getPlayers().get(0).getPlayerName(),Color.BLUE);
        //assertEquals(game.getPlayers().get(0).getColor(), Color.RED);
        //assertEquals(game.getPlayers().get(1).getColor(), Color.BLUE);

    }


    @Test
    public void setPlayerToReadyTest(){
        game.setPlayerToReady(game.getPlayers().get(0).getPlayerName());
        assertNotEquals(game.getCurrentRound(), null);
    }

    @Test
    public void executeMoveTest(){

    }

    @Test
    public void changeStartingPlayerTest(){
        Player player = game.getStartPlayer();
        game.changeStartingPlayer();
        assertNotEquals(player, game.getStartPlayer());

    }
}
