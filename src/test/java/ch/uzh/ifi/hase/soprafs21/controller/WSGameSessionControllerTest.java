package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.objects.GameSession;
import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameRequestDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameSessionLeaveDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WSGameSessionControllerTest extends AbstractWSControllerTest {

    @Autowired
    @MockBean
    private GameEngine gameEngine;

    @Autowired
    @MockBean
    WebSocketService websocketService;

    @MockBean
    CardAPIService cardAPIService;

/*    @Autowired
    @MockBean
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
        Mockito.doNothing().when(websocketService).broadcastInvitedUsersInGameSession(Mockito.any());
        Mockito.doNothing().when(websocketService).sendInvitationToHome(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(websocketService).sendGameSessionInvitedUserCounter(Mockito.any(), Mockito.any());


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

        //gameEngine = GameEngine.instance();
        Game game = new Game(users, websocketService, cardAPIService);
        gameEngine.newGameSession(testUser1);

        GameSession gameSession = new GameSession(testUser1, userService);
        doReturn(gameSession).when(gameEngine).findGameSessionByID(Mockito.any());

        Mockito.doNothing().when(websocketService).broadcastInvitedUsersInGameSession(Mockito.any());
        Mockito.doNothing().when(websocketService).broadcastAcceptedUsersInGameSession(Mockito.any());

        stompSession.send("/app/gamesession-request/"+game.getGameId()+"/reject", null);

/*        Mockito.doNothing().when(websocketService).broadcastInvitedUsersInGameSession(Mockito.any());
        Mockito.doNothing().when(websocketService).broadcastAcceptedUsersInGameSession(Mockito.any());*/
    }

    @Test
    void acceptInvitationTest() {

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

        //gameEngine = GameEngine.instance();
        Game game = new Game(users, websocketService, cardAPIService);
        gameEngine.newGameSession(testUser1);

        GameSession gameSession = new GameSession(testUser1, userService);

        //doReturn(testUser2).when(userService).convertSessionIdentityToUser(Mockito.any());
        doNothing().when(gameEngine).addUserToGameSession(Mockito.any(), Mockito.any());

        Mockito.doNothing().when(websocketService).broadcastInvitedUsersInGameSession(Mockito.any());
        Mockito.doNothing().when(websocketService).broadcastAcceptedUsersInGameSession(Mockito.any());

        stompSession.send("/app/gamesession-request/"+game.getGameId()+"/accept", null);

/*        Mockito.doNothing().when(websocketService).broadcastInvitedUsersInGameSession(Mockito.any());
        Mockito.doNothing().when(websocketService).broadcastAcceptedUsersInGameSession(Mockito.any());*/
    }

    @Test
    void fillUpTest() {

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

        //gameEngine = GameEngine.instance();
        Game game = new Game(users, websocketService, cardAPIService);
        gameEngine.newGameSession(testUser1);

        GameSession gameSession = new GameSession(testUser1, userService);

        /*given(gameEngine.findGameSessionByID(Mockito.any()))
                .willReturn(gameSession);*/

        doReturn(gameSession).when(gameEngine).findGameSessionByID(Mockito.any());
        doNothing().when(gameEngine).createGameFromGameSessionAndFillUp(Mockito.any());

        stompSession.send("/app/gamesession/"+game.getGameId()+"/fill-up", null);

        //doNothing().when(gameEngine).createGameFromGameSessionAndFillUp(Mockito.any());
    }
}
