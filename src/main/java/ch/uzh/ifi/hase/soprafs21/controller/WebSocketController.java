package ch.uzh.ifi.hase.soprafs21.controller;



import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.HomeRegisterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.getIdentity;

/**
 * Provides general WebSocket endpoints and handles session disconnects
 */
@Controller
public class WebSocketController {

    Logger log = LoggerFactory.getLogger(WebSocketController.class);

    private final GameEngine gameEngine;

    public WebSocketController(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }


    @MessageMapping("/register")
    public synchronized void registerPlayer(SimpMessageHeaderAccessor sha, HomeRegisterDTO homeRegisterDTO) {
        log.info("Player " + getIdentity(sha) + ": Message received");
        gameEngine.getUserService().updateUserIdentity(getIdentity(sha), homeRegisterDTO.getToken());

    }
}
