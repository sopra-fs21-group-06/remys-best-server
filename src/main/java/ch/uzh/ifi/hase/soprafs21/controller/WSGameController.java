package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.ExecutePlayCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameCardExchange;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameReadyDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.GameEndDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

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
}

