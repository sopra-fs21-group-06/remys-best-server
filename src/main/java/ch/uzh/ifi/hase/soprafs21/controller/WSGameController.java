package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.moves.IMove;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.GameEndDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.convertSessionIdentityToUserName;
import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.getIdentity;

@Controller
public class WSGameController {
    Logger log = LoggerFactory.getLogger(WSGameController.class);
    private final GameEngine gameEngine;
    private final WebSocketService webSocketService;
    private final UserService userService;

    public WSGameController(GameEngine gameEngine, WebSocketService webSocketService, UserService userService) {
        this.gameEngine = gameEngine;
        this.webSocketService = webSocketService;
        this.userService = userService;
    }

    @MessageMapping("/game/{gameId}/ready")
    public synchronized void ready(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, GameReadyDTO gameReadyDTO) {
        log.info("Player " + getIdentity(sha) + ": Ready for game received");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.setPlayerToReady(DogUtils.convertTokenToUsername(gameReadyDTO.getToken(), userService));
    }

    @MessageMapping("/game/{gameId}/card-exchange")
    public synchronized void cardExchange(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, GameCardExchange gameCardExchange){
        log.info("Player" + getIdentity(sha) + ": Has cardExchangePerformed");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.setCardExchange(DogUtils.convertTokenToUsername(gameCardExchange.getToken(), userService), gameCardExchange.getCode());
    }

    @MessageMapping("/game/{gameId}/move-request")
    public synchronized void moveRequest(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, CardMoveRequestDTO cardMoveRequestDTO){
        log.info("Player" + getIdentity(sha) + ": Has made a moverequest");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        Player p = currentGame.getCurrentRound().getCurrentPlayer();

        Card card = new Card(cardMoveRequestDTO.getCode());
        List<CardMove> moves = new ArrayList<>();
        for(IMove move : card.getMoves()) {
            CardMove cardMove = new CardMove();
            cardMove.setMoveName(move.getName());
            moves.add(cardMove);
        }

        webSocketService.sendMovesToPlayer(getIdentity(sha), moves, gameId);
    }

    @MessageMapping("game/{gameId}/marble-request")
    public synchronized void marbleRequest(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, MoveMarbleRequestDTO moveMarbleRequestDTO){
        log.info("Player" + getIdentity(sha) + ":Has made marblerequest");
        Game currentGame = gameEngine.getRunningGameByID(gameId);

        Card card = new Card(moveMarbleRequestDTO.getCode());
        String playerName = DogUtils.convertTokenToUsername(moveMarbleRequestDTO.getToken(), userService);

        List<Marble> marbleList = currentGame.getGameService().getPlayableMarble(playerName, card, moveMarbleRequestDTO.getMoveName(), currentGame);
        webSocketService.sendMarblesToPlayer(getIdentity(sha), marbleList, gameId);
    }

    @MessageMapping("game/{gameId}/target-fields-request")
    public synchronized void targetFieldRequest(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, GamePossibleTargetFieldRequestDTO gamePossibleTargetFieldRequestDTO){
        log.info("Player" + getIdentity(sha) + ":Has requested TargetFieldList");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.sendOutTargetFieldList(gamePossibleTargetFieldRequestDTO);
    }

    @MessageMapping("game/{gameId}/play")
    public synchronized void playMove(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, ExecutePlayCardDTO executePlayCardDTO){
        log.info("Player" + getIdentity(sha) + ":Has played");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.sendExecutedMove(executePlayCardDTO);
        currentGame.getCurrentRound().sendOutCurrentTurnDTO();
        currentGame.sendOutCurrentTurnFactsDTO();
    }

    @EventListener
    public synchronized void handleSessionDisconnect(SessionDisconnectEvent event) {
        String p = Objects.requireNonNull(event.getUser()).getName();
        if (p != null) {
            log.info("Player " + p + ": Connection lost");
            SimpMessageHeaderAccessor header = SimpMessageHeaderAccessor.wrap(event.getMessage());
            GameEndDTO dto = new GameEndDTO();
            String username = convertSessionIdentityToUserName(p,userService);
            dto.setAborted(username);
            log.info(username);
            webSocketService.sentGameEndMessage(gameEngine.findGameIdByPlayerName(username).toString(), dto);
            gameEngine.deleteGameByGameID(gameEngine.findGameIdByPlayerName(username));
            log.info(username);
        }
    }
}

