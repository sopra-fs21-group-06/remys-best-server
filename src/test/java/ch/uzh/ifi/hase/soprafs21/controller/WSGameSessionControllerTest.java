package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.objects.GameSession;
import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WSGameSessionControllerTest extends AbstractWSControllerTest {

    @Autowired
    private GameEngine gameEngine;

    @Autowired
    @MockBean
    WebSocketService websocketService;

    @MockBean
    CardAPIService cardAPIService;

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
        User testUser1 = createTestUser("abcd_sid", "hello@abcd_sid.com");
        User testUser2 = createTestUser("efgh_sid", "hello@efgh_sid.com");

        GameSession gameSession = gameEngine.newGameSession(testUser1);
        GameRequestDTO gameRequestDTO = generateGameRequestDTO(testUser2.getToken(), testUser2.getUsername());

        stompSession.send("/app/gamesession/"+gameSession.getID()+"/invite", gameRequestDTO);
    }

    @Test
    void userLeavesGameSessionTest() {
        User testUser1 = createTestUser("abcd_sid", "hello@abcd_sid.com");
        User testUser2 = createTestUser("efgh_sid", "hello@efgh_sid.com");

        GameSession gameSession = gameEngine.newGameSession(testUser1);
        gameEngine.addUserToGameSession(testUser2, gameSession.getID());
        GameSessionLeaveDTO gameSessionLeaveDTO = generateGameSessionLeaveDTO(testUser2.getToken());

        stompSession.send("/app/gamesession/"+gameSession.getID()+"/leave", gameSessionLeaveDTO);
    }

    @Test
    void rejectInvitationTest() {
        User testUser1 = createTestUser("abcd_sid", "hello@abcd_sid.com");

        GameSession gameSession = gameEngine.newGameSession(testUser1);

        Mockito.doNothing().when(websocketService).broadcastInvitedUsersInGameSession(Mockito.any());
        Mockito.doNothing().when(websocketService).broadcastAcceptedUsersInGameSession(Mockito.any());

        stompSession.send("/app/gamesession-request/"+gameSession.getID()+"/reject", null);
    }

    @Test
    void acceptInvitationTest() {
        User testUser1 = createTestUser("abcd_sid", "hello@abcd_sid.com");
        User testUser2 = createTestUser("efgh_sid", "hello@efgh_sid.com");

        GameSession gameSession = gameEngine.newGameSession(testUser1);

        Mockito.doNothing().when(websocketService).broadcastInvitedUsersInGameSession(Mockito.any());
        Mockito.doNothing().when(websocketService).broadcastAcceptedUsersInGameSession(Mockito.any());

        stompSession.send("/app/gamesession-request/"+gameSession.getID()+"/accept", null);
    }

    @Test
    void fillUpTest() {
        User testUser1 = createTestUser("abcd_sid", "hello@abcd_sid.com");

        GameSession gameSession = gameEngine.newGameSession(testUser1);

        stompSession.send("/app/gamesession/"+gameSession.getID()+"/fill-up", null);
    }
}
