package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractWSControllerTest {

    @Value("${local.server.port}")
    public int port;

    @Autowired
    public UserService userService;

    public StompSession stompSession;

    @BeforeEach
    public void setup() throws Exception {
        userService.getUserRepository().deleteAll();
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompSession = stompClient.connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);
    }

    @AfterEach
    public void tearDown() {
        this.stompSession.disconnect();
    }

    public User createTestUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password");
        user.setSessionIdentity(UUID.randomUUID().toString());
        user.setToken(UUID.randomUUID().toString());
        userService.createUser(user);
        return user;
    }

    public <T> BlockingQueue<T> setupBlockingQueue(Class<T> payloadType, String pathToSubscribe) {
        BlockingQueue<T> bq = new LinkedBlockingDeque<>();

        stompSession.subscribe(pathToSubscribe, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return payloadType;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                bq.offer(payloadType.cast(payload));
            }
        });

        return bq;
    }
}
