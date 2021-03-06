package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
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
    private final UserService userService;

    public WSWaitingRoomController(GameEngine gameEngine, UserService userService) {
        this.gameEngine = gameEngine;
        this.userService = userService;
    }

    @MessageMapping("/waiting-room/register")
    @SendTo("/topic/waiting-room")
    public synchronized WaitingRoomSendOutCurrentUsersDTO registerPlayer(SimpMessageHeaderAccessor sha, WaitingRoomEnterDTO waitingRoomEnterDTO) {
        log.info("Player " + getIdentity(sha) + ": Message received");
        userService.updateUserIdentity(getIdentity(sha), waitingRoomEnterDTO.getToken());
        userService.updateStatus(waitingRoomEnterDTO.getToken(), UserStatus.Busy);
        log.info("Player " + userService.convertTokenToUsername(waitingRoomEnterDTO.getToken()));
        gameEngine.addUserToWaitingRoom(userService.findByToken(waitingRoomEnterDTO.getToken()));
        WaitingRoomSendOutCurrentUsersDTO userObjDTOList = gameEngine.createWaitingRoomUserList();
        log.info(userObjDTOList.toString());

        return userObjDTOList;
    }

    @MessageMapping("/waiting-room/unregister")
    @SendTo("/topic/waiting-room")
    public synchronized WaitingRoomSendOutCurrentUsersDTO unregisterPlayer(SimpMessageHeaderAccessor sha, WaitingRoomEnterDTO waitingRoomEnterDTO) {
        log.info("Player " + getIdentity(sha) + ": Message received");
        userService.updateStatus(waitingRoomEnterDTO.getToken(), UserStatus.Free);
        gameEngine.removeUserFromWaitingRoom(gameEngine.getUserService().getUserRepository().findByToken(waitingRoomEnterDTO.getToken()));
        WaitingRoomSendOutCurrentUsersDTO userObjDTOList = gameEngine.createWaitingRoomUserList();
        log.info(userObjDTOList.toString());
        return userObjDTOList;
    }
}

