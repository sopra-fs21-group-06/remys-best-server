package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameChooseColorDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomChooseColorDTO;
import org.junit.jupiter.api.Assertions;
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
        User user_1 = createTestUser("iamsiddhantsahu", "hello@siddhantsahu.com");
        User user_2 = createTestUser("pascal", "pascal@pascal.com");

        //WaitingRoom waitingRoom = new WaitingRoom();
        //waitingRoom.addUser(user_1);
        //waitingRoom.addUser(user_2);

        gameEngine = GameEngine.instance();
        gameEngine.getUserService().createUser(user_1);
        gameEngine.getUserService().createUser(user_2);
        gameEngine.addUserToWaitingRoom(user_1);
        gameEngine.addUserToWaitingRoom(user_2);
        Game game = gameEngine.getRunningGamesList().get(0);
        //gameEngine.getRunningGameByID(game.getGameId());
        User testUser = gameEngine.getUserService().findByUsername(user_1.getUsername());

        //ChooseColorPlayerDTO chooseColorPlayerDTO = generateChoosePlayerDTO(testUser);
        GameChooseColorDTO gameChooseColorDTO = generateGameChooseColorDTO(testUser);

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
