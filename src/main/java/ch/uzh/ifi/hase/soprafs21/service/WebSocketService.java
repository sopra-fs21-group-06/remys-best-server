package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.FactDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.GameCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ch.uzh.ifi.hase.soprafs21.utils.DogUtils.convertPlayersToGameSessionUserListDTO;


/**
 * - Handles general WebSocket stuff (chat, reconnect)
 * - Provides utility functions for sending packets
 */
@Service
public class WebSocketService {

    public static String TOPIC_PREFIX = "/topic";
    public static String QUEUE_PREFIX = "/queue";

    private final SimpMessagingTemplate simp;

    @Autowired
    public WebSocketService(SimpMessagingTemplate simp) {
        this.simp = simp;
    }

    public void sendToPlayer(String identity, String path, Object dto) {
        this.simp.convertAndSendToUser(identity, QUEUE_PREFIX + path, dto);
    }

    public void broadcastToTopic(String path, Object dto){
        this.simp.convertAndSend(TOPIC_PREFIX + path, dto);
    }

    public void broadcastExchangeFactsMessage(String roundBeginner, UUID gameId) {
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
        broadcastToTopic(String.format(pathNotifications, gameId.toString()), DogUtils.generateGameNotificatoinDTO(playerName, action, cardCode));
    }

    public void broadcastCurrentTurnMessage(String playerName, UUID gameId) {
        RoundCurrentPlayerDTO roundCurrentPlayerDTO = new RoundCurrentPlayerDTO();
        roundCurrentPlayerDTO.setPlayerName(playerName);

        String path = "/game/%s/turn";
        broadcastToTopic(String.format(path, gameId.toString()), roundCurrentPlayerDTO);
    }

    public void broadcastPlayedMessage(String playerName, String cardCode, ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKeys, UUID gameId){
        String path = "/game/%s/played";
        broadcastNotificationMessage(playerName, "played", cardCode, gameId);
        broadcastToTopic(String.format(path, gameId.toString()), DogUtils.generateExecutedCardDTO(playerName, cardCode, DogUtils.generateMarbleExecutedCardDTO(marbleIdsAndTargetFieldKeys)));
    }

    public void broadcastGameEndMessage(String gameId, GameEndDTO gameEndDTO) {
        String path = "/game/%s/game-end";
        broadcastToTopic(String.format(path, gameId), gameEndDTO);
    }

    public void broadcastGameSessionEndMessage(String gameSessionId, String hostName) {
        String path = "/gamesession/%s/gamesession-end";
        broadcastToTopic(String.format(path, gameSessionId), DogUtils.generateGameSessionHostLeftDTO(hostName));
    }

    public void broadcastGameSessionInvitedUserList(UUID gameSessionId, List<User> invitedUserList){
        String path = "/gamesession/%s/invited-user";
        broadcastToTopic(String.format(path, gameSessionId.toString()), DogUtils.generateGameSessionInvitedUsersDTO(invitedUserList));
    }

    public void broadcastCountdownToGameSession(UUID gameSessionId, RequestCountDownDTO requestCountDownDTO){
        String path = "/gamesession/%s/countdown";
        broadcastToTopic(String.format(path, gameSessionId.toString()), requestCountDownDTO);
    }

    public void broadcastThrowAway(UUID gameId, String playerName, List<String> cardCodes){
        String path = "/game/%s/throwaway";
        broadcastNotificationMessage(playerName, "threw cards away", gameId);
        broadcastToTopic(String.format(path, gameId.toString()), DogUtils.generateGameThrowAwayDTO(playerName, cardCodes));
    }

    public void broadcastUsersInGameSession(UUID gameSessionId) {
        String path = "/gamesession/%s/accepted";
        broadcastToTopic(String.format(path, gameSessionId.toString()), convertPlayersToGameSessionUserListDTO(GameEngine.instance().getUsersByGameSessionId(gameSessionId)));
    }

    public void broadcastPlayerDisconnectedFromWaitingRoom(WaitingRoomSendOutCurrentUsersDTO dto){
        String path = "/waiting-room";
        broadcastToTopic(path, dto);
    }
    public void broadcastAbruptEndOfGameSessionMessage(UUID gameSessionIdByUsername, String username) {
        String path = "/gamesession/%s/gamesession-end";
        GameSessionEndDTO dto = new GameSessionEndDTO(username);
        broadcastToTopic(String.format(path, gameSessionIdByUsername.toString()), dto);
    }

    public void sendCardsToPlayer(String sessionIdentity, List<Card> cards, UUID gameId) {
        sendCardsToPlayer(sessionIdentity, cards, -1, gameId);
    }

    public void sendCardsToPlayer(String userSessionIdentity, List<Card> cards, int idx, UUID gameId) {
        GameListOfCardsDTO gameListOfCardsDTO = new GameListOfCardsDTO();
        List<GameCardDTO> cardList = new ArrayList<>();
        for(Card c : cards) {
            GameCardDTO gameCardDTO = DTOMapper.INSTANCE.convertCardToGameCardDTO(c);
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

    public void sendGameAssignmentMessageToGameSession(String userSessionIdentity, List<Player> players, UUID gameId,UUID gameSessionID) {
        String path = "/gamesession/%s/ready";
        sendGameAssignmentMessage(userSessionIdentity, players, gameId, String.format(path, gameSessionID.toString()));
    }

    private void sendGameAssignmentMessage(String userSessionIdentity, List<Player> players, UUID gameId, String path){
        WaitingRoomChooseColorDTO waitingRoomChooseColorDTO = DogUtils.convertPlayersToWaitingRoomChooseColorDTO(players, gameId);
        sendToPlayer(userSessionIdentity, path, waitingRoomChooseColorDTO);
    }

    public void sendGameSessionInvitedUserCounter(GameSession gameSession, User invitedUser, String sessionIdentity) throws InterruptedException {

        final int[] counter = {15};
        TimerTask task = new TimerTask() {
            public void run() {
                RequestCountDownDTO requestCountDownDTO = DogUtils.generateRequestCountDownDTO(counter[0], invitedUser.getUsername());
                --counter[0];

                if(counter[0] < 0 || !gameSession.isInvitedUserInHere(invitedUser.getUsername())){
                    if(gameSession.isInvitedUserInHere(invitedUser.getUsername())){
                        gameSession.deleteInvitedUser(invitedUser);
                    }
                    broadcastGameSessionInvitedUserList(gameSession.getID(), gameSession.getInvitedUsers());
                    cancel();
                }else{
                    broadcastCountdownToGameSession(gameSession.getID(),requestCountDownDTO );
                    sendCountdownToHome(sessionIdentity, requestCountDownDTO);
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

    public void sendCountdownToHome(String sessionIdentity, RequestCountDownDTO requestCountDownDTO){
        String path = "/countdown";
        sendToPlayer(sessionIdentity, path, requestCountDownDTO);
    }

    public void sendGameSessionInvitation(UUID gameSessionId, String sessionIdentityOfInvitedUser, String hostName){
        String path = "/invitation";
        sendToPlayer(sessionIdentityOfInvitedUser, path, DogUtils.generateGameSessionInviteUserDTO(gameSessionId, hostName));
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
