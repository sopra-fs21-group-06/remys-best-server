package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleExecuteCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.getIdentity;

@Controller
public class WSGameController {
    Logger log = LoggerFactory.getLogger(WSGameController.class);

    private final GameEngine gameEngine;
    private final WebSocketService webSocketService;

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

    @MessageMapping("game/{gameId}/play")
    public synchronized void playMove(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, ExecutePlayCardDTO executePlayCardDTO){
        log.info("Player" + getIdentity(sha) + ":Has played");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        //process marble-request and sendout via for example sendGameExecutedcard(String , UUID gameId) from gameservice.
        //This method is implemented in the websocketservice already.

        // TODO which function??
        // TODO where is next turn triggered?
        // TODO target field for selected move & marble?


        //For testing purposes
        List<MarbleExecuteCardDTO> marbleExecuteCardDTOList = new ArrayList<>();
        MarbleExecuteCardDTO marbleExecuteCardDTO1 = new MarbleExecuteCardDTO(0, 16, Color.BLUE);
        marbleExecuteCardDTOList.add(marbleExecuteCardDTO1);
        webSocketService.sendGameExecutedCard("a", "AH", marbleExecuteCardDTOList, gameId);
    }
}

