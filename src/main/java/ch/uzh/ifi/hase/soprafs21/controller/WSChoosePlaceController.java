package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.objects.Player;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameChooseColorDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomChooseColorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.getIdentity;

@Controller
public class WSChoosePlaceController {
    Logger log = LoggerFactory.getLogger(WSChoosePlaceController.class);

    private final GameEngine gameEngine;
    private final WebSocketService webSocketService;

    public WSChoosePlaceController(GameEngine gameEngine, WebSocketService webSocketService) {
        this.gameEngine = gameEngine;
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/game/{gameId}/choose-color")
    @SendTo("/topic/game/{gameId}/colors")
    public synchronized WaitingRoomChooseColorDTO registerPlayer(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, GameChooseColorDTO gameChooseColorDTO) {
        log.info("Player " + getIdentity(sha) + ": Choose place (color) received");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        List<Player> updatedPlayers = currentGame.updatePlayerColor(DogUtils.convertTokenToUsername(gameChooseColorDTO.getToken(), gameEngine.getUserService()), Color.fromId(gameChooseColorDTO.getColor()));
        log.info(updatedPlayers.toString());
        return DogUtils.convertPlayersToWaitingRoomChooseColorDTO(updatedPlayers);
    }
}

