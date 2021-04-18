package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.service.LoginService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.WaitingRoomEnterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.getIdentity;

/**
 * Provides general WebSocket endpoints and handles session disconnects
 */
@Controller
public class WebSocketController {

    Logger log = LoggerFactory.getLogger(WebSocketController.class);

    private final LoginService loginService;
    private final WebSocketService webSocketService;

    public WebSocketController(LoginService loginService, WebSocketService webSocketService) {
        this.loginService = loginService;
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/register")
    @SendTo("/topic/register")
    public synchronized WaitingRoomEnterDTO registerPlayer(SimpMessageHeaderAccessor sha) {
        log.info("Player " + getIdentity(sha) + ": Message received");
        WaitingRoomEnterDTO answer = new WaitingRoomEnterDTO();
        answer.setExample("Test");
        WaitingRoomEnterDTO answer2 = new WaitingRoomEnterDTO();
        answer2.setExample("Test2");

        this.webSocketService.sendToPlayer(getIdentity(sha), "user/queue/register", answer2 );
        return answer;

    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        Principal p = event.getUser();
        if (p != null) {
            log.info("Player " + p.getName() + ": Connection lost");

            //do something with this information
        }
    }
}
