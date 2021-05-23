package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.objects.GameSession;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameRequestDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameSessionLeaveDTO;
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

    @MessageMapping("/gamesession/{gamesessionId}/invite")
    public void inviteUser(@DestinationVariable UUID gamesessionId, SimpMessageHeaderAccessor sha, GameRequestDTO gameRequestDTO) {
        try{
            log.info("Player " + getIdentity(sha) + ": Made an invitation to " + gameRequestDTO.getUsername());
            User invitedUser = userService.findByUsername(gameRequestDTO.getUsername());
            if(invitedUser.getStatus() != UserStatus.Free){
                String invitationSenderSessionIdentity = getIdentity(sha);
                webSocketService.sendGameSessionInviteError(invitationSenderSessionIdentity);
                return;
            }

            userService.updateStatus(invitedUser.getToken(), UserStatus.Busy);

            gameEngine.addInvitedUserToGameSession(invitedUser, gamesessionId);
            webSocketService.broadcastInvitedUsersInGameSession(gamesessionId);
            webSocketService.sendInvitationToHome(gamesessionId, invitedUser.getSessionIdentity());

            // periodically
            webSocketService.sendGameSessionInvitedUserCounter(gamesessionId, invitedUser);
        }
        catch (Exception e){
            log.info(e.toString());
        }
    }

    @MessageMapping("/gamesession/{gamesessionId}/leave")
    public synchronized void userLeavesGameSession(@DestinationVariable UUID gamesessionId, SimpMessageHeaderAccessor sha, GameSessionLeaveDTO gameSessionLeaveDTO){
        log.info("Player " + getIdentity(sha) + ": Left the gameSession");
        GameSession currentGameSession = gameEngine.findGameSessionByID(gamesessionId);
        User userLeaver = userService.getUserRepository().findByToken(gameSessionLeaveDTO.getToken());
        if(gameEngine.userIsHost(userLeaver.getUsername())){
            webSocketService.broadcastGameSessionEndMessage(gamesessionId.toString(), userLeaver.getUsername());
            for(User u: currentGameSession.getAcceptedUsers()){
                userService.updateStatus(u.getToken(), UserStatus.Free);
            }
            gameEngine.deleteGameSession(gamesessionId);
        }
        else{
            userService.updateStatus(userLeaver.getToken(), UserStatus.Free);
            if (currentGameSession != null) {
                currentGameSession.deleteAcceptedUser(userLeaver);
            }
            webSocketService.broadcastAcceptedUsersInGameSession(gamesessionId);
        }
    }

    @MessageMapping("/gamesession/{gamesessionId}/fill-up")
    public synchronized void fillUpGameSession(@DestinationVariable UUID gamesessionId, SimpMessageHeaderAccessor sha) {
        log.info("Player" + getIdentity(sha) + ": Triggered gameSession Fill-Up");
        GameSession currentGameSession = gameEngine.findGameSessionByID(gamesessionId);
        gameEngine.createGameFromGameSessionAndFillUp(currentGameSession);
    }


    @MessageMapping("/gamesession-request/{gamesessionId}/accept")
    public synchronized void acceptInvitation(@DestinationVariable UUID gamesessionId, SimpMessageHeaderAccessor sha){
        log.info("Player" + getIdentity(sha) + ": Accepted invitation");
        User userWhichHasAccepted = userService.convertSessionIdentityToUser(getIdentity(sha));
        GameEngine.instance().addUserToGameSession(userWhichHasAccepted, gamesessionId);
        webSocketService.broadcastInvitedUsersInGameSession(gamesessionId);
        webSocketService.broadcastAcceptedUsersInGameSession(gamesessionId);
    }

    @MessageMapping("/gamesession-request/{gamesessionId}/reject")
    public synchronized void rejectInvitation(@DestinationVariable UUID gamesessionId, SimpMessageHeaderAccessor sha){
        log.info("Player" + getIdentity(sha) + ": Accepted rejected");
        User userWhichHasRejected = userService.convertSessionIdentityToUser(getIdentity(sha));
        gameEngine.findGameSessionByID(gamesessionId).deleteInvitedUser(userWhichHasRejected);
        webSocketService.broadcastInvitedUsersInGameSession(gamesessionId);
        webSocketService.broadcastAcceptedUsersInGameSession(gamesessionId);
    }
}
