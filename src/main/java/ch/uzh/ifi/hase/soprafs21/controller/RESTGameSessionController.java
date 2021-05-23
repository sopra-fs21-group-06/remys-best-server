package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameSessionHostDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.GameSessionIdDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class RESTGameSessionController {

    @PostMapping("create-gamesession")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public GameSessionIdDTO createNewGameSession(@RequestBody GameSessionHostDTO gameSessionHostDTO){
        User user = GameEngine.instance().getUserService().findByUsername(gameSessionHostDTO.getHostUsername());
        GameEngine.instance().newGameSession(user);
        return new GameSessionIdDTO(user.getUsername());
    }


}
