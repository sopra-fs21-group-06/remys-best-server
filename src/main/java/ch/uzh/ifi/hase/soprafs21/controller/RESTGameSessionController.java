package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.objects.GameSession;
import ch.uzh.ifi.hase.soprafs21.rest.dto.CreateGameSession.GameSessionIdDTO;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RESTGameSessionController {
    Logger log = LoggerFactory.getLogger(WSGameController.class);
    private final GameEngine gameEngine;
    private final UserService userService;

    public RESTGameSessionController(GameEngine gameEngine, UserService userService) {
        this.gameEngine = gameEngine;
        this.userService = userService;
    }

    @GetMapping("/create-gamesession")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameSessionIdDTO createGameSession(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        User newHost = userService.getUserRepository().findByToken(token);

        gameEngine.newGameSession(newHost);

        newHost.setStatus(UserStatus.Busy);
        userService.getUserRepository().saveAndFlush(newHost);

        GameSession gameSession = gameEngine.findGameSessionByHostName(newHost.getUsername());

        GameSessionIdDTO gameSessionIdDTO = new GameSessionIdDTO();
        gameSessionIdDTO.setGameSessionId(gameSession.getID());
        return gameSessionIdDTO;
    }

}
