package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.AbstractTest;
import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

    }


    @Test
    public void setPlayerToReadyTest(){
        game.setPlayerToReady(game.getPlayers().get(0).getPlayerName());
        assertNotEquals(game.getCurrentRound(), null);
    }

    @Test
    public void changeStartingPlayerTest(){
        Player player = game.getStartPlayer();
        game.changeStartingPlayer();
        assertNotEquals(player, game.getStartPlayer());

    }
}
