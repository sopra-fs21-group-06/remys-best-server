package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.objects.GameSession;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameRequestDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameSessionLeaveDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.HomeRegisterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

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
    public void inviteUser(@DestinationVariable UUID gameSessionId, SimpMessageHeaderAccessor sha, GameRequestDTO gameRequestDTO) {
        try{
            log.info("Player " + getIdentity(sha) + ": Made an invitation to" + gameRequestDTO.getUsername());
            User invitedUser = userService.getUserRepository().findByUsername(gameRequestDTO.getUsername());
            String sessionIdentityInvitedUser = invitedUser.getSessionIdentity();

            userService.updateStatus(DogUtils.convertUserNameToToken(invitedUser.getUsername(), userService), UserStatus.BUSY);

            GameSession currentGameSession = gameEngine.findGameSessionByID(gameSessionId);

            gameEngine.addinvitedUserToGameSession(invitedUser, gameSessionId);
            webSocketService.sendGameSessionInvitedUserList(gameSessionId, currentGameSession.getInvitedUsers());
            webSocketService.sendGameSessionInvitedUserCounter(currentGameSession, invitedUser.getUsername(), sessionIdentityInvitedUser);}
        catch (Exception e){
            log.info(e.toString());
        }
    }

    @MessageMapping("/gameSession/{gameSessionId}/leave")
    public void userLeavesGameSession(@DestinationVariable UUID gameSessionId, SimpMessageHeaderAccessor sha, GameSessionLeaveDTO gameSessionLeaveDTO){
        log.info("Player " + getIdentity(sha) + ": Left the gameSession");
        GameSession currentGameSession = gameEngine.findGameSessionByID(gameSessionId);
        User userLeaver = userService.getUserRepository().findByToken(gameSessionLeaveDTO.getToken());
        if(gameEngine.userIsHost(userLeaver.getUsername())){
            webSocketService.sentGameSessionEndMessage(gameSessionId.toString(), userLeaver.getUsername());
            for(User u: currentGameSession.getUserList()){
                userService.updateStatus(u.getToken(), UserStatus.FREE);
            }
            gameEngine.deleteGameSession(gameSessionId);
        }
        else{
            userService.updateStatus(userLeaver.getToken(), UserStatus.FREE);
            currentGameSession.deleteUser(userLeaver);
            //webSocketService.sendCurrentUserListOutAgain
        }
    }
}
