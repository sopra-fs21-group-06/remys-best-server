package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameRequestDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.HomeRegisterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.getIdentity;

@Controller
public class WSGameSessionController {

    Logger log = LoggerFactory.getLogger(WSGameController.class);
    private final GameEngine gameEngine;
    private final WebSocketService webSocketService;
    private final UserService userService;

    public WSGameSessionController(GameEngine gameEngine, WebSocketService webSocketService, UserService userService) {
        this.gameEngine = gameEngine;
        this.webSocketService = webSocketService;
        this.userService = userService;
    }

    @MessageMapping("/gameSession/{gameSessionId}/invite")
    public void inviteUser(SimpMessageHeaderAccessor sha, GameRequestDTO gameRequestDTO) {
        try{
            log.info("Player " + getIdentity(sha) + ": Message received");
            User invitedUser = userService.getUserRepository().findByUsername(gameRequestDTO.getUsername());
            String sessionIdentityInvitedUser = invitedUser.getSessionIdentity();
            userService.updateStatus(DogUtils.convertUserNameToToken(invitedUser.getUsername(), userService), UserStatus.BUSY);
            gameEngine.addinvitedUserToGameSession(invitedUser, gameRequestDTO.getGameSessionId());
            webSocketService.sendGameSessionInvitedUserList(gameRequestDTO.getGameSessionId(), gameEngine.findGameSessionByID(gameRequestDTO.getGameSessionId()).getInvitedUsers());
            webSocketService.sendGameSessionInvitedUserCounter(gameEngine.findGameSessionByID(gameRequestDTO.getGameSessionId()), invitedUser.getUsername(), sessionIdentityInvitedUser);}
        catch (Exception e){
            log.info(e.toString());
        }
    }
}
