package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleExecuteCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.ExecutePlayCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameCardExchange;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameReadyDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WSGameControllerTest extends AbstractWSControllerTest {

    @Value("${local.server.port}")
    public int port;

    @Autowired
    @MockBean
    GameEngine gameEngine;

    @MockBean
    WebSocketService websocketService;

    @MockBean
    CardAPIService cardAPIService;

    @MockBean
    Game game;

    private GameReadyDTO generateGameReadyDTO(String token) {
        GameReadyDTO gameReadyDTO = new GameReadyDTO();
        gameReadyDTO.setToken(token);
        return gameReadyDTO;
    }

    private ExecutePlayCardDTO generateExecutePlayCardDTO() {
        ExecutePlayCardDTO executePlayCardDTO = new ExecutePlayCardDTO();
        executePlayCardDTO.setCode("2D");
        executePlayCardDTO.setToken("qwerty");
        executePlayCardDTO.setMoveName("2 Forwards");

        List<MarbleExecuteCardDTO> marbles = new ArrayList<>();
        marbles.add(new MarbleExecuteCardDTO(2, "dummy"));
        marbles.add(new MarbleExecuteCardDTO(4, "dummy2"));

        executePlayCardDTO.setMarbles(marbles);
        return executePlayCardDTO;
    }

    @Test
    void testReady() {
        User user = createTestUser("sid", "hello@siddhantsahu.com");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Game currentRunningGame = new Game(users, websocketService, cardAPIService);

        given(gameEngine.getRunningGameByID(currentRunningGame.getGameId())).willReturn(currentRunningGame);

        GameReadyDTO gameReadyDTO = generateGameReadyDTO(user.getToken());

        stompSession.send("/app/game/" + currentRunningGame.getGameId().toString() + "/ready", gameReadyDTO);

        doNothing().when(game).setPlayerToReady(Mockito.anyString());
    }

    @Test
    void testLeave() {
        User user = createTestUser("sid", "hello@siddhantsahu.com");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Game currentRunningGame = new Game(users, websocketService, cardAPIService);

        given(gameEngine.getRunningGameByID(currentRunningGame.getGameId())).willReturn(currentRunningGame);

        GameReadyDTO gameReadyDTO = generateGameReadyDTO(user.getToken());

        stompSession.send("/app/game/" + currentRunningGame.getGameId().toString() + "/leave", gameReadyDTO);

        doNothing().when(websocketService).broadcastGameEndMessage(Mockito.anyString(), Mockito.any());
    }

    @Test
    void testCardExchange() {
        User user = createTestUser("sid", "hello@siddhantsahu.com");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Game currentRunningGame = new Game(users, websocketService, cardAPIService);

        given(gameEngine.getRunningGameByID(currentRunningGame.getGameId())).willReturn(currentRunningGame);

        GameCardExchange gameCardExchange = new GameCardExchange();
        gameCardExchange.setCode("2D");
        gameCardExchange.setToken(user.getToken());

        stompSession.send("/app/game/" + currentRunningGame.getGameId().toString() + "/card-exchange", gameCardExchange);

        doNothing().when(game).setCardCodeToExchange(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void testPlayMove() throws Exception {
        User user = createTestUser("sid", "hello@siddhantsahu.com");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Game currentRunningGame = new Game(users, websocketService, cardAPIService);

        given(gameEngine.getRunningGameByID(currentRunningGame.getGameId())).willReturn(currentRunningGame);

        ExecutePlayCardDTO executePlayCardDTO = generateExecutePlayCardDTO();

        stompSession.send("/app/game/" + currentRunningGame.getGameId().toString() + "/play", executePlayCardDTO);

        doNothing().when(game).executeMove(Mockito.any());
    }
}
