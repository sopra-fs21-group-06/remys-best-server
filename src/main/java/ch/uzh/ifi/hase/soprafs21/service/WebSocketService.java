package ch.uzh.ifi.hase.soprafs21.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * - Handles general WebSocket stuff (chat, reconnect)
 * - Provides utility functions for sending packets
 */
@Service
@Transactional
public class WebSocketService {

    Logger log = LoggerFactory.getLogger(WebSocketService.class);

    /*private final WaitingRoomService waitingRoomService;*/


    public WebSocketService() {

    }


    @Autowired
    public SimpMessagingTemplate simp;


    public void sendToPlayer(String identity, String path, Object dto) {
        this.simp.convertAndSendToUser(identity, path, dto);
    }
    public void sendToTopic(String path, Object dtO){
        this.simp.convertAndSend(path, dtO);
    }
}
