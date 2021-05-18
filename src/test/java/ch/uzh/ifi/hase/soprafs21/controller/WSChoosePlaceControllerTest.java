package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameChooseColorDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomChooseColorDTO;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WSChoosePlaceControllerTest extends AbstractWSControllerTest {

    @Autowired
    private GameEngine gameEngine;

    private User user1;
    private User user2;
    private User user3;
    private User user4;

    @BeforeEach
    public void setup() {
        super.setup();
        user1 = createTestUser("User 1", "user1@gmail.com");
        user2 = createTestUser("User 2", "user2@gmail.com");
        user3 = createTestUser("User 3", "user3@gmail.com");
        user4 = createTestUser("User 4", "user4@gmail.com");
    }

    private GameChooseColorDTO generateGameChooseColorDTO(User testUser) {
        GameChooseColorDTO gameChooseColorDTO = new GameChooseColorDTO();
        gameChooseColorDTO.setColor("RED");
        gameChooseColorDTO.setToken(testUser.getToken());
        return gameChooseColorDTO;
    }

    @Test
    void registerPlayerSuccess() throws Exception {
        //given
        BlockingQueue<WaitingRoomChooseColorDTO> bq = new LinkedBlockingDeque<>();

        gameEngine = GameEngine.instance();
        gameEngine.addUserToWaitingRoom(user1);
        gameEngine.addUserToWaitingRoom(user2);
        gameEngine.addUserToWaitingRoom(user3);
        gameEngine.addUserToWaitingRoom(user4);
        Game game = gameEngine.getRunningGamesList().get(0);

        GameChooseColorDTO gameChooseColorDTO = generateGameChooseColorDTO(user1);

        StompSession session = stompClient.connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);

        session.subscribe("/topic/game/"+game.getGameId()+"/colors", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return WaitingRoomChooseColorDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                bq.offer((WaitingRoomChooseColorDTO) payload);
            }
        });

        session.send("/app/game/"+game.getGameId()+"/choose-color", gameChooseColorDTO);

        WaitingRoomChooseColorDTO response = bq.poll(2, TimeUnit.SECONDS);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getPlayers().get(0).getColor().toString(), gameChooseColorDTO.getColor());
    }
}
