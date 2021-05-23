package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.WaitingRoomUserObjDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.WaitingRoomSendOutCurrentUsersDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Scope("singleton")
@Transactional
public class GameEngine {
    private static GameEngine gameEngine;
    private List<GameSession> gameSessionList = new ArrayList<>();
    private List<Game> runningGamesList = new ArrayList<>();
    private final WaitingRoom waitingRoom;
    private final UserService userService;
    private final WebSocketService webSocketService;
    private final GameService gameService;
    public static final int PLAYER_AMOUNT = 4;
    Logger log = LoggerFactory.getLogger(GameEngine.class);

    @Autowired
    public GameEngine(WaitingRoom waitingRoom, UserService userService, WebSocketService webSocketService, GameService gameService){
        this.webSocketService = webSocketService;
        this.gameService = gameService;
        this.waitingRoom = waitingRoom;
        this.userService = userService;
        gameEngine = this;
    }

    public UserService getUserService() {
        return userService;
    }

    public static synchronized GameEngine instance() {
        return gameEngine;
    }

    public GameService getGameService() {
        return gameService;
    }

    public List<Game> getRunningGamesList() {
        return runningGamesList;
    }

    public List<GameSession> getGameSessionList() {
        return gameSessionList;
    }

    public WaitingRoom getWaitingRoom() {
        return waitingRoom;
    }

    public void addUserToWaitingRoom(User user){
        if(waitingRoom.addUser(user)==PLAYER_AMOUNT){
            try {
                List<User> usersForGame = waitingRoom.getXNumberOfUsers(PLAYER_AMOUNT);
                Game createdGame = createGameFromWaitingRoom(usersForGame);
                for (User userInWaitingRoom : usersForGame) {
                    String userIdentity = userInWaitingRoom.getSessionIdentity();
                    webSocketService.sendGameAssignmentMessageToWaitingRoom(userIdentity, createdGame.getPlayers(), createdGame.getGameId());
                }
            }
            catch (Exception e){
                log.info(e.getMessage());
            }
        }
    }

    public void addInvitedUserToGameSession(User user, UUID gameSessionId){
        GameSession gameSession = findGameSessionByID(gameSessionId);
        assert gameSession != null;
        gameSession.addInvitedUser(user);
    }

    public void removeUserFromWaitingRoom(User user){
        waitingRoom.removeUser(user);
    }

    public WaitingRoomSendOutCurrentUsersDTO createWaitingRoomUserList(){
        WaitingRoomSendOutCurrentUsersDTO waitingRoomSendOutCurrentUsersDTO = new WaitingRoomSendOutCurrentUsersDTO();
        List<WaitingRoomUserObjDTO> userList = new ArrayList<>();
        for(User user : waitingRoom.getUserQueue()){
            log.info(user.toString());
            userList.add(DTOMapper.INSTANCE.convertUserToWaitingRoomUserObjDTO(user));
        }
        waitingRoomSendOutCurrentUsersDTO.setCurrentUsers(userList);
        return waitingRoomSendOutCurrentUsersDTO;
    }

    private Game createGameFromWaitingRoom(List<User> usersForGame) {
        Game createdGame = new Game(usersForGame, webSocketService, new CardAPIService());
        runningGamesList.add(createdGame);
        return createdGame;
    }

    public Game createGameFromGameSession(GameSession gameSession){
        try {
            if (gameSessionList.contains(gameSession)) {
                if (gameSession.getAcceptedUsers().size() == PLAYER_AMOUNT) {
                    gameSessionList.remove(gameSession);
                    Game game = new Game(gameSession.getAcceptedUsers(), webSocketService, new CardAPIService());

                    for(User user:gameSession.getAcceptedUsers()) {
                        webSocketService.sendGameAssignmentMessageToGameSession(user.getSessionIdentity(),game.getPlayers(),game.getGameId(),gameSession.getID());
                    }
                    gameSessionList.remove(gameSession);
                    runningGamesList.add(game);
                    return game;
                }
            }
            return null;
        }catch(NullPointerException e){
            System.out.println("Something went wrong in createGameFromGameSession()");
            return null;
        }
    };
    public boolean userInGameSession(User user, UUID gameSessionId){
        return Objects.requireNonNull(findGameSessionByID(gameSessionId)).isAcceptedUserInHere(user);
    }

    private boolean gameSessionExists(GameSession gameSession){
        if(gameSession!=null){
            return (gameSessionList.contains(gameSession));
        }else{
            return false;
        }
    }

    public void deleteGameSession(UUID gameSessionId){
        if(this.gameSessionExists(findGameSessionByID(gameSessionId))){
            this.gameSessionList.remove(findGameSessionByID(gameSessionId));
        }
    };

    public GameSession findGameSessionByID(UUID id){
        try {
            for (GameSession gameSession : gameSessionList) {
                if (gameSession.getID().equals(id)) {
                    return gameSession;
                }
            }
            return null;
        }catch(NullPointerException e){
            System.out.println("Something went wrong in findGameSessionByID()");
            return null;
        }
    }

    public Game getRunningGameByID(UUID id){
        try {
            for (Game game : runningGamesList) {
                if (game.getGameId().equals(id)) {
                    return game;
                }
            }
            return null;
        }catch(NullPointerException e){
            System.out.println("Something went wrong in runningGameByID");
            return null;
        }
    }

    public void addUserToGameSession(User user, UUID gameSessionID){
        GameSession gameSession = findGameSessionByID(gameSessionID);
        gameSession.addAcceptedUser(user);
        if(gameSession.getAcceptedUsers().size() == PLAYER_AMOUNT){
            createGameFromGameSession(gameSession);
        }
    }

    public void deleteUserFromGameSession(User user, UUID gameSessionID){
        try {
            Objects.requireNonNull(findGameSessionByID(gameSessionID)).deleteAcceptedUser(user);
        }catch(NullPointerException e){
            System.out.println("Something went wrong in deleteUserFromSession()");
        }
    }

    public void deleteGameByGameID(UUID GameID){
        Game game = getRunningGameByID(GameID);
        if(game!=null){
            runningGamesList.remove(game);
        }
    };

    public UUID findGameIdByPlayerName(String playerName){
        try {
            for (Game game : runningGamesList) {
                for (Player player : game.getPlayers()) {
                    log.info(player.getPlayerName());
                    if (playerName.equals(player.getPlayerName())) {
                        return game.getGameId();
                    }
                }
            }
            return null;
        }catch(NullPointerException e){
            return null;
        }
    }

    /** check needs to happen if user available before calling method **/
    public void newGameSession(User host) {
        if(host.getStatus().equals(UserStatus.Free)){
            GameSession gameSession = new GameSession(host, userService);
            try {
                gameSessionList.add(gameSession);
            }catch(NullPointerException e){
                System.out.println("Something went wrong in newGameSession");
            }
        }
    }

    public boolean userInWaitingRoom(String username) {
        return waitingRoom.userInHere(userService.findByUsername(username));
    }

    public boolean isUserInGameSession(String username) {
        for(GameSession gameSession: gameSessionList){
            if(userInGameSession(userService.findByUsername(username),gameSession.getID())){
                return true;
            }
        }
        return false;
    }

    public boolean userIsHost(String username) {
        for(GameSession gameSession: gameSessionList){
            if(userInGameSession(userService.findByUsername(username),gameSession.getID())){
                if(gameSession.isHost(username)){
                    return true;
                }
            }
        }
        return false;
    }

    public GameSession findGameSessionByHostName(String hostName){
        for(GameSession gs: gameSessionList){
            if(gs.getHostName().equals(hostName)){
                return gs;
            }
        }
        return null;
    }

    public boolean userInGame(String username) {
        for(Game game: runningGamesList){
            for(Player player: game.getPlayers()){
                if(player.getPlayerName().equals(username)){
                    return true;
                }
        }
    }
        return false;
    }

    public Player findPlayerbyUsername(Game game, String playerName){
        for(Player p: game.getPlayers()){
            if(p.getPlayerName().equals(playerName)){
                return p;
            }
        }
        return null;
    }

    public void createGameFromGameSessionAndFillUp(GameSession gameSession){

        int numberOfUsersToInvite = PLAYER_AMOUNT - gameSession.getAcceptedUsers().size();

        if(!gameSession.getInvitedUsers().isEmpty()){
            String msg =  "Cant use FillUp while there are still pending gameRequests";
            webSocketService.sendGameSessionFillUpError(userService.convertUserNameToSessionIdentity(gameSession.getHostName()), msg);
        }
        else {

            try {
                List<User> usersFromWaitingRoomToFillup = waitingRoom.getXNumberOfUsers(numberOfUsersToInvite);
                List<User> usersInGameSession = gameSession.getAcceptedUsers();

                gameSession.getAcceptedUsers().addAll(usersFromWaitingRoomToFillup);
                Game createdGame = createGameFromGameSession(gameSession);

                for (User userForFillup : usersFromWaitingRoomToFillup) {
                    String userIdentity = userForFillup.getSessionIdentity();
                    webSocketService.sendGameAssignmentMessageToWaitingRoom(userIdentity, createdGame.getPlayers(), createdGame.getGameId());
                }
                for(User usersGameSession: usersInGameSession){
                    String userIdentity = usersGameSession.getSessionIdentity();
                    webSocketService.sendGameAssignmentMessageToGameSession(userIdentity, createdGame.getPlayers(), createdGame.getGameId(),gameSession.getID());
                }
            }catch (Exception e) {
                webSocketService.sendGameSessionFillUpError(userService.convertUserNameToSessionIdentity(gameSession.getHostName()), e.getMessage());
            }
        }
    }

    public synchronized void deleteGameSessionByHostName(String username) {
        try {
            for (GameSession gameSession : gameSessionList) {
                if (userIsHost(username) && userInGameSession(userService.getUserRepository().findByUsername(username), gameSession.getID())) {
                    deleteGameSession(gameSession.getID());
                }
            }
        }catch(ConcurrentModificationException | NullPointerException ignored){}
    }

    public UUID findGameSessionIdByUsername(String username) {
        for(GameSession gameSession: gameSessionList) {
            for (User user : gameSession.getAcceptedUsers()) {
                if (user.getUsername().equals(username)) {
                    return gameSession.getID();
                }
            }
        }
        return null;
    }

    public List<User> getAcceptedUsersByGameSessionId(UUID gameSessionId){
        try {
            return Objects.requireNonNull(findGameSessionByID(gameSessionId)).getAcceptedUsers();
        }catch(NullPointerException e){
            return null;
        }
    }

    public List<User> getInvitedUsersByGameSessionId(UUID gameSessionId){
        try {
            return Objects.requireNonNull(findGameSessionByID(gameSessionId)).getInvitedUsers();
        }catch(NullPointerException e){
            return null;
        }
    }
}
