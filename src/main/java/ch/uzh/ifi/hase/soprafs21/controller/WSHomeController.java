package ch.uzh.ifi.hase.soprafs21.controller;



import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
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
public class WSHomeController {

    Logger log = LoggerFactory.getLogger(WSHomeController.class);

    private final GameEngine gameEngine;

    public WSHomeController(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @MessageMapping("/home/register")
    public synchronized void registerUser(SimpMessageHeaderAccessor sha, HomeRegisterDTO homeRegisterDTO) {
        log.info("Player " + getIdentity(sha) + ": Entered HomeScreen");
        gameEngine.getUserService().updateUserIdentity(getIdentity(sha), homeRegisterDTO.getToken());
    }
    @MessageMapping("home/unregister")
    public synchronized void unregisterUser(SimpMessageHeaderAccessor sha, HomeRegisterDTO homeRegisterDTO){
        log.info("Player" + getIdentity(sha) + ": Left HomeScreen");
        gameEngine.getUserService().updateStatus(homeRegisterDTO.getToken(), UserStatus.Busy);
    }
}
