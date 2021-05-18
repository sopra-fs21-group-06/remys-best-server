package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleExecuteCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.ExecutePlayCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameCardExchange;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameReadyDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.WaitingRoomEnterDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomSendOutCurrentUsersDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WSGameControllerTest {

    @Value("${local.server.port}")
    public int port;

    public WebSocketStompClient stompClient;

    @Autowired
    @MockBean
    GameEngine gameEngine;

    @MockBean
    WebSocketService websocketService;

    @MockBean
    CardAPIService cardAPIService;

    @MockBean
    Game game;

    @MockBean
    UserService userService;

    @BeforeEach
    void setup() {
        stompClient = new WebSocketStompClient(new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

/*    @AfterEach
    void resetUserRepository() {
        gameEngine.getUserService().getUserRepository().deleteAll();

        List<User> usersInWaitingRoom = gameEngine.getWaitingRoom().getUserQueue();
        Iterator<User> iterator = usersInWaitingRoom.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }*/

    private GameReadyDTO generateGameReadyDTO(String token) {
        GameReadyDTO gameReadyDTO = new GameReadyDTO();
        gameReadyDTO.setToken(token);
        return gameReadyDTO;
    }

    private GameCardExchange genearateGameCardExchangeDTO(String token, String code) {
        GameCardExchange gameCardExchange = new GameCardExchange();
        gameCardExchange.setCode(code);
        gameCardExchange.setToken(token);
        return gameCardExchange;
    }

    private ExecutePlayCardDTO generateExecutePlayCardDTO() {
        ExecutePlayCardDTO executePlayCardDTO = new ExecutePlayCardDTO();
        executePlayCardDTO.setCode("2D");
        executePlayCardDTO.setToken("qwerty");
        executePlayCardDTO.setMoveName("2 Forwards");

        List<MarbleExecuteCardDTO> marbles = new ArrayList<>();
        marbles.add(new MarbleExecuteCardDTO(2, "dumy"));
        marbles.add(new MarbleExecuteCardDTO(4, "dumy2"));

        executePlayCardDTO.setMarbles(marbles);
        return executePlayCardDTO;
    }

    @Test
    void gameReadyTest() throws Exception {
        //given
        //BlockingQueue<WaitingRoomSendOutCurrentUsersDTO> bq = new LinkedBlockingDeque<>();
        //User user = createTestUser("user1", "user1@siddhantsahu.com");
        User user = new User();
        user.setUsername("Siddhant");
        user.setToken("abcd");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Game currentRunningGame = new Game(users, websocketService, cardAPIService);
        //String gameID = currentRunningGame.getGameId();

        given(gameEngine.getRunningGameByID(currentRunningGame.getGameId())).willReturn(currentRunningGame);
        given(userService.convertTokenToUsername(Mockito.any())).willReturn(user.getUsername());
        //doNothing().when(Game).setPlayerToReady(user.getUsername());
        //verify(Game, times(1)).setPlayerToReady(user.getUsername());

        //given(gameEngine.getUserService().updateUserIdentity(Mockito.any(), Mockito.any()));
        //gameEngine = GameEngine.instance();
        //gameEngine.getUserService().createUser(user);
        //User testUser = gameEngine.getUserService().findByUsername(user.getUsername());
        //WaitingRoomEnterDTO waitingRoomSample = generateWaitingRoomEnterDTO(testUser);
        //WaitingRoomSendOutCurrentUsersDTO waitingRoomSendOutCurrentUsersDTO = generateWaitingRoomSendOutCurrentUsersDTO();

        GameReadyDTO gameReadyDTO = generateGameReadyDTO(user.getToken());

        StompSession session = stompClient.connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);

        /*session.subscribe("/topic/waiting-room", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return WaitingRoomSendOutCurrentUsersDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                bq.offer((WaitingRoomSendOutCurrentUsersDTO) payload);
            }
        });*/

        //System.out.println("waitingRoomSample");
        session.send("/app/game/" + currentRunningGame.getGameId().toString() + "/ready", gameReadyDTO);

        //verify(currentRunningGame, times(1)).setPlayerToReady(user.getUsername());
        //verify(gameEngine, times(1)).getRunningGameByID(currentRunningGame.getGameId());

        //WaitingRoomSendOutCurrentUsersDTO response = bq.poll(2, TimeUnit.SECONDS);
        //assertion
        //System.out.println(response);
        //Assertions.assertNotNull(response);
        //Assertions.assertEquals(testUser.getUsername(), response.getCurrentUsers().get(0).getUsername());
        //Assertions.assertFalse(response.getCurrentUsers().isEmpty());
        //session.send("/app/waiting-room/unregister", waitingRoomSample);
    }

    @Test
    void cardExchangeTest() throws Exception {
        //given
        //BlockingQueue<WaitingRoomSendOutCurrentUsersDTO> bq = new LinkedBlockingDeque<>();
        //User user = createTestUser("user1", "user1@siddhantsahu.com");
        User user = new User();
        user.setUsername("Siddhant2");
        user.setToken("1234");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Game currentRunningGame = new Game(users, websocketService, cardAPIService);
        //String gameID = currentRunningGame.getGameId();

        given(gameEngine.getRunningGameByID(currentRunningGame.getGameId())).willReturn(currentRunningGame);
        given(userService.convertTokenToUsername(Mockito.any())).willReturn(user.getUsername());
        //doNothing().when(Game).setPlayerToReady(user.getUsername());
        //verify(Game, times(1)).setPlayerToReady(user.getUsername());

        //given(gameEngine.getUserService().updateUserIdentity(Mockito.any(), Mockito.any()));
        //gameEngine = GameEngine.instance();
        //gameEngine.getUserService().createUser(user);
        //User testUser = gameEngine.getUserService().findByUsername(user.getUsername());
        //WaitingRoomEnterDTO waitingRoomSample = generateWaitingRoomEnterDTO(testUser);
        //WaitingRoomSendOutCurrentUsersDTO waitingRoomSendOutCurrentUsersDTO = generateWaitingRoomSendOutCurrentUsersDTO();

        GameCardExchange gameCardExchange = genearateGameCardExchangeDTO(user.getToken(), "2D");

        StompSession session = stompClient.connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);

        /*session.subscribe("/topic/waiting-room", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return WaitingRoomSendOutCurrentUsersDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                bq.offer((WaitingRoomSendOutCurrentUsersDTO) payload);
            }
        });*/

        //System.out.println("waitingRoomSample");
        session.send("/app/game/" + currentRunningGame.getGameId().toString() + "/card-exchange", gameCardExchange);

        //verify(currentRunningGame, times(1)).setPlayerToReady(user.getUsername());
        //verify(gameEngine, times(1)).getRunningGameByID(currentRunningGame.getGameId());

        //WaitingRoomSendOutCurrentUsersDTO response = bq.poll(2, TimeUnit.SECONDS);
        //assertion
        //System.out.println(response);
        //Assertions.assertNotNull(response);
        //Assertions.assertEquals(testUser.getUsername(), response.getCurrentUsers().get(0).getUsername());
        //Assertions.assertFalse(response.getCurrentUsers().isEmpty());
        //session.send("/app/waiting-room/unregister", waitingRoomSample);
    }

    @Test
    void playMoveTest() throws Exception {
        //given
        //BlockingQueue<WaitingRoomSendOutCurrentUsersDTO> bq = new LinkedBlockingDeque<>();
        //User user = createTestUser("user1", "user1@siddhantsahu.com");
        User user = new User();
        user.setUsername("Siddhant3");
        user.setToken("qwerty");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Game currentRunningGame = new Game(users, websocketService, cardAPIService);
        //String gameID = currentRunningGame.getGameId();

        given(gameEngine.getRunningGameByID(currentRunningGame.getGameId())).willReturn(currentRunningGame);
        //given(userService.convertTokenToUsername(Mockito.any())).willReturn(user.getUsername());
        //doNothing().when(Game).setPlayerToReady(user.getUsername());
        //verify(Game, times(1)).setPlayerToReady(user.getUsername());

        //given(gameEngine.getUserService().updateUserIdentity(Mockito.any(), Mockito.any()));
        //gameEngine = GameEngine.instance();
        //gameEngine.getUserService().createUser(user);
        //User testUser = gameEngine.getUserService().findByUsername(user.getUsername());
        //WaitingRoomEnterDTO waitingRoomSample = generateWaitingRoomEnterDTO(testUser);
        //WaitingRoomSendOutCurrentUsersDTO waitingRoomSendOutCurrentUsersDTO = generateWaitingRoomSendOutCurrentUsersDTO();

        ExecutePlayCardDTO executePlayCardDTO = generateExecutePlayCardDTO();

        StompSession session = stompClient.connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);

        /*session.subscribe("/topic/waiting-room", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return WaitingRoomSendOutCurrentUsersDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                bq.offer((WaitingRoomSendOutCurrentUsersDTO) payload);
            }
        });*/

        //System.out.println("waitingRoomSample");
        session.send("/app/game/" + currentRunningGame.getGameId().toString() + "/play", executePlayCardDTO);

        //verify(currentRunningGame, times(1)).setPlayerToReady(user.getUsername());
        //verify(gameEngine, times(1)).getRunningGameByID(currentRunningGame.getGameId());

        //WaitingRoomSendOutCurrentUsersDTO response = bq.poll(2, TimeUnit.SECONDS);
        //assertion
        //System.out.println(response);
        //Assertions.assertNotNull(response);
        //Assertions.assertEquals(testUser.getUsername(), response.getCurrentUsers().get(0).getUsername());
        //Assertions.assertFalse(response.getCurrentUsers().isEmpty());
        //session.send("/app/waiting-room/unregister", waitingRoomSample);
    }

}
