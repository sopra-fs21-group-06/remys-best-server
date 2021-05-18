package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.WaitingRoomUserObjDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.WaitingRoomEnterDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomSendOutCurrentUsersDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

//@Disabled("Disabled, needs additional work")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WSWaitingRoomControllerTest extends AbstractWSControllerTest {

    @Autowired
    private GameEngine gameEngine;

    @BeforeEach
    void resetUserRepository() {
        List<User> usersInWaitingRoom = gameEngine.getWaitingRoom().getUserQueue();
        Iterator<User> iterator = usersInWaitingRoom.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        super.setup();
    }

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
        User user = createTestUser("user1", "user1@siddhantsahu.com");

        gameEngine = GameEngine.instance();
        WaitingRoomEnterDTO waitingRoomSample = generateWaitingRoomEnterDTO(user);

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
        Assertions.assertEquals(user.getUsername(), response.getCurrentUsers().get(0).getUsername());
        Assertions.assertFalse(response.getCurrentUsers().isEmpty());
        //session.send("/app/waiting-room/unregister", waitingRoomSample);
    }

    @Test
    void waitingRoomUnRegister() throws Exception {

        //given
        BlockingQueue<WaitingRoomSendOutCurrentUsersDTO> bq = new LinkedBlockingDeque<>();
        User user = createTestUser("hahahaha", "hahahah@siddhantsahu.com");

        gameEngine = GameEngine.instance();
        WaitingRoomEnterDTO waitingRoomSample = generateWaitingRoomEnterDTO(user);

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
