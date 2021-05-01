package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.GameEndDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleExecuteCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.convertSessionIdentityToUserName;
import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.getIdentity;

@Controller
public class WSGameController {
    Logger log = LoggerFactory.getLogger(WSGameController.class);
    private final GameEngine gameEngine;
    private final WebSocketService webSocketService;
    private SessionDisconnectEvent sessionDisconnectEvent;

    public WSGameController(GameEngine gameEngine, WebSocketService webSocketService) {
        this.gameEngine = gameEngine;
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/game/{gameId}/ready")
    public synchronized void ready(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, GameReadyDTO gameReadyDTO) {
        log.info("Player " + getIdentity(sha) + ": Ready for game received");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.setPlayerToReady(DogUtils.convertTokenToUsername(gameReadyDTO.getToken(), gameEngine.getUserService()));
    }

    @MessageMapping("/game/{gameId}/card-exchange")
    public synchronized void cardExchange(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, GameCardExchange gameCardExchange){
        log.info("Player" + getIdentity(sha) + ": Has cardExchangePerformed");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.setCardExchange(DogUtils.convertTokenToUsername(gameCardExchange.getToken(), gameEngine.getUserService()), gameCardExchange.getCode());
    }

    @MessageMapping("/game/{gameId}/move-request")
    public synchronized void moveRequest(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, CardMoveRequestDTO cardMoveRequestDTO){
        log.info("Player" + getIdentity(sha) + ": Has made a moverequest");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        Player p = currentGame.getCurrentRound().getCurrentPlayer();
        gameEngine.getGameService().canPlay(p, currentGame);

        Card card = new Card(cardMoveRequestDTO.getCode());
        List<String> moveNames = currentGame.getGameService().sendCardMove(card);
        List<CardMove> moves = new ArrayList<>();
        for(String moveName : moveNames) {
            CardMove cardMove = new CardMove();
            cardMove.setMoveName(moveName);
            moves.add(cardMove);
        }

        webSocketService.sendMovesToPlayer(getIdentity(sha), moves, gameId);
    }

    @MessageMapping("game/{gameId}/marble-request")
    public synchronized void marbleRequest(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, MoveMarbleRequestDTO moveMarbleRequestDTO){
        log.info("Player" + getIdentity(sha) + ":Has made marblerequest");
        Game currentGame = gameEngine.getRunningGameByID(gameId);

        Card card = new Card(moveMarbleRequestDTO.getCode());
        List<Marble> marbleList = currentGame.getGameService().getPlayableMarble(card, moveMarbleRequestDTO.getMoveName(), currentGame);
        webSocketService.sendMarblesToPlayer(getIdentity(sha), marbleList, gameId);
    }

    // TODO target-field-list
    // List<String> possibleTargetFieldKeys = currentGame.getGameService().getPossibleFields(marble, moveName, game)

    @MessageMapping("game/{gameId}/play")
    public synchronized void playMove(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, ExecutePlayCardDTO executePlayCardDTO){
        log.info("Player" + getIdentity(sha) + ":Has played");
        Game currentGame = gameEngine.getRunningGameByID(gameId);


        currentGame.sendExecutedMove(executePlayCardDTO);
        currentGame.getCurrentRound().sendOutCurrentTurnDTO();
        currentGame.sendOutCurrentTurnFactsDTO();

    }


    @MessageMapping("game/{gameId}/target-fields-request")
    public synchronized void targetFieldRequest(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, GamePossibleTargetFieldRequestDTO gamePossibleTargetFieldRequestDTO){
        log.info("Player" + getIdentity(sha) + ":Has requested TargetFieldList");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.sendOutTargetFieldList(gamePossibleTargetFieldRequestDTO);
        /*List<String> dummyTargetField= new ArrayList<>();
        dummyTargetField.add("4GREEN");
        dummyTargetField.add("10BLUE");
        dummyTargetField.add("14YELLOW");
        dummyTargetField.add("8RED");
        webSocketService.sendTargetFieldListMessage(getIdentity(sha),dummyTargetField, currentGame.getGameId());
        */
    }


    //target-fields-list


    // List<String> getpossibleField(String moveName, Marble marble)



    @EventListener
    public synchronized void handleSessionDisconnect(SessionDisconnectEvent event) {
        String p = Objects.requireNonNull(event.getUser()).getName();
        if (p != null) {
            log.info("Player " + p + ": Connection lost");
            SimpMessageHeaderAccessor header = SimpMessageHeaderAccessor.wrap(event.getMessage());
            GameEndDTO dto = new GameEndDTO();
            String username = convertSessionIdentityToUserName(p,gameEngine.getUserService());
            dto.setAborted(username);
            gameEngine.deleteGameByGameID(gameEngine.findGameIdByPlayerName(username));
            log.info(String.valueOf(gameEngine.getRunningGamesList().size()));
            webSocketService.sentGameEndMessage(gameEngine.findGameIdByPlayerName(username).toString(), dto);
            log.info(username);
        }
    }
}

