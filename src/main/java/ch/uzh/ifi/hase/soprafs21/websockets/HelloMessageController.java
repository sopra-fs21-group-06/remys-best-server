package ch.uzh.ifi.hase.soprafs21.websockets;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = "*")
public class HelloMessageController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public HelloMessageDTO greeting(HelloMessageDTO message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return message;
    }
}
