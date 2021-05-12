package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.ExecutePlayCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameCardExchange;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.GameReadyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

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
        currentGame.setPlayerToReady(userService.convertTokenToUsername(gameReadyDTO.getToken()));
    }

    @MessageMapping("/game/{gameId}/card-exchange")
    public synchronized void cardExchange(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, GameCardExchange gameCardExchange){
        log.info("Player" + getIdentity(sha) + ": Has cardExchangePerformed");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.setCardExchange(userService.convertTokenToUsername(gameCardExchange.getToken()), gameCardExchange.getCode());
    }

    @MessageMapping("game/{gameId}/play")
    public synchronized void playMove(@DestinationVariable UUID gameId, SimpMessageHeaderAccessor sha, ExecutePlayCardDTO executePlayCardDTO){
        log.info("Player" + getIdentity(sha) + ":Has played");
        Game currentGame = gameEngine.getRunningGameByID(gameId);
        currentGame.sendExecutedMove(executePlayCardDTO);
        currentGame.getGameService().checkEndTurnAndEndRound(currentGame);
        currentGame.getCurrentRound().sendOutCurrentTurnDTO();
        currentGame.sendOutCurrentTurnFactsDTO();
    }
/*
   @EventListener
    public synchronized void handleSessionDisconnect(SessionDisconnectEvent event) {
        String p = Objects.requireNonNull(event.getUser()).getName();
        if (p != null) {
            log.info("Player " + p + ": Connection lost");
            SimpMessageHeaderAccessor header = SimpMessageHeaderAccessor.wrap(event.getMessage());
            String username = convertSessionIdentityToUserName(p,gameEngine.getUserService());

            //status change with users
            //sessionIdentity null
            //token null
            if(gameEngine.isUserInGameSession(username)){
                if(gameEngine.userIsHost(username)){
                    //deletes gameSession and redirects users
                    //need an empty DTO
                    DogUtils.resetStatusTokenAndSessionIdentity(gameEngine.getUserService(), username);
                }else{
                    //deletes user from gameSession and notifies users
                    //gameEngine.deleteUserFromSession(username);
                    DogUtils.resetStatusTokenAndSessionIdentity(gameEngine.getUserService(), username);
                }
            }else if(gameEngine.userInWaitingRoom(username)){
                // user deleted from waitingRoom;
                //send list of current waiting users
                DogUtils.resetStatusTokenAndSessionIdentity(gameEngine.getUserService(), username);
            }else if(gameEngine.userInGame(username)) {
                GameEndDTO dto = new GameEndDTO();

                dto.setAborted(username);
                webSocketService.sentGameEndMessage(gameEngine.findGameIdByPlayerName(username).toString(), dto);
                gameEngine.deleteGameByGameID(gameEngine.findGameIdByPlayerName(username));
                DogUtils.resetStatusTokenAndSessionIdentity(gameEngine.getUserService(), username);
            }else{
                DogUtils.resetStatusTokenAndSessionIdentity(gameEngine.getUserService(), username);
            }
        }
    }*/

}

