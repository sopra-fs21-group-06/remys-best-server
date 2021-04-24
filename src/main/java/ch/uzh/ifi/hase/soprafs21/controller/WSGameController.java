package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.CardMoveRequestDTO;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameCardExchange;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameReadyDTO;
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

    public WSGameController(GameEngine gameEngine, WebSocketService webSocketService) {
        this.gameEngine = gameEngine;
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/game/{gameId}/ready")
    public synchronized void ready(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, GameReadyDTO gameReadyDTO) {
        log.info("Player " + getIdentity(sha) + ": Ready for game received");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.setPlayerToReady(DogUtils.convertTokenToUsername(gameReadyDTO.getToken(), gameEngine.getUserService()));
    }

    @MessageMapping("/game/{gameId}/card-exchange")
    public synchronized void cardExchange(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, GameCardExchange gameCardExchange){
        log.info("Player" + getIdentity(sha) + ": Has cardExchangePerformed");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.setCardExhange(DogUtils.convertTokenToUsername(gameCardExchange.getToken(), gameEngine.getUserService()), gameCardExchange.getCode());
    }


    @MessageMapping("/game/{gameId}/move-request")
    public synchronized void moveRequest(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, CardMoveRequestDTO cardMoveRequestDTO){
        log.info("Player" + getIdentity(sha) + ": Has made a moverequest");
        Game currentGame = gameEngine.getRunningGameByID(gameId);

    }
}

