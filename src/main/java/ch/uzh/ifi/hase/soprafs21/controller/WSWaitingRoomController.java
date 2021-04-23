package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.WaitingRoomEnterDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomSendOutCurrentUsersDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.getIdentity;

@Controller
public class WSWaitingRoomController {
    Logger log = LoggerFactory.getLogger(WSWaitingRoomController.class);

    private final GameEngine gameEngine;
    private final WebSocketService webSocketService;

    public WSWaitingRoomController(GameEngine gameEngine, WebSocketService webSocketService) {
        this.gameEngine = gameEngine;
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/waiting-room/register")
    @SendTo("/topic/waiting-room")
    public synchronized WaitingRoomSendOutCurrentUsersDTO registerPlayer(SimpMessageHeaderAccessor sha, WaitingRoomEnterDTO waitingRoomEnterDTO) {
        log.info("Player " + getIdentity(sha) + ": Message received");
        gameEngine.addUserToWaitingRoom(gameEngine.getUserService().getUserRepository().findByToken(waitingRoomEnterDTO.getToken()));
        WaitingRoomSendOutCurrentUsersDTO userToSendOut = new WaitingRoomSendOutCurrentUsersDTO();
        WaitingRoomSendOutCurrentUsersDTO userObjDTOList = gameEngine.createWaitingRoomUserList();
        log.info(userObjDTOList.toString());

        //this.webSocketService.sendToPlayer(getIdentity(sha), "user/queue/register", answer2);

        return userObjDTOList;
    }
}

