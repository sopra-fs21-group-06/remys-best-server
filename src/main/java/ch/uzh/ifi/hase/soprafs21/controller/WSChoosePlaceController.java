package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameChooseColorDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.WaitingRoomEnterDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomSendOutCurrentUsersDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.getIdentity;

@Controller
public class WSChoosePlaceController {
    Logger log = LoggerFactory.getLogger(WSChoosePlaceController.class);

    private final GameEngine gameEngine;
    private final WebSocketService webSocketService;

    public WSChoosePlaceController(GameEngine gameEngine, WebSocketService webSocketService) {
        this.gameEngine = gameEngine;
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/game/{gameId}/choose-color")
    @SendTo("/topic/game/{gameId}/colors")
    public synchronized WaitingRoomSendOutCurrentUsersDTO registerPlayer(@DestinationVariable String gameId, SimpMessageHeaderAccessor sha, GameChooseColorDTO gameChooseColorDTO) {
        log.info("Player " + getIdentity(sha) + ": Choose place (color) received");


        // map color to enum
        // assign color and player
        // send

        /*
        gameEngine.addUserToWaitingRoom(gameEngine.getUserService().getUserRepository().findByToken(gameChooseColorDTO.getToken()));
        WaitingRoomSendOutCurrentUsersDTO userToSendOut = new WaitingRoomSendOutCurrentUsersDTO();
        WaitingRoomSendOutCurrentUsersDTO userObjDTOList = gameEngine.createWaitingRoomUserList();
        log.info(userObjDTOList.toString());*/

        //this.webSocketService.sendToPlayer(getIdentity(sha), "user/queue/register", answer2);

        return null;
    }
}

