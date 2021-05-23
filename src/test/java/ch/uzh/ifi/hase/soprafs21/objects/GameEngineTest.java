package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.AbstractTest;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameEngineTest extends AbstractTest {

    @Autowired
    private GameEngine gameEngine;

    private User user1;
    private User user2;
    private User user3;
    private User user4;

    @BeforeEach
    public void setup() {
        super.setup();
        user1 = createTestUser("User 1", "user1@gmail.com");
        user2 = createTestUser("User 2", "user2@gmail.com");
        user3 = createTestUser("User 3", "user3@gmail.com");
        user4 = createTestUser("User 4", "user4@gmail.com");
    }

    @Test
    public void addUserToWaitingRoomTest() {
        gameEngine.addUserToWaitingRoom(user1);
        assertTrue(gameEngine.getWaitingRoom().userInHere(user1));
        gameEngine.removeUserFromWaitingRoom(user1);
    }

    @Test
    public void removeUserFromWaitingRoomTest(){
        gameEngine.addUserToWaitingRoom(user1);
        gameEngine.removeUserFromWaitingRoom(user1);
        assertFalse(gameEngine.getWaitingRoom().userInHere(user1));
    }

    @Test
    public void addUserToGameSessionTest(){
        gameEngine.newGameSession(user1);
        gameEngine.addUserToGameSession(user2,gameEngine.getGameSessionList().get(0).getID());
        assertTrue(gameEngine.getGameSessionList().get(0).isAcceptedUserInHere(user2));
        gameEngine.deleteGameSession(gameEngine.getGameSessionList().get(0).getID());
    }

    @Test
    public void addInvitedUserToGameSession(){
        gameEngine.newGameSession(user1);
        gameEngine.addInvitedUserToGameSession(user2,gameEngine.getGameSessionList().get(0).getID());
        assertTrue(gameEngine.getGameSessionList().get(0).isInvitedUserInHere(user2.getUsername()));
        gameEngine.deleteGameSession(gameEngine.getGameSessionList().get(0).getID());
    }

    @Test
    public void createGameFromGameSessionAndFillUpTest(){
        gameEngine.newGameSession(user1);
        gameEngine.addUserToGameSession(user2,gameEngine.getGameSessionList().get(0).getID());
        gameEngine.addUserToWaitingRoom(user3);
        gameEngine.addUserToWaitingRoom(user4);
        gameEngine.createGameFromGameSessionAndFillUp(gameEngine.getGameSessionList().get(0));
        assertFalse(gameEngine.getRunningGamesList().isEmpty());
        assertTrue(gameEngine.getGameSessionList().isEmpty());
        gameEngine.deleteGameByGameID(gameEngine.getRunningGamesList().get(0).getGameId());
    }

    @Test
    public void deleteGameSessionByHostNameTest(){
        gameEngine.newGameSession(user1);
        gameEngine.deleteGameSessionByHostName(user1.getUsername());
        assertTrue(gameEngine.getGameSessionList().isEmpty());
    }

    @Test
    public void getUsersByGameSessionIDTest(){
        gameEngine.newGameSession(user1);
        gameEngine.addUserToGameSession(user2,gameEngine.getGameSessionList().get(0).getID());
        assertTrue(gameEngine.getAcceptedUsersByGameSessionId(gameEngine.getGameSessionList().get(0).getID()) instanceof ArrayList);
        assertEquals(user1.getUsername(), gameEngine.getAcceptedUsersByGameSessionId(gameEngine.getGameSessionList().get(0).getID()).get(0).getUsername());
        gameEngine.deleteGameSession(gameEngine.getGameSessionList().get(0).getID());
    }

    @Test
    public void isUserInWaitingRoomTest(){
        gameEngine.addUserToWaitingRoom(user1);
        assertTrue(gameEngine.userInWaitingRoom(user1.getUsername()));
        gameEngine.removeUserFromWaitingRoom(user1);
    }

    @Test
    public void userIsHostTest(){
        gameEngine.newGameSession(user1);
        assertTrue(gameEngine.userIsHost(user1.getUsername()));
        gameEngine.deleteGameSession(gameEngine.getGameSessionList().get(0).getID());
    }

    @Test
    public void findGameSessionByHostnameTest(){
        gameEngine.newGameSession(user1);
        assertEquals(gameEngine.getGameSessionList().get(0), gameEngine.findGameSessionByHostName(user1.getUsername()));
        gameEngine.deleteGameSession(gameEngine.getGameSessionList().get(0).getID());
    }

    @Test
    public void userInGameTest(){
        gameEngine.addUserToWaitingRoom(user1);
        gameEngine.addUserToWaitingRoom(user2);
        gameEngine.addUserToWaitingRoom(user3);
        gameEngine.addUserToWaitingRoom(user4);
        assertTrue(gameEngine.userInGame(user2.getUsername()));
        gameEngine.deleteGameByGameID(gameEngine.getRunningGamesList().get(0).getGameId());
    }

    @Test
    public void deleteGameByGameIDTest(){
        gameEngine.addUserToWaitingRoom(user1);
        gameEngine.addUserToWaitingRoom(user2);
        gameEngine.addUserToWaitingRoom(user3);
        gameEngine.addUserToWaitingRoom(user4);
        gameEngine.deleteGameByGameID(gameEngine.getRunningGamesList().get(0).getGameId());
        assertTrue(gameEngine.getRunningGamesList().isEmpty());
    }


    @Test
    public void testCreateGameFromWaitingRoom(){
        gameEngine.addUserToWaitingRoom(user1);
        gameEngine.addUserToWaitingRoom(user2);
        gameEngine.addUserToWaitingRoom(user3);
        gameEngine.addUserToWaitingRoom(user4);
        assertFalse(gameEngine.getRunningGamesList().isEmpty());
        assertEquals(gameEngine.getWaitingRoom().getUserCount(), 0);
        gameEngine.deleteGameByGameID(gameEngine.getRunningGamesList().get(0).getGameId());
    }

    @Test
    public void userInGameSessionTest(){
        gameEngine.newGameSession(user1);
        assertTrue(gameEngine.userInGameSession(user1,gameEngine.findGameSessionIdByUsername(user1.getUsername())));
        gameEngine.deleteGameSession(gameEngine.findGameSessionIdByUsername(user1.getUsername()));
    }

    @Test
    public void findPlayerByUsernameTest(){
        gameEngine.addUserToWaitingRoom(user1);
        gameEngine.addUserToWaitingRoom(user2);
        gameEngine.addUserToWaitingRoom(user3);
        gameEngine.addUserToWaitingRoom(user4);
        assertEquals(user1.getUsername(), gameEngine.findPlayerbyUsername(gameEngine.getRunningGamesList().get(0), "User 1").getPlayerName());
        gameEngine.deleteGameByGameID(gameEngine.findGameIdByPlayerName("User 1"));
    }

    @Test
    public void gameSessionDeletionTest(){
        gameEngine.newGameSession(user1);
        gameEngine.deleteGameSession(gameEngine.getGameSessionList().get(0).getID());
        assertTrue(gameEngine.getGameSessionList().isEmpty());

    }

    @Test
    public void findGameSessionIdByUsernameTest(){
        gameEngine.newGameSession(user1);
        assertEquals(gameEngine.getGameSessionList().get(0).getID() ,gameEngine.findGameSessionIdByUsername(user1.getUsername()));
        gameEngine.deleteGameSession(gameEngine.findGameSessionIdByUsername(user1.getUsername()));
    }

    @Test
    public void testCreateGameSession_Successful(){
        gameEngine.newGameSession(user1);
        assertFalse(gameEngine.getGameSessionList().isEmpty());
        assertEquals(user1, gameEngine.getGameSessionList().get(0).getAcceptedUsers().get(0));
        gameEngine.deleteGameSession(gameEngine.getGameSessionList().get(0).getID());
    }


    @Test
    public void testCreateGameFromGameSession_Successful(){
        gameEngine.newGameSession(user1);
        gameEngine.addUserToGameSession(user2,gameEngine.getGameSessionList().get(0).getID());
        gameEngine.addUserToGameSession(user3,gameEngine.getGameSessionList().get(0).getID());
        gameEngine.addUserToGameSession(user4,gameEngine.getGameSessionList().get(0).getID());
        gameEngine.createGameFromGameSession(gameEngine.findGameSessionByHostName("User 1"));
        assertFalse(gameEngine.getRunningGamesList().isEmpty());
        assertTrue(gameEngine.getGameSessionList().isEmpty());
        gameEngine.deleteGameByGameID(gameEngine.getRunningGamesList().get(0).getGameId());
    }

    @Test
    public void testGameDeletion_Successful(){
        gameEngine.addUserToWaitingRoom(user1);
        gameEngine.addUserToWaitingRoom(user2);
        gameEngine.addUserToWaitingRoom(user3);
        gameEngine.addUserToWaitingRoom(user4);
        gameEngine.deleteGameByGameID(gameEngine.getRunningGamesList().get(0).getGameId());
        assertTrue(gameEngine.getRunningGamesList().isEmpty());
    }


    @Test
    public void testDeletingUserFromGameSession_Successful(){
        user1.setId(1L);
        gameEngine.newGameSession(user1);
        user2.setId(2L);
        gameEngine.addUserToGameSession(user2,gameEngine.getGameSessionList().get(0).getID());
        assertTrue(gameEngine.getGameSessionList().get(0).getAcceptedUsers().contains(user2));
        gameEngine.deleteUserFromGameSession(user2,gameEngine.getGameSessionList().get(0).getID());
        assertFalse(gameEngine.userInGameSession(user2, gameEngine.getGameSessionList().get(0).getID()));
        gameEngine.deleteGameSession(gameEngine.getGameSessionList().get(0).getID());
    }

    @Test
    public void testDeletingHostFromGameSession(){
        user1.setId(1L);
        gameEngine.newGameSession(user1);
        gameEngine.deleteUserFromGameSession(user1,gameEngine.getGameSessionList().get(0).getID());
        assertTrue(gameEngine.getGameSessionList().get(0).getAcceptedUsers().contains(user1));
        gameEngine.deleteGameSession(gameEngine.getGameSessionList().get(0).getID());
    }
}
