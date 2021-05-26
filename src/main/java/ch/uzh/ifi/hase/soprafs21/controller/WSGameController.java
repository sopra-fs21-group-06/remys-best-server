package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.ExecutePlayCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameCardExchange;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameReadyDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.GameEndDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomSendOutCurrentUsersDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;
import java.util.UUID;

import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.getIdentity;

@Controller
public class WSGameController {
    Logger log = LoggerFactory.getLogger(WSGameController.class);
    private final GameEngine gameEngine;
    private final WebSocketService webSocketService;
    private final UserService userService;

    public WSGameController(GameEngine gameEngine, WebSocketService webSocketService, UserService userService) {
        this.gameEngine = gameEngine;
        this.webSocketService = webSocketService;
        this.userService = userService;
    }

    @MessageMapping("/game/{gameId}/ready")
    public synchronized void ready(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, GameReadyDTO gameReadyDTO) {
        log.info("Player " + getIdentity(sha) + ": Ready for game received");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.setPlayerToReady(userService.convertTokenToUsername(gameReadyDTO.getToken()));
    }

    @MessageMapping("/game/{gameId}/leave")
    public synchronized void leaveGame(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha){
        User userLeaver = userService.getUserRepository().findBySessionIdentity(getIdentity(sha));
        GameEndDTO gameEndDTO = new GameEndDTO();
        gameEndDTO.setAborted(userLeaver.getUsername());
        webSocketService.broadcastGameEndMessage(gameId.toString(), gameEndDTO);
    }

    @MessageMapping("/game/{gameId}/card-exchange")
    public synchronized void cardExchange(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, GameCardExchange gameCardExchange){
        log.info("Player" + getIdentity(sha) + ": Has cardExchangePerformed");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.setCardCodeToExchange(userService.convertTokenToUsername(gameCardExchange.getToken()), gameCardExchange.getCode());
    }

    @MessageMapping("game/{gameId}/play")
    public synchronized void playMove(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, ExecutePlayCardDTO executePlayCardDTO) throws Exception {
        log.info("Player" + getIdentity(sha) + ":Has played");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.executeMove(executePlayCardDTO);
    }

    @EventListener
    public synchronized void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionIdentityOfPlayer = Objects.requireNonNull(event.getUser()).getName();
        if (sessionIdentityOfPlayer != null) {
            log.info("Player " + sessionIdentityOfPlayer + ": Connection lost");
            String username = userService.convertSessionIdentityToUser(sessionIdentityOfPlayer).getUsername();
            if(gameEngine.isUserInGameSession(username)){
                if(gameEngine.userIsHost(username)){
                    log.info("Player" + sessionIdentityOfPlayer + ":Has disconnected from GameSession as Host");
                    webSocketService.broadcastAbruptEndOfGameSessionMessage(gameEngine.findGameSessionIdByUsername(username), username);
                    gameEngine.deleteGameSessionByHostName(username);
                }else{
                    log.info("Player" + sessionIdentityOfPlayer + ":Has disconnected from GameSession as Player");
                    log.info(gameEngine.findGameSessionIdByUsername(username).toString());
                    log.info(userService.findByUsername(username).getUsername());
                    UUID gameSessionId = gameEngine.findGameSessionIdByUsername(username);
                    gameEngine.deleteUserFromGameSession(userService.findByUsername(username), gameSessionId);
                    webSocketService.broadcastAcceptedUsersInGameSession(gameSessionId);
                }
            }else if(gameEngine.userInWaitingRoom(username)){
                log.info("Player" + sessionIdentityOfPlayer + ":Has disconnected from waitingRoom");
                gameEngine.removeUserFromWaitingRoom(userService.findByUsername(username));
                WaitingRoomSendOutCurrentUsersDTO dto = gameEngine.createWaitingRoomUserList();
                webSocketService.broadcastPlayerDisconnectedFromWaitingRoom(dto);
            }else if(gameEngine.userInGame(username)) {
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

