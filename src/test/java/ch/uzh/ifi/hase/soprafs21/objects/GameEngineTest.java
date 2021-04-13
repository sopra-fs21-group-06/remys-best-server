package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class GameEngineTest {

    GameEngine gameEngine = GameEngine.instance();
    User user1 = new User();
    User user2 = new User();
    User user3 = new User();
    User user4 = new User();



    @Test
    public void testCreateGameFromWaitingRoom(){
        gameEngine.addUserToWaitingRoom(user1);
        gameEngine.addUserToWaitingRoom(user2);
        gameEngine.addUserToWaitingRoom(user3);
        gameEngine.addUserToWaitingRoom(user4);
        assertTrue(!gameEngine.getRunningGamesList().isEmpty());
        assertTrue(gameEngine.getWaitingRoom().getUserCount()==0);
        gameEngine.deleteGameByGameID(gameEngine.getRunningGamesList().get(0).getGameID());
    }
    @Test
    public void testCreateGameSession_Successful(){
        gameEngine.newGameSession(user1);
        assertTrue(!gameEngine.getGameSessionList().isEmpty());
        assertTrue(gameEngine.getGameSessionList().get(0).getUserList().get(0).equals(user1));
        gameEngine.deleteGameSession(gameEngine.getGameSessionList().get(0).getID());
    }
    @Test
    public void testCreateGameFromGameSession_Successful(){
        gameEngine.newGameSession(user1);
        gameEngine.addUserToSession(user2,gameEngine.getGameSessionList().get(0).getID());
        gameEngine.addUserToSession(user3,gameEngine.getGameSessionList().get(0).getID());
        gameEngine.addUserToSession(user4,gameEngine.getGameSessionList().get(0).getID());
        gameEngine.createGameFromGameSession(gameEngine.getGameSessionList().get(0));
        assertTrue(!gameEngine.getRunningGamesList().isEmpty());
        assertTrue(gameEngine.getGameSessionList().isEmpty());
        gameEngine.deleteGameSession(gameEngine.getRunningGamesList().get(0).getGameID());
    }

    @Test
    public void testGameDeletion_Successful(){
        gameEngine.addUserToWaitingRoom(user1);
        gameEngine.addUserToWaitingRoom(user2);
        gameEngine.addUserToWaitingRoom(user3);
        gameEngine.addUserToWaitingRoom(user4);
        gameEngine.deleteGameByGameID(gameEngine.getRunningGamesList().get(0).getGameID());
        assertTrue(gameEngine.getRunningGamesList().isEmpty());
    }


    @Test
    public void testDeletingUserFromGameSession_Successful(){
        user1.setId(1L);
        gameEngine.newGameSession(user1);
        user2.setId(2L);
        gameEngine.addUserToSession(user2,gameEngine.getGameSessionList().get(0).getID());
        assertTrue(gameEngine.getGameSessionList().get(0).getUserList().contains(user2));
        gameEngine.deleteUserFromSession(user2,gameEngine.getGameSessionList().get(0).getID());
        assertTrue(!gameEngine.userInGameSession(user2,gameEngine.getGameSessionList().get(0).getID()));
        gameEngine.deleteGameSession(gameEngine.getGameSessionList().get(0).getID());
    }

    /** user does not get deleted if it is the host **/
    @Test
    public void testDeletingHostFromGameSession(){
        user1.setId(1L);
        gameEngine.newGameSession(user1);
        gameEngine.deleteUserFromSession(user1,gameEngine.getGameSessionList().get(0).getID());
        assertTrue(gameEngine.getGameSessionList().get(0).getUserList().contains(user1));
    }

}