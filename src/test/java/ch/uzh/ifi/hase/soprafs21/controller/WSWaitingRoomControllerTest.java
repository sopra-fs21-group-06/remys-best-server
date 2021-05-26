package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.WaitingRoomEnterDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomSendOutCurrentUsersDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WSWaitingRoomControllerTest extends AbstractWSControllerTest {

    @Autowired
    private GameEngine gameEngine;

    @BeforeEach
    void resetUserRepository() throws Exception {
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

    @Test
    void waitingRoomRegister() throws Exception {
        //given
        User user = createTestUser("user1", "user1@siddhantsahu.com");
        gameEngine = GameEngine.instance();
        WaitingRoomEnterDTO waitingRoomSample = generateWaitingRoomEnterDTO(user);

        BlockingQueue<WaitingRoomSendOutCurrentUsersDTO> bq = setupBlockingQueue(WaitingRoomSendOutCurrentUsersDTO.class, "/topic/waiting-room");
        stompSession.send("/app/waiting-room/register", waitingRoomSample);
        WaitingRoomSendOutCurrentUsersDTO response = bq.poll(2, TimeUnit.SECONDS);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(user.getUsername(), response.getCurrentUsers().get(0).getUsername());
        Assertions.assertFalse(response.getCurrentUsers().isEmpty());
    }

    @Test
    void waitingRoomUnRegister() throws Exception {
        User user = createTestUser("hahahaha", "hahahah@siddhantsahu.com");
        gameEngine = GameEngine.instance();
        WaitingRoomEnterDTO waitingRoomSample = generateWaitingRoomEnterDTO(user);

        BlockingQueue<WaitingRoomSendOutCurrentUsersDTO> bq = setupBlockingQueue(WaitingRoomSendOutCurrentUsersDTO.class, "/topic/waiting-room");
        stompSession.send("/app/waiting-room/unregister", waitingRoomSample);
        WaitingRoomSendOutCurrentUsersDTO response = bq.poll(2, TimeUnit.SECONDS);

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getCurrentUsers().isEmpty());
    }
}
