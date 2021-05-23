package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.WaitingRoomUserObjDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class WebSocketServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketService webSocketService;

    private WebSocketService webSocketServiceSpy;

    private String identity;
    private String path;
    private Object dto;

    @BeforeEach
    public void setup() {
        userService.getUserRepository().deleteAll();
        webSocketServiceSpy = Mockito.spy(webSocketService);

        Mockito.doAnswer((Answer<Void>) invocation -> {
            final Object[] args = invocation.getArguments();
            this.path = (String) args[0];
            this.dto = args[1];
            return null;
        }).when(webSocketServiceSpy).broadcastToTopic(Mockito.any(), Mockito.any());


        Mockito.doAnswer((Answer<Void>) invocation -> {
            final Object[] args = invocation.getArguments();
            this.identity = (String) args[0];
            this.path = (String) args[1];
            this.dto = args[2];
            return null;
        }).when(webSocketServiceSpy).sendToPlayer(Mockito.any(), Mockito.any(), Mockito.any());
    }

    private User createTestUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password");
        user.setSessionIdentity(UUID.randomUUID().toString());
        userService.createUser(user);
        return user;
    }

    @Test
    void testBroadcastFactsMessage() {
        int roundCount = 2;
        String playerName = "player one";
        int nextCardAmount = 4;
        String nextPlayerName = "player two";
        UUID gameId = UUID.randomUUID();

        webSocketServiceSpy.broadcastFactsMessage(roundCount, playerName, nextCardAmount, nextPlayerName, gameId);

        assertEquals(String.format("/game/%s/facts", gameId.toString()), path);

        GameFactsDTO gameFactsDTO = (GameFactsDTO) dto;
        assertEquals(4, gameFactsDTO.getFacts().size());
        assertEquals(String.valueOf(roundCount), gameFactsDTO.getFacts().get(0).getTitle());
        assertEquals("Round", gameFactsDTO.getFacts().get(0).getSubTitle());
        assertEquals(playerName, gameFactsDTO.getFacts().get(1).getTitle());
        assertEquals("Active Player", gameFactsDTO.getFacts().get(1).getSubTitle());
        assertEquals(String.valueOf(nextCardAmount), gameFactsDTO.getFacts().get(2).getTitle());
        assertEquals("Next Round Card Amount", gameFactsDTO.getFacts().get(2).getSubTitle());
        assertEquals(nextPlayerName, gameFactsDTO.getFacts().get(3).getTitle());
        assertEquals("Next Round Beginner", gameFactsDTO.getFacts().get(3).getSubTitle());
    }

    @Test
    void testBroadcastNotificationMessage() {
        String action = "Card Exchange";
        UUID gameId = UUID.randomUUID();

        webSocketServiceSpy.broadcastNotificationMessage(action, gameId);

        assertEquals(String.format("/game/%s/notification", gameId.toString()), path);
        GameNotificationDTO gameNotificationDTO = (GameNotificationDTO) dto;
        assertNull(gameNotificationDTO.getPlayerName());
        assertEquals(action, gameNotificationDTO.getAction());
        assertNull(gameNotificationDTO.getCard());

        String playerName = "Player one";
        action = "threw cards away";
        webSocketServiceSpy.broadcastNotificationMessage(playerName, action, gameId);

        assertEquals(String.format("/game/%s/notification", gameId.toString()), path);
        gameNotificationDTO = (GameNotificationDTO) dto;
        assertEquals(playerName, gameNotificationDTO.getPlayerName());
        assertEquals(action, gameNotificationDTO.getAction());
        assertNull(gameNotificationDTO.getCard());

        playerName = "Player two";
        action = "played";
        String cardCode = "X1";
        webSocketServiceSpy.broadcastNotificationMessage(playerName, action, cardCode, gameId);

        assertEquals(String.format("/game/%s/notification", gameId.toString()), path);
        gameNotificationDTO = (GameNotificationDTO) dto;
        assertEquals(playerName, gameNotificationDTO.getPlayerName());
        assertEquals(action, gameNotificationDTO.getAction());
        assertEquals("Joker", gameNotificationDTO.getCard());
    }

    @Test
    void testBroadcastCurrentTurnMessage() {
        String playerName = "Player one";
        UUID gameId = UUID.randomUUID();

        webSocketServiceSpy.broadcastCurrentTurnMessage(playerName, gameId);

        assertEquals(String.format("/game/%s/turn", gameId.toString()), path);
        RoundCurrentPlayerDTO roundCurrentPlayerDTO = (RoundCurrentPlayerDTO) dto;
        assertEquals(playerName, roundCurrentPlayerDTO.getPlayerName());
    }

    @Test
    void testBroadcastPlayedMessage() {
        String playerName = "Player one";
        String cardCode = "KD";
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKeys = new ArrayList<>();
        int marbleId = 4;
        String targetFieldKey = "4GREEN";
        marbleIdsAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marbleId, targetFieldKey));
        UUID gameId = UUID.randomUUID();

        webSocketServiceSpy.broadcastPlayedMessage(playerName, cardCode, marbleIdsAndTargetFieldKeys, gameId);

        assertEquals(String.format("/game/%s/played", gameId.toString()), path);
        ExecutedCardDTO executedCardDTO = (ExecutedCardDTO) dto;
        assertEquals(playerName, executedCardDTO.getPlayerName());
        assertEquals(cardCode, executedCardDTO.getCard().getCode());
        assertEquals(1, executedCardDTO.getMarbles().size());
        assertEquals(marbleId, executedCardDTO.getMarbles().get(0).getMarbleId());
        assertEquals(targetFieldKey, executedCardDTO.getMarbles().get(0).getTargetFieldKey());
    }

    @Test
    void testBroadcastGameEndMessage() {
        String playerName = "Player one";
        UUID gameId = UUID.randomUUID();
        GameEndDTO gameEndDTO = new GameEndDTO();
        gameEndDTO.setAborted(playerName);
        webSocketServiceSpy.broadcastGameEndMessage(gameId.toString(), gameEndDTO);

        assertEquals(String.format("/game/%s/game-end", gameId.toString()), path);
        gameEndDTO = (GameEndDTO) dto;
        assertEquals(playerName, gameEndDTO.getAborted());
    }

    @Test
    void testBroadcastGameSessionEndMessage() {
        String hostName = "Player one";
        UUID gameId = UUID.randomUUID();
        webSocketServiceSpy.broadcastGameSessionEndMessage(gameId.toString(), hostName);

        assertEquals(String.format("/gamesession/%s/gamesession-end", gameId.toString()), path);
        GameSessionHostLeftDTO gameSessionHostLeftDTO = (GameSessionHostLeftDTO) dto;
        assertEquals(hostName, gameSessionHostLeftDTO.getUsername());
    }

    @Test
    void testBroadcastGameSessionInvitedUserList() {
        User host = createTestUser("Carl", "carl@carl.ch");
        User invitedUser = createTestUser("Peter", "peter@carl.ch");
        GameSession gameSession = GameEngine.instance().newGameSession(host);
        GameEngine.instance().addInvitedUserToGameSession(invitedUser, gameSession.getID());

        webSocketServiceSpy.broadcastInvitedUsersInGameSession(gameSession.getID());

        assertEquals(String.format("/gamesession/%s/invited-user", gameSession.getID().toString()), path);
        GameSessionInvitedUsersDTO gameSessionInvitedUsersDTO = (GameSessionInvitedUsersDTO) dto;
        assertEquals(1, gameSessionInvitedUsersDTO.getInvitedUsers().size());
        assertEquals(invitedUser.getUsername(), gameSessionInvitedUsersDTO.getInvitedUsers().get(0).getUsername());

        GameEngine.instance().deleteGameSessionByHostName(host.getUsername());
    }

    @Test
    void testBroadcastCountdownToGameSession() {
        UUID gameSessionId = UUID.randomUUID();
        int currentCounter = 13;
        String playerName = "Peter";
        RequestCountDownDTO requestCountDownDTO = new RequestCountDownDTO();
        requestCountDownDTO.setCurrentCounter(currentCounter);
        requestCountDownDTO.setUsername(playerName);
        webSocketServiceSpy.broadcastCountdownToGameSession(gameSessionId, requestCountDownDTO);

        assertEquals(String.format("/gamesession/%s/countdown", gameSessionId.toString()), path);
        requestCountDownDTO = (RequestCountDownDTO) dto;
        assertEquals(currentCounter, requestCountDownDTO.getCurrentCounter());
        assertEquals(playerName, requestCountDownDTO.getUsername());
    }

    @Test
    void testBroadcastThrowAway() {
        UUID gameId = UUID.randomUUID();
        String playerName = "Peter";
        List<String> cardCodes = new ArrayList<>();
        cardCodes.add("2D");
        cardCodes.add("5C");
        cardCodes.add("JH");

        webSocketServiceSpy.broadcastThrowAway(gameId, playerName, cardCodes);

        assertEquals(String.format("/game/%s/throwaway", gameId.toString()), path);
        GameThrowAwayDTO gameThrowAwayDTO = (GameThrowAwayDTO) dto;
        assertEquals(playerName, gameThrowAwayDTO.getPlayerName());
        assertEquals(3, gameThrowAwayDTO.getCardCodes().size());
    }

    @Test
    void testBroadcastPlayerDisconnectedFromWaitingRoom() {
        String username = "Peter";

        WaitingRoomSendOutCurrentUsersDTO waitingRoomSendOutCurrentUsersDTO = new WaitingRoomSendOutCurrentUsersDTO();
        List<WaitingRoomUserObjDTO> users = new ArrayList<>();
        WaitingRoomUserObjDTO waitingRoomUserObjDTO = new WaitingRoomUserObjDTO();
        waitingRoomUserObjDTO.setUsername(username);
        users.add(waitingRoomUserObjDTO);
        waitingRoomSendOutCurrentUsersDTO.setCurrentUsers(users);

        webSocketServiceSpy.broadcastPlayerDisconnectedFromWaitingRoom(waitingRoomSendOutCurrentUsersDTO);

        assertEquals("/waiting-room", path);
        waitingRoomSendOutCurrentUsersDTO = (WaitingRoomSendOutCurrentUsersDTO) dto;
        assertEquals(1, waitingRoomSendOutCurrentUsersDTO.getCurrentUsers().size());
        assertEquals(username, waitingRoomSendOutCurrentUsersDTO.getCurrentUsers().get(0).getUsername());
    }

    @Test
    void testBroadcastAbruptEndOfGameSessionMessage() {
        UUID gameSessionId = UUID.randomUUID();
        String username = "Peter";
        webSocketServiceSpy.broadcastAbruptEndOfGameSessionMessage(gameSessionId, username);

        assertEquals(String.format("/gamesession/%s/gamesession-end", gameSessionId.toString()), path);
        GameSessionEndDTO gameSessionEndDTO = (GameSessionEndDTO) dto;
        assertEquals(username, gameSessionEndDTO.getUsername());
    }

    @Test
    void testSendCardsToPlayer() {
        String sessionIdentity = UUID.randomUUID().toString();
        UUID gameId = UUID.randomUUID();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("2D"));
        cards.add(new Card("KH"));
        cards.add(new Card("X1"));

        webSocketServiceSpy.sendCardsToPlayer(sessionIdentity, cards, gameId);

        assertEquals(String.format("/game/%s/cards", gameId.toString()), path);
        assertEquals(sessionIdentity, identity);
        GameListOfCardsDTO gameListOfCardsDTO = (GameListOfCardsDTO) dto;
        assertEquals(3, gameListOfCardsDTO.getCards().size());
    }

    @Test
    void testSendGameAssignmentMessageToWaitingRoom() {
        String sessionIdentity = UUID.randomUUID().toString();
        UUID gameId = UUID.randomUUID();
        List<Player> players = new ArrayList<>();
        players.add(new Player("Peter"));
        players.add(new Player("Carl"));

        webSocketServiceSpy.sendGameAssignmentMessageToWaitingRoom(sessionIdentity, players, gameId);

        assertEquals("/waiting-room", path);
        assertEquals(sessionIdentity, identity);
        WaitingRoomChooseColorDTO waitingRoomChooseColorDTO = (WaitingRoomChooseColorDTO) dto;
        assertEquals(gameId, waitingRoomChooseColorDTO.getGameId());
        assertEquals(2, waitingRoomChooseColorDTO.getPlayers().size());
    }

    @Test
    void testSendGameAssignmentMessageToGameSession() {
        String sessionIdentity = UUID.randomUUID().toString();
        UUID gameId = UUID.randomUUID();
        UUID gameSessionId = UUID.randomUUID();
        List<Player> players = new ArrayList<>();
        players.add(new Player("Peter"));
        players.add(new Player("Carl"));

        webSocketServiceSpy.sendGameAssignmentMessageToGameSession(sessionIdentity, players, gameId, gameSessionId);

        assertEquals(String.format("/gamesession/%s/ready", gameSessionId.toString()), path);
        assertEquals(sessionIdentity, identity);
        WaitingRoomChooseColorDTO waitingRoomChooseColorDTO = (WaitingRoomChooseColorDTO) dto;
        assertEquals(gameId, waitingRoomChooseColorDTO.getGameId());
        assertEquals(2, waitingRoomChooseColorDTO.getPlayers().size());
    }

    @Test
    void testSendInvitationCountdownToHome() {
        String sessionIdentity = UUID.randomUUID().toString();
        int currentCounter = 13;
        String playerName = "Peter";
        RequestCountDownDTO requestCountDownDTO = new RequestCountDownDTO();
        requestCountDownDTO.setCurrentCounter(currentCounter);
        requestCountDownDTO.setUsername(playerName);

        webSocketServiceSpy.sendInvitationCountdownToHome(sessionIdentity, requestCountDownDTO);

        assertEquals("/home/invitation/countdown", path);
        assertEquals(sessionIdentity, identity);
        requestCountDownDTO = (RequestCountDownDTO) dto;
        assertEquals(currentCounter, requestCountDownDTO.getCurrentCounter());
        assertEquals(playerName, requestCountDownDTO.getUsername());
    }

    @Test
    void testSendInvitationToHome() {
        String sessionIdentity = UUID.randomUUID().toString();
        User host = createTestUser("Peter", "peter@peter.ch");
        GameSession gameSession = GameEngine.instance().newGameSession(host);

        webSocketServiceSpy.sendInvitationToHome(gameSession.getID(), sessionIdentity);

        assertEquals("/home/invitation", path);
        assertEquals(sessionIdentity, identity);
        GameSessionInviteUserDTO gameSessionInviteUserDTO = (GameSessionInviteUserDTO) dto;
        assertEquals(host.getUsername(), gameSessionInviteUserDTO.getHostName());
        assertEquals(gameSession.getID(), gameSessionInviteUserDTO.getGameSessionId());

        GameEngine.instance().deleteGameSessionByHostName(host.getUsername());
    }

    @Test
    void testSendGameSessionInviteError() {
        String sessionIdentity = UUID.randomUUID().toString();
        webSocketServiceSpy.sendGameSessionInviteError(sessionIdentity);

        assertEquals("/gamesession/error/invite", path);
        assertEquals(sessionIdentity, identity);
        ErrorDTO errorDTO = (ErrorDTO) dto;
        assertEquals("Invited User must be online and free to join your game.", errorDTO.getMsg());
    }

    @Test
    void testSendGameSessionFillUpError() {
        String sessionIdentity = UUID.randomUUID().toString();
        String msg = "Error message";
        webSocketServiceSpy.sendGameSessionFillUpError(sessionIdentity, msg);

        assertEquals("/gamesession/error/fill-up", path);
        assertEquals(sessionIdentity, identity);
        ErrorDTO errorDTO = (ErrorDTO) dto;
        assertEquals(msg, errorDTO.getMsg());
    }
}
