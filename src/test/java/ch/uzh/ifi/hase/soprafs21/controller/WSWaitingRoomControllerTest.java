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
        List<WaitingRoomUserObjDTO> currentUsers = new List<WaitingRoomUserObjDTO>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<WaitingRoomUserObjDTO> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(WaitingRoomUserObjDTO waitingRoomUserObjDTO) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends WaitingRoomUserObjDTO> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends WaitingRoomUserObjDTO> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public WaitingRoomUserObjDTO get(int index) {
                return null;
            }

            @Override
            public WaitingRoomUserObjDTO set(int index, WaitingRoomUserObjDTO element) {
                return null;
            }

            @Override
            public void add(int index, WaitingRoomUserObjDTO element) {

            }

            @Override
            public WaitingRoomUserObjDTO remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<WaitingRoomUserObjDTO> listIterator() {
                return null;
            }

            @Override
            public ListIterator<WaitingRoomUserObjDTO> listIterator(int index) {
                return null;
            }

            @Override
            public List<WaitingRoomUserObjDTO> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
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

        System.out.println("waitingRoomSample");
        session.send("/app/waiting-room/register", waitingRoomSample);

        WaitingRoomSendOutCurrentUsersDTO response = bq.poll(2, TimeUnit.SECONDS);
        //assertion
        System.out.println(response);
        Assertions.assertNotNull(response);
    }

/*    @Test
    void waitingRoomUnRegister() throws Exception {

    }*/

}
