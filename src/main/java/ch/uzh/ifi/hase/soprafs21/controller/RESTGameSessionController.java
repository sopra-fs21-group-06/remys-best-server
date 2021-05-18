package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameRequestAcceptDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameSessionHostDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.GameSessionIdDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.GameSessionUserListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping("gamesession/{gameSessionID}/users")
    @ResponseStatus(HttpStatus.FOUND)
    @ResponseBody
    public GameSessionUserListDTO getGameSessionUsers(@PathVariable UUID gameSessionID){
        return DogUtils.convertPlayersToGameSessionUserListDTO(GameEngine.instance().getUsersByGameSessionId(gameSessionID));
    }
}
