package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.moves.IMove;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.PossibleTargetFieldKeysListDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.RoundMarbleListDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.RoundMoveListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class RESTGameController {
    Logger log = LoggerFactory.getLogger(WSGameController.class);
    private final GameEngine gameEngine;
    private final UserService userService;
    private final  GameService gameService;


    public RESTGameController(GameEngine gameEngine, UserService userService, GameService gameService) {
        this.gameEngine = gameEngine;
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping("/game/{gameId}/moves")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoundMoveListDTO getMoves(@PathVariable UUID gameId, @RequestParam String code) {
        //log.info("Player" + getIdentity(sha) + ": Has made a moverequest");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        Player p = currentGame.getCurrentRound().getCurrentPlayer();

        Card card = new Card(code);
        List<CardMove> moves = new ArrayList<>();
        for(IMove move : card.getMoves()) {
            CardMove cardMove = new CardMove();
            cardMove.setMoveName(move.getName());
            moves.add(cardMove);
        }

        return DogUtils.generateRoundMoveListDTO(moves);
    }

    @GetMapping("/game/{gameId}/possible-marbles")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoundMarbleListDTO getPossibleMarbles(HttpServletRequest request, @PathVariable UUID gameId, @RequestParam String code, @RequestParam String moveName) {
        String token = request.getHeader("Authorization");
        Game currentGame = gameEngine.getRunningGameByID(gameId);

        Card card = new Card(code);
        String playerName = DogUtils.convertTokenToUsername(token, userService);

        List<Marble> marbleList = currentGame.getGameService().getPlayableMarble(playerName, card, moveName, currentGame);
        return DogUtils.generateRoundMarblesListDTO(marbleList);
    }

    @GetMapping("/game/{gameId}/possible-target-fields")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PossibleTargetFieldKeysListDTO getPossibleMarbles(@PathVariable UUID gameId, @RequestParam String code, @RequestParam String moveName, @RequestParam int marbleId) throws Exception {
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        Marble currentMarble = gameService.getMarbleByMarbleId(currentGame, marbleId);
        Card cardToPlay = new Card(code);
        List<String> targetFields = gameService.getPossibleTargetFields(currentMarble, moveName, cardToPlay, currentGame);
        return DogUtils.generatePossibleTargetFieldKeyListDTO(targetFields);
    }
}