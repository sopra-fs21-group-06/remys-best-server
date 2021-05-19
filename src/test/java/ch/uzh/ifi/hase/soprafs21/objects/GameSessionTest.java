package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameSessionTest {

    @Autowired
    UserService userService;

    private User host;

    @BeforeEach
    public void setup() {
        userService.getUserRepository().deleteAll();
        host = createTestUser("host", "host@host.ch");
    }

    @Test
    public void testInvitedUsers(){
        GameSession gameSession = new GameSession(host, userService);
        assertFalse(gameSession.isInvitedUserInHere("Peter"));

        //add
        User newUser = createTestUser("Peter", "peter@peter.ch");
        gameSession.addInvitedUser(newUser);
        assertTrue(gameSession.isInvitedUserInHere(newUser.getUsername()));
        assertEquals(UserStatus.Busy, userService.findByUsername(newUser.getUsername()).getStatus());

        //delete
        gameSession.deleteInvitedUser(newUser);
        assertFalse(gameSession.isInvitedUserInHere(newUser.getUsername()));
        assertEquals(UserStatus.Free, userService.findByUsername(newUser.getUsername()).getStatus());
    }

    @Test
    public void testAcceptedUsers(){
        GameSession gameSession = new GameSession(host, userService);
        User newUser = createTestUser("Peter", "peter@peter.ch");

        assertTrue(gameSession.isAcceptedUserInHere(host));
        assertFalse(gameSession.isAcceptedUserInHere(newUser));

        //add
        gameSession.addAcceptedUser(newUser);
        assertTrue(gameSession.isAcceptedUserInHere(newUser));
        assertEquals(UserStatus.Busy, userService.findByUsername(newUser.getUsername()).getStatus());

        //delete
        gameSession.deleteAcceptedUser(newUser);
        assertFalse(gameSession.isAcceptedUserInHere(newUser));
        assertEquals(UserStatus.Free, userService.findByUsername(newUser.getUsername()).getStatus());
    }

    private User createTestUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password");
        user.setSessionIdentity(UUID.randomUUID().toString());
        userService.createUser(user);
        return user;
    }
}
