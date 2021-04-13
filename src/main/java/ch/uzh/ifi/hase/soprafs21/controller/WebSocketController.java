package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.service.LoginService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.WaitingRoomDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Objects;

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
    @SendTo("/queue/register")
    public synchronized WaitingRoomDTO registerPlayer(SimpMessageHeaderAccessor sha) {
        log.info("Player " + getIdentity(sha) + ": Message received");
        WaitingRoomDTO answer = new WaitingRoomDTO();
        answer.setExample("Test");
        WaitingRoomDTO answer2 = new WaitingRoomDTO();
        answer.setExample("Test2");

        webSocketService.sendToPlayer(getIdentity(sha), "/queue/register", answer2 );
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
