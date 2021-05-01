package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.WaitingRoomUserObjDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.WaitingRoomEnterDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomSendOutCurrentUsersDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

import static org.mockito.BDDMockito.given;

//@Disabled("Disabled, needs additional work")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WSWaitingRoomControllerTest {

    WebSocketStompClient stompClient;

    @Value("${local.server.port}")
    private int port;

    /*
    @MockBean
    private UserService userService;*/

    @Autowired
    private GameEngine gameEngine;

    @MockBean
    private WebSocketService webSocketService;

    @BeforeEach
    void setup() {
        stompClient = new WebSocketStompClient(new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    // TODO reset Database after the test

    private WaitingRoomEnterDTO generateWaitingRoomEnterDTO(User user) {
        WaitingRoomEnterDTO waitingRoomEnterDTO = new WaitingRoomEnterDTO();
        waitingRoomEnterDTO.setToken(user.getToken());
        return waitingRoomEnterDTO;
    }

    private WaitingRoomSendOutCurrentUsersDTO generateWaitingRoomSendOutCurrentUsersDTO() {
        WaitingRoomSendOutCurrentUsersDTO waitingRoomSendOutCurrentUsersDTO = new WaitingRoomSendOutCurrentUsersDTO();
        List<WaitingRoomUserObjDTO> currentUsers = new ArrayList<>();
        WaitingRoomUserObjDTO waitingRoomUserObjDTO = new WaitingRoomUserObjDTO();
        currentUsers.add(waitingRoomUserObjDTO);
        waitingRoomSendOutCurrentUsersDTO.setCurrentUsers(currentUsers);

        return waitingRoomSendOutCurrentUsersDTO;
    }

    @Test
    void waitingRoomRegister() throws Exception {
        //given
        BlockingQueue<WaitingRoomSendOutCurrentUsersDTO> bq = new LinkedBlockingDeque<>();
        User user = new User();
        user.setUsername("iamsiddhantsahu");
        user.setPassword("abcd");
        user.setEmail("hello@siddhantsahu.com");

        //given(gameEngine.getUserService().updateUserIdentity(Mockito.any(), Mockito.any()));
        gameEngine = GameEngine.instance();
        gameEngine.getUserService().createUser(user);
        User testUser = gameEngine.getUserService().findByUsername(user.getUsername());
        WaitingRoomEnterDTO waitingRoomSample = generateWaitingRoomEnterDTO(testUser);
        WaitingRoomSendOutCurrentUsersDTO waitingRoomSendOutCurrentUsersDTO = generateWaitingRoomSendOutCurrentUsersDTO();

        StompSession session = stompClient.connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);

        session.subscribe("/topic/waiting-room", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return WaitingRoomSendOutCurrentUsersDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                bq.offer((WaitingRoomSendOutCurrentUsersDTO) payload);
            }
        });

        //System.out.println("waitingRoomSample");
        session.send("/app/waiting-room/register", waitingRoomSample);

        WaitingRoomSendOutCurrentUsersDTO response = bq.poll(2, TimeUnit.SECONDS);
        //assertion
        //System.out.println(response);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCurrentUsers().get(0).getUsername(), testUser.getUsername());
        Assertions.assertFalse(response.getCurrentUsers().isEmpty());
    }

    @Test
    void waitingRoomUnRegister() throws Exception {

        //given
        BlockingQueue<WaitingRoomSendOutCurrentUsersDTO> bq = new LinkedBlockingDeque<>();
        User user = new User();
        user.setUsername("iamsiddhantsahu");
        user.setPassword("abcd");
        user.setEmail("hello@siddhantsahu.com");

        //given(gameEngine.getUserService().updateUserIdentity(Mockito.any(), Mockito.any()));
        gameEngine = GameEngine.instance();
        gameEngine.getUserService().createUser(user);
        User testUser = gameEngine.getUserService().findByUsername(user.getUsername());
        WaitingRoomEnterDTO waitingRoomSample = generateWaitingRoomEnterDTO(testUser);
        WaitingRoomSendOutCurrentUsersDTO waitingRoomSendOutCurrentUsersDTO = generateWaitingRoomSendOutCurrentUsersDTO();

        StompSession session = stompClient.connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);

        session.subscribe("/topic/waiting-room", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return WaitingRoomSendOutCurrentUsersDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                bq.offer((WaitingRoomSendOutCurrentUsersDTO) payload);
            }
        });

        //System.out.println("waitingRoomSample");
        session.send("/app/waiting-room/unregister", waitingRoomSample);

        WaitingRoomSendOutCurrentUsersDTO response = bq.poll(2, TimeUnit.SECONDS);
        //assertion
        System.out.println(response);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getCurrentUsers().isEmpty());
    }

    //Blocking Queue poll method is giving issues.
/*    @Test
    void waitingRoomRegisterUserUnRegisterSameUser () throws Exception {

        //given
        BlockingQueue<WaitingRoomSendOutCurrentUsersDTO> bq = new LinkedBlockingDeque<>();
        //LinkedTransferQueue<WaitingRoomSendOutCurrentUsersDTO> bq = new LinkedTransferQueue<>();
        User user_1 = new User();
        user_1.setUsername("iamsiddhantsahu");
        user_1.setPassword("abcd");
        user_1.setEmail("hello@siddhantsahu.com");

        User user_2 = new User();
        user_2.setUsername("pascal");
        user_2.setPassword("frnjifer");
        user_2.setEmail("pascal@pascal.com");

        gameEngine = GameEngine.instance();
        gameEngine.getUserService().createUser(user_1);
        gameEngine.getUserService().createUser(user_2);

        User testUser_1 = gameEngine.getUserService().findByUsername(user_1.getUsername());
        User testUser_2 = gameEngine.getUserService().findByUsername(user_2.getUsername());

        WaitingRoomEnterDTO waitingRoomSample_1 = generateWaitingRoomEnterDTO(testUser_1);
        WaitingRoomEnterDTO waitingRoomSample_2 = generateWaitingRoomEnterDTO(testUser_2);

        WaitingRoomSendOutCurrentUsersDTO waitingRoomSendOutCurrentUsersDTO = generateWaitingRoomSendOutCurrentUsersDTO();

        StompSession session = stompClient.connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);

        session.subscribe("/topic/waiting-room", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return WaitingRoomSendOutCurrentUsersDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                bq.offer((WaitingRoomSendOutCurrentUsersDTO) payload);
            }
        });

        session.send("/app/waiting-room/register", waitingRoomSample_1);
        WaitingRoomSendOutCurrentUsersDTO response_1 = bq.poll(2, TimeUnit.SECONDS);
        session.send("/app/waiting-room/register", waitingRoomSample_2);

        //WaitingRoomSendOutCurrentUsersDTO response_1 = bq.poll(2, TimeUnit.SECONDS);
        //response = bq.poll(2, TimeUnit.SECONDS);
        WaitingRoomSendOutCurrentUsersDTO response_2 = bq.poll(10, TimeUnit.SECONDS);

        //Assertion
        Assertions.assertTrue(response_2.getCurrentUsers().size() == 2);
        //Assertions.
    }*/
}
