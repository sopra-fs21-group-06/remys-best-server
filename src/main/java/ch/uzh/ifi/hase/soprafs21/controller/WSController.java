package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.GameEndDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomSendOutCurrentUsersDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;
import java.util.UUID;

@Controller
public class WSController {

    Logger log = LoggerFactory.getLogger(WSController.class);
    private final GameEngine gameEngine;
    private final WebSocketService webSocketService;
    private final UserService userService;

    public WSController(GameEngine gameEngine, WebSocketService webSocketService, UserService userService) {
        this.gameEngine = gameEngine;
        this.webSocketService = webSocketService;
        this.userService = userService;
    }

    @EventListener
    public synchronized void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionIdentityOfPlayer = Objects.requireNonNull(event.getUser()).getName();
        if (sessionIdentityOfPlayer != null) {
            log.info("Player " + sessionIdentityOfPlayer + ": Connection lost");
            String username = userService.convertSessionIdentityToUser(sessionIdentityOfPlayer).getUsername();
            if (gameEngine.isUserInGameSession(username)){
                if (gameEngine.userIsHost(username)){
                    log.info("Player" + sessionIdentityOfPlayer + ":Has disconnected from GameSession as Host");
                    webSocketService.broadcastAbruptEndOfGameSessionMessage(gameEngine.findGameSessionIdByUsername(username), username);
                    gameEngine.deleteGameSessionByHostName(username);
                } else{
                    log.info("Player" + sessionIdentityOfPlayer + ":Has disconnected from GameSession as Player");
                    log.info(gameEngine.findGameSessionIdByUsername(username).toString());
                    log.info(userService.findByUsername(username).getUsername());
                    UUID gameSessionId = gameEngine.findGameSessionIdByUsername(username);
                    gameEngine.deleteUserFromGameSession(userService.findByUsername(username), gameSessionId);
                    webSocketService.broadcastAcceptedUsersInGameSession(gameSessionId);
                }
            } else if(gameEngine.userInWaitingRoom(username)){
                log.info("Player" + sessionIdentityOfPlayer + ":Has disconnected from waitingRoom");
                gameEngine.removeUserFromWaitingRoom(userService.findByUsername(username));
                WaitingRoomSendOutCurrentUsersDTO dto = gameEngine.createWaitingRoomUserList();
                webSocketService.broadcastPlayerDisconnectedFromWaitingRoom(dto);
            } else if(gameEngine.userInGame(username)) {
                log.info("Player" + sessionIdentityOfPlayer + ":Has disconnected from game");
                GameEndDTO dto = new GameEndDTO();
                dto.setAborted(username);
                webSocketService.broadcastGameEndMessage(gameEngine.findGameIdByPlayerName(username).toString(), dto);
                gameEngine.deleteGameByGameID(gameEngine.findGameIdByPlayerName(username));
            }
            DogUtils.resetStatusAndSessionIdentity(gameEngine.getUserService(), username);
        }
    }
}
