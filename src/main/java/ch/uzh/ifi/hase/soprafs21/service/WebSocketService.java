package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.objects.Card;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;
import ch.uzh.ifi.hase.soprafs21.objects.Player;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.FactDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.GameCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * - Handles general WebSocket stuff (chat, reconnect)
 * - Provides utility functions for sending packets
 */
@Service
@Transactional
public class WebSocketService {
    Logger log = LoggerFactory.getLogger(WebSocketService.class);

    @Autowired
    public SimpMessagingTemplate simp;
    private final UserService userService;

    public WebSocketService(UserService userService) {
        this.userService = userService;
    }

    private void sendToPlayer(String identity, String path, Object dto) {
        this.simp.convertAndSendToUser(identity, "/queue" + path, dto);
    }

    private void broadcastToTopic(String path, Object dto){
        this.simp.convertAndSend("/topic" + path, dto);
    }

    public void sendExchangeFactsMessage(String roundBeginner, UUID gameId) {
        broadcastFactsMessage(DogUtils.convertRoundBeginnerToFactList(roundBeginner), gameId);
        // "Card Exchange" will trigger a mode switch on the frontend
        broadcastNotificationMessage( "Card Exchange", gameId);
    }

    public void broadcastFactsMessage(int roundCount, String playerName, int nextCardAmount, String nextPlayerName, UUID gameId) {
        broadcastFactsMessage(DogUtils.generateCurrentTurnFactList(roundCount, playerName, nextCardAmount, nextPlayerName), gameId);
    }

    public void broadcastFactsMessage(List<FactDTO> factList, UUID gameId) {
        GameFactsDTO gameFactsDTO = new GameFactsDTO();
        gameFactsDTO.setFacts(factList);

        String pathFacts = "/game/%s/facts";
        broadcastToTopic(String.format(pathFacts, gameId.toString()), gameFactsDTO);
    }

    public void broadcastNotificationMessage(String action, UUID gameId) {
        broadcastNotificationMessage(null, action, null, gameId);
    }

    public void broadcastNotificationMessage(String playerName, String action, UUID gameId) {
        broadcastNotificationMessage(playerName, action, null, gameId);
    }

    public void broadcastNotificationMessage(String playerName, String action, String cardCode, UUID gameId) {
        String pathNotifications = "/game/%s/notification";
        broadcastToTopic(String.format(pathNotifications, gameId.toString()),
                    DogUtils.generateGameNotificatoinDTO(playerName, action, cardCode));
    }

    public void broadcastCurrentTurnMessage(String playerName, UUID gameId) {
        RoundCurrentPlayerDTO roundCurrentPlayerDTO = new RoundCurrentPlayerDTO();
        roundCurrentPlayerDTO.setPlayerName(playerName);

        String path = "/game/%s/turn";
        broadcastToTopic(String.format(path, gameId.toString()), roundCurrentPlayerDTO);
    }

    public void sendCardsToPlayer(String sessionIdentity, List<Card> cards, UUID gameId) {
        sendCardsToPlayer(sessionIdentity, cards, -1, gameId);
    }

    public void sendCardsToPlayer(String userSessionIdentity, List<Card> cards, int idx, UUID gameId) {
        GameListOfCardsDTO gameListOfCardsDTO = new GameListOfCardsDTO();
        List<GameCardDTO> cardList = new ArrayList<>();
        for(Card c : cards) {
            GameCardDTO gameCardDTO = DTOMapper.INSTANCE.convertCardtoGameCardDTO(c);
            if(idx > -1) {
                gameCardDTO.setIdx(idx);
            }
            cardList.add(gameCardDTO);
        }
        gameListOfCardsDTO.setCards(cardList);

        String path = "/game/%s/cards";
        sendToPlayer(userSessionIdentity, String.format(path, gameId.toString()), gameListOfCardsDTO);
    }

    public void sendGameAssignmentMessageToWaitingRoom(String userSessionIdentity, List<Player> players, UUID gameId) {
        String path = "/waiting-room";
        sendGameAssignmentMessage(userSessionIdentity, players, gameId, path);
    }

    public void sendGameAssignmentMessageToGameSession(String userSessionIdentity, List<Player> players, UUID gameId) {
        String path = "/gamesession/ready";
        sendGameAssignmentMessage(userSessionIdentity, players, gameId, path);
    }

    private void sendGameAssignmentMessage(String userSessionIdentity, List<Player> players, UUID gameId, String path){
        WaitingRoomChooseColorDTO waitingRoomChooseColorDTO = DogUtils.convertPlayersToWaitingRoomChooseColorDTO(players, gameId);
        sendToPlayer(userSessionIdentity, path, waitingRoomChooseColorDTO);
    }

    public void broadcastPlayedMessage(String playerName, String cardCode, ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKeys, UUID gameId){
        String path = "/game/%s/played";
        broadcastNotificationMessage(playerName, "played", cardCode, gameId);
        broadcastToTopic(String.format(path, gameId.toString()),
                DogUtils.generateExecutedCardDTO(playerName, cardCode,
                        DogUtils.generateMarbleExecutedCardDTO(marbleIdsAndTargetFieldKeys)));
    }

    public void sentGameEndMessage(String gameId, GameEndDTO gameEndDTO) {
        String path = "/game/%s/game-end";
        broadcastToTopic(String.format(path, gameId), gameEndDTO);
    }

    public void broadcastGameSessionEndMessage(String gameSessionId, String hostName) {

        String path = "/gamesession/%s/gamesession-end";
        broadcastToTopic(String.format(path, gameSessionId),
                    DogUtils.generateGameSessionHostLeftDTO(hostName));
    }

    public void broadcastGameSessionInvitedUserList(UUID gameSessionId, List<User> invitedUserList){
        String path = "/gamesession/%s/invited-user";
        broadcastToTopic(String.format(path, gameSessionId.toString()),
                DogUtils.generateGameSessionInvitedUsersDTO(invitedUserList));

    }

    public void sendGameSessionInvitedUserCounter(GameSession gameSession, User invitedUser, String sessionIdentity) throws InterruptedException {

        final int[] counter = {15};
        TimerTask task = new TimerTask() {
            public void run() {
                RequestCountDownDTO requestCountDownDTO = DogUtils.generateRequestCountDownDTO(counter[0], invitedUser.getUsername());
                broadcastRequestCountdown(gameSession.getID(),requestCountDownDTO );
                sendRequestCountdown(sessionIdentity, requestCountDownDTO);
                --counter[0];

                if(counter[0] < 0 || !gameSession.userInInvitedUsers(invitedUser.getUsername())){
                    cancel();
                    gameSession.getInvitedUsers().remove(invitedUser);
                    userService.updateStatus(invitedUser.getToken(), UserStatus.Free);
                    broadcastGameSessionInvitedUserList(gameSession.getID(), gameSession.getInvitedUsers());
                }
            }
        };

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        long delay = 1000L;
        long period = 1000L;
        executor.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
        Thread.sleep(1000L*17);
        executor.shutdown();

    }

    private void broadcastRequestCountdown(UUID gameSessionId, RequestCountDownDTO requestCountDownDTO){
        String path = "/gamesession/%s/countdown";
        broadcastToTopic(String.format(path, gameSessionId.toString()),
                    requestCountDownDTO);
    }

    private void sendRequestCountdown(String sessionIdentity, RequestCountDownDTO requestCountDownDTO){
        String path = "/countdown";
        sendToPlayer(sessionIdentity, path,
                    requestCountDownDTO);

    }

    public void sendGameSessionInvitation(UUID gameSessionId, String sessionIdentityOfInvitedUser, String hostName){
        String path = "/invitation";
        sendToPlayer(sessionIdentityOfInvitedUser, path, DogUtils.generateGameSessionInviteUserDTO(gameSessionId, hostName));
    }

    public void broadcastThrowAway(UUID gameId, String playerName, List<String> cardCodes){
        String path = "/game/%s/throwaway";
        broadcastNotificationMessage(playerName, "threw cards away", gameId);
        broadcastToTopic(String.format(path, gameId.toString()),
                    DogUtils.generateGameThrowAwayDTO(playerName, cardCodes));

    }

    public void sendGameSessionInviteError(String sessionIdentity){
        String msg = "Invited User must be online and free to join your game.";
        String path = "/gamesession/error/invite";

        sendErrorMsg(sessionIdentity, msg, path);
    }

    public void sendGameSessionFillUpError(String sessionIdentity, String msg){
        String path = "/gamesession/error/fill-up";
        sendErrorMsg(sessionIdentity, msg, path);
    }

    private void sendErrorMsg(String sessionIdentitiy, String msg, String path){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMsg(msg);
        sendToPlayer(sessionIdentitiy, path, errorDTO);
    }

}
