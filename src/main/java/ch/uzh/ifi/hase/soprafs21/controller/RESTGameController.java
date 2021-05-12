package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.moves.IMove;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement.RemainingSevenMovesDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement.SevenMovesDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.PossibleTargetFieldKeysListDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.RoundMarbleListDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.RoundMoveListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController

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
    public RoundMarbleListDTO getPossibleMarbles(HttpServletRequest request, @PathVariable UUID gameId, @RequestParam String code, @RequestParam String moveName, @RequestParam int remainingSevenMoves) throws Exception {
        String token = request.getHeader("Authorization");
        Game currentGame = gameEngine.getRunningGameByID(gameId);

        Card card = new Card(code);
        String playerName = userService.convertTokenToUsername(token);

        List<Marble> marbleList = currentGame.getGameService().getPlayableMarble(currentGame, playerName, card, moveName, remainingSevenMoves);
        return DogUtils.generateRoundMarblesListDTO(marbleList);
    }

    @GetMapping("/game/{gameId}/possible-target-fields")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PossibleTargetFieldKeysListDTO getPossibleTargetFields(@PathVariable UUID gameId, @RequestParam String code, @RequestParam String moveName, @RequestParam int marbleId,  @RequestParam int remainingSevenMoves) throws Exception {
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        Marble currentMarble = gameService.getMarbleByMarbleId(currentGame, marbleId);
        Card cardToPlay = new Card(code);
        List<String> targetFields = gameService.getPossibleTargetFields(currentGame, currentMarble, moveName, cardToPlay, remainingSevenMoves);
        return DogUtils.generatePossibleTargetFieldKeyListDTO(targetFields);
    }

    @GetMapping("/game/{gameId}/throw-away")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public List<String> loginUser(@PathVariable UUID gameId, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        Player player = gameEngine.findPlayerbyUsername(gameEngine.getRunningGameByID(gameId), userService.convertTokenToUsername(token));
        return gameEngine.getGameService().canPlay(player, currentGame);
    }

    @PostMapping("/game/{gameId}/remaining-seven-moves")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RemainingSevenMovesDTO getRemainingSevenMoves(@PathVariable UUID gameId, @RequestBody SevenMovesDTO sevenMovesDTO) {
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKeys = DogUtils.generateMarbleIdsAndTargetFieldKeys(sevenMovesDTO.getSevenMoves());
        int remainingSevenMoves = currentGame.getGameService().getRemainingSevenMoves(currentGame, marbleIdsAndTargetFieldKeys);
        return DogUtils.generateRemainingSevenMovesDTO(remainingSevenMoves);
    }
}
