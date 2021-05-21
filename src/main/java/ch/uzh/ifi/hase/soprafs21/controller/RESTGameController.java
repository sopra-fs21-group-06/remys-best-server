package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.moves.IMove;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement.PossibleMarblesDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement.PossibleTargetFieldsDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement.RemainingSevenMovesDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement.SevenMovesDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.PossibleTargetFieldKeysListDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.RoundMarbleListDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.RoundMoveListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.Boolean.FALSE;

@RestController
public class RESTGameController {
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
        Card card = new Card(code);
        List<CardMove> moves = new ArrayList<>();
        for(IMove move : card.getMoves()) {
            CardMove cardMove = new CardMove(move.getName());
            moves.add(cardMove);
        }
        return DogUtils.generateRoundMoveListDTO(moves);
    }

    @PostMapping("/game/{gameId}/possible-marbles")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoundMarbleListDTO getPossibleMarbles(HttpServletRequest request, @PathVariable UUID gameId, @RequestBody PossibleMarblesDTO possibleMarblesDTO) throws Exception {
        String token = request.getHeader("Authorization");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        Card card = new Card(possibleMarblesDTO.getCode());
        String playerName = userService.convertTokenToUsername(token);
        ArrayList<MarbleIdAndTargetFieldKey> sevenMoves = DogUtils.generateMarbleIdsAndTargetFieldKeys(possibleMarblesDTO.getSevenMoves());
        List<Marble> marbleList = currentGame.getGameService().getPlayableMarbles(currentGame, playerName, card, possibleMarblesDTO.getMoveName(), sevenMoves);
        return DogUtils.generateRoundMarblesListDTO(marbleList);
    }

    @PostMapping("/game/{gameId}/possible-target-fields")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PossibleTargetFieldKeysListDTO getPossibleTargetFields(@PathVariable UUID gameId, @RequestBody PossibleTargetFieldsDTO possibleTargetFieldsDTO) throws Exception {
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        Marble currentMarble = gameService.getMarbleByMarbleId(currentGame, possibleTargetFieldsDTO.getMarbleId());
        Card cardToPlay = new Card(possibleTargetFieldsDTO.getCode());
        ArrayList<MarbleIdAndTargetFieldKey> sevenMoves = DogUtils.generateMarbleIdsAndTargetFieldKeys(possibleTargetFieldsDTO.getSevenMoves());
        List<String> targetFields = gameService.getPossibleTargetFields(currentGame, currentMarble, possibleTargetFieldsDTO.getMoveName(), cardToPlay, sevenMoves);
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
