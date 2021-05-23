package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameRequestDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameSessionLeaveDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WSGameSessionControllerTest extends AbstractWSControllerTest {

    @Autowired
    //@MockBean
    private GameEngine gameEngine;

    @MockBean
    WebSocketService websocketService;

    @MockBean
    CardAPIService cardAPIService;

    /*@MockBean
    UserService userService;*/

    private GameRequestDTO generateGameRequestDTO(String token, String username) {
        GameRequestDTO gameRequestDTO = new GameRequestDTO();
        gameRequestDTO.setToken(token);
        gameRequestDTO.setUsername(username);
        return gameRequestDTO;
    }

    private GameSessionLeaveDTO generateGameSessionLeaveDTO(String token) {
        GameSessionLeaveDTO gameSessionLeaveDTO = new GameSessionLeaveDTO();
        gameSessionLeaveDTO.setToken(token);
        return gameSessionLeaveDTO;
    }

    @Test
    void inviteUserTest() {

/*        User user = new User();
        user.setUsername("Siddhant");
        user.setToken("abcd");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Game currentRunningGame = new Game(users, websocketService, cardAPIService);*/

        User testUser1 = createTestUser("abcd_sid", "hello@abcd_sid.com");
        User testUser2 = createTestUser("efgh_sid", "hello@efgh_sid.com");
        User testUser3 = createTestUser("ijkl_sid", "hello@ijkl_sid.com");
        User testUser4 = createTestUser("mnop_sid", "hello@mnop_sid.com");
        testUser1.setStatus(UserStatus.Busy);
        testUser2.setStatus(UserStatus.Free);

        ArrayList<User> users = new ArrayList<>();
        users.add(testUser1);
        //users.add(testUser2);
        //users.add(testUser3);
        //users.add(testUser4);

        gameEngine = GameEngine.instance();
        Game game = new Game(users, websocketService, cardAPIService);
        gameEngine.newGameSession(testUser1);
        //Game currentRunningGame = gameEngine.getRunningGamesList().get(0);

        //doNothing().when(userService).getUserRepository().findByUsername(Mockito.anyString());
        //given(gameEngine.getRunningGameByID(game.getGameId())).willReturn(game);
        //given(userService.getUserRepository().findByUsername(Mockito.anyString())).willReturn(testUser1);
        //given(gameEngine.getRunningGameByID(game.getGameId())).willReturn(game);

        GameRequestDTO gameRequestDTO = generateGameRequestDTO(testUser2.getToken(), testUser2.getUsername());

        stompSession.send("/app/gamesession/"+game.getGameId()+"/invite", gameRequestDTO);

        //verify(websocketService, times(1)).sendGameSessionInvitation();


    }

    @Test
    void inviteUserTestUserBusy() {

/*        User user = new User();
        user.setUsername("Siddhant");
        user.setToken("abcd");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Game currentRunningGame = new Game(users, websocketService, cardAPIService);*/

        User testUser1 = createTestUser("abcd_sid", "hello@abcd_sid.com");
        User testUser2 = createTestUser("efgh_sid", "hello@efgh_sid.com");
        User testUser3 = createTestUser("ijkl_sid", "hello@ijkl_sid.com");
        User testUser4 = createTestUser("mnop_sid", "hello@mnop_sid.com");
        testUser1.setStatus(UserStatus.Busy);
        testUser2.setStatus(UserStatus.Busy);

        ArrayList<User> users = new ArrayList<>();
        users.add(testUser1);
        //users.add(testUser2);
        //users.add(testUser3);
        //users.add(testUser4);

        gameEngine = GameEngine.instance();
        Game game = new Game(users, websocketService, cardAPIService);
        gameEngine.newGameSession(testUser1);
        //Game currentRunningGame = gameEngine.getRunningGamesList().get(0);

        //doNothing().when(userService).getUserRepository().findByUsername(Mockito.anyString());
        //given(gameEngine.getRunningGameByID(game.getGameId())).willReturn(game);
        //given(userService.getUserRepository().findByUsername(Mockito.anyString())).willReturn(testUser1);
        //given(gameEngine.getRunningGameByID(game.getGameId())).willReturn(game);

        GameRequestDTO gameRequestDTO = generateGameRequestDTO(testUser2.getToken(), testUser2.getUsername());

        stompSession.send("/app/gamesession/"+game.getGameId()+"/invite", gameRequestDTO);

        //verify(websocketService, times(1)).sendGameSessionInvitation();


    }

    @Test
    void userLeavesGameSessionTest() {

        User testUser1 = createTestUser("abcd_sid", "hello@abcd_sid.com");
        User testUser2 = createTestUser("efgh_sid", "hello@efgh_sid.com");
        User testUser3 = createTestUser("ijkl_sid", "hello@ijkl_sid.com");
        User testUser4 = createTestUser("mnop_sid", "hello@mnop_sid.com");
        testUser1.setStatus(UserStatus.Busy);
        testUser2.setStatus(UserStatus.Busy);
        testUser3.setStatus(UserStatus.Busy);
        testUser4.setStatus(UserStatus.Busy);

        ArrayList<User> users = new ArrayList<>();
        users.add(testUser1);
        users.add(testUser2);
        users.add(testUser3);
        users.add(testUser4);

        gameEngine = GameEngine.instance();
        Game game = new Game(users, websocketService, cardAPIService);
        gameEngine.newGameSession(testUser1);
        //Game currentRunningGame = gameEngine.getRunningGamesList().get(0);

        //doNothing().when(userService).getUserRepository().findByUsername(Mockito.anyString());
        //given(gameEngine.getRunningGameByID(game.getGameId())).willReturn(game);
        //given(userService.getUserRepository().findByUsername(Mockito.anyString())).willReturn(testUser1);
        //given(gameEngine.getRunningGameByID(game.getGameId())).willReturn(game);

        GameSessionLeaveDTO gameSessionLeaveDTO = generateGameSessionLeaveDTO(testUser1.getToken());

        stompSession.send("/app/gamesession/"+game.getGameId()+"/leave", gameSessionLeaveDTO);

        //verify(websocketService, times(1)).sendGameSessionInvitation();


    }

    /*    @Test
    void fillUpGameSessionTest() {

        User testUser1 = createTestUser("abcd_sid", "hello@abcd_sid.com");
        User testUser2 = createTestUser("efgh_sid", "hello@efgh_sid.com");
        User testUser3 = createTestUser("ijkl_sid", "hello@ijkl_sid.com");
        User testUser4 = createTestUser("mnop_sid", "hello@mnop_sid.com");
        testUser1.setStatus(UserStatus.Busy);
        testUser2.setStatus(UserStatus.Busy);
        testUser3.setStatus(UserStatus.Busy);
        testUser4.setStatus(UserStatus.Busy);

        ArrayList<User> users = new ArrayList<>();
        users.add(testUser1);
        users.add(testUser2);
        users.add(testUser3);
        users.add(testUser4);

        gameEngine = GameEngine.instance();
        Game game = new Game(users, websocketService, cardAPIService);
        gameEngine.newGameSession(testUser1);

        //GameSessionLeaveDTO gameSessionLeaveDTO = generateGameSessionLeaveDTO(testUser1.getToken());

        stompSession.send("/app/gamesession/"+game.getGameId()+"/fill-up", );


    }*/

    @Test
    void rejectInvitationTest() {

        User testUser1 = createTestUser("abcd_sid", "hello@abcd_sid.com");
        User testUser2 = createTestUser("efgh_sid", "hello@efgh_sid.com");
        User testUser3 = createTestUser("ijkl_sid", "hello@ijkl_sid.com");
        User testUser4 = createTestUser("mnop_sid", "hello@mnop_sid.com");
        testUser1.setStatus(UserStatus.Busy);
        testUser2.setStatus(UserStatus.Free);
        testUser3.setStatus(UserStatus.Free);
        testUser4.setStatus(UserStatus.Free);

        ArrayList<User> users = new ArrayList<>();
        users.add(testUser1);
        //users.add(testUser2);
        //users.add(testUser3);
        //users.add(testUser4);

        gameEngine = GameEngine.instance();
        Game game = new Game(users, websocketService, cardAPIService);
        gameEngine.newGameSession(testUser1);

        stompSession.send("/app/gamesession-request/"+game.getGameId()+"/reject", null);
    }
}
