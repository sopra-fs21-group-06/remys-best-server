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
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameRequestAcceptDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameRequestDeniedDTO;
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
            User invitedUser = userService.getUserRepository().findByUsername(gameRequestDTO.getUsername());
            String sessionIdentityInvitedUser = invitedUser.getSessionIdentity();
            if(invitedUser.getStatus() != UserStatus.Free){
                webSocketService.sendGameSessionInviteError(sessionIdentityInvitedUser);
                return;
            }

            userService.updateStatus(userService.convertUserNameToToken(invitedUser.getUsername()), UserStatus.Busy);

            GameSession currentGameSession = gameEngine.findGameSessionByID(gamesessionId);

            gameEngine.addInvitedUserToGameSession(invitedUser, gamesessionId);
            webSocketService.sendGameSessionInvitation(gamesessionId,  userService.convertUserNameToSessionIdentity(gameRequestDTO.getUsername()), userService.convertTokenToUsername(gameRequestDTO.getToken()));
            webSocketService.broadcastGameSessionInvitedUserList(gamesessionId, currentGameSession.getInvitedUsers());
            webSocketService.sendGameSessionInvitedUserCounter(currentGameSession, invitedUser, sessionIdentityInvitedUser);}
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
            for(User u: currentGameSession.getUserList()){
                userService.updateStatus(u.getToken(), UserStatus.Free);
            }
            gameEngine.deleteGameSession(gamesessionId);
        }
        else{
            userService.updateStatus(userLeaver.getToken(), UserStatus.Free);
            if (currentGameSession != null) {
                currentGameSession.deleteUser(userLeaver);
            }
            webSocketService.broadcastUsersInGameSession(gamesessionId);
        }
    }

    @MessageMapping("/gamesession/{gameSessionId}/fill-up")
    public synchronized void fillUpGameSession(@DestinationVariable UUID gameSessionId, SimpMessageHeaderAccessor sha) {
        log.info("Player" + getIdentity(sha) + ": Triggered gameSession Fill-Up");
        GameSession currentGameSession = gameEngine.findGameSessionByID(gameSessionId);
        gameEngine.createGameFromGameSessionAndFillUp(currentGameSession);
    }


    @MessageMapping("/game-session-request/{gameSessionId}/accept")
    public synchronized void acceptInvitation(@DestinationVariable UUID gameSessionId, SimpMessageHeaderAccessor sha, GameRequestAcceptDTO dto){
        log.info("Player" + getIdentity(sha) + ": Accepted invitation");
        String username = DogUtils.convertSessionIdentityToUserName(getIdentity(sha), GameEngine.instance().getUserService());
        try {
            GameEngine.instance().findGameSessionByID(gameSessionId).deleteInvitedUser(GameEngine.instance().getUserService().findByUsername(username));
            GameEngine.instance().addUserToSession(GameEngine.instance().getUserService().findByUsername(username), gameSessionId);
            GameEngine.instance().getUserService().findByUsername(username).setStatus(UserStatus.Busy);
            wait(1000);
            webSocketService.broadcastUsersInGameSession(gameSessionId);
            webSocketService.broadcastGameSessionInvitedUserList(gameSessionId,gameEngine.findGameSessionByID(gameSessionId).getInvitedUsers());
        }catch(NullPointerException | InterruptedException e){
            log.info("Something went wrong whilst accepting the invitation");
        }
    }

    @MessageMapping("/game-session-request/{gameSessionId}/reject")
    public synchronized void rejectInvitation(@DestinationVariable UUID gameSessionId, SimpMessageHeaderAccessor sha, GameRequestDeniedDTO dto){
        if(gameSessionId!=null){
            String username = DogUtils.convertSessionIdentityToUserName(getIdentity(sha), GameEngine.instance().getUserService());
            gameEngine.findGameSessionByID(gameSessionId).deleteInvitedUser(userService.findByUsername(username));
            webSocketService.broadcastUsersInGameSession(gameSessionId);
            webSocketService.broadcastGameSessionInvitedUserList(gameSessionId,gameEngine.findGameSessionByID(gameSessionId).getInvitedUsers());
            GameEngine.instance().getUserService().findByUsername(username).setStatus(UserStatus.Free);
        }

    }
}
