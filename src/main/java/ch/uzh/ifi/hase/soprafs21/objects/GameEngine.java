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
    private GameSessionRequestList gameSessionRequestList = new GameSessionRequestList();
    private final WaitingRoom waitingRoom;
    private final UserService userService;
    private final WebSocketService webSocketService;
    private final GameService gameService;
    private static final int PLAYER_AMOUNT = 2;
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
        /*if (gameEngine==null)
            //gameEngine = new GameEngine();*/
        //according to what I read online @Scope ("singleton") is the way to implement singleton in spring
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
    public GameSessionRequestList getGameSessionRequestList() {
        return gameSessionRequestList;
    }
    public WaitingRoom getWaitingRoom() {
        return waitingRoom;
    }
    public void setGameSessionList(List<GameSession> gameSessionList) {
        this.gameSessionList = gameSessionList;
    }
    public void setGameSessionRequestList(GameSessionRequestList gameSessionRequestList) {
        this.gameSessionRequestList = gameSessionRequestList;
    }
    public void setRunningGamesList(List<Game> runningGamesList) {
        this.runningGamesList = runningGamesList;
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

    public void addinvitedUserToGameSession(User user, UUID gameSessionId){
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
                if (gameSession.getUserList().size() == 4) {
                    gameSessionList.remove(gameSession);
                    Game game = new Game(gameSession.getUserList(), webSocketService, new CardAPIService());

                    for(User user:gameSession.getUserList()) {
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
        return Objects.requireNonNull(findGameSessionByID(gameSessionId)).userInHere(user);
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

    public void userDisconnected(User user){
        if(inWaitingRoom(user)){
            waitingRoom.removeUser(user);
        }else if(inGameSession(user)!=null){
            try {
                Objects.requireNonNull(findGameSessionByID(inGameSession(user))).deleteUser(user);
            }catch(NullPointerException e){
                System.out.println("Something went wrong in userDisconnected()");
            }
        }
    }

    private boolean inWaitingRoom(User user) {return waitingRoom.userInHere(user);};

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

    private UUID inGameSession(User user){
        try {
            for(GameSession gameSession:gameSessionList){
                if (gameSession.userInHere(user)) {
                    return gameSession.getID();
                }
            }
            return null;
        }catch(NullPointerException e) {
            System.out.println("Something went wrong in runningGameByID");
            return null;
        }
    }


    public void clearRequestsByHost(Long hostID){gameSessionRequestList.clearByHostID(hostID);}

    public void clearRequestByUser(Long userID, UUID gameSessionID){gameSessionRequestList.clearByUserAndGameSessionID(gameSessionID, userID);}


    public void addRequest(GameSessionRequest gameSessionRequest){gameSessionRequestList.addRequest(gameSessionRequest);}

    public void addUserToSession(User user,UUID gameSessionID){
        try {
            if(gameEngine.findGameSessionByID(gameSessionID).getUserList().size()==4){
                gameEngine.createGameFromGameSession(findGameSessionByID(gameSessionID));

            }else {
                for (GameSession gameSession : gameSessionList) {
                    if (gameSession.getID().equals(gameSessionID)) {
                        gameSession.addUser(user);
                    }
                }
                if(gameEngine.findGameSessionByID(gameSessionID).getUserList().size()==4) {
                    log.info("GameSession user list size 4");
                    gameEngine.createGameFromGameSession(findGameSessionByID(gameSessionID));
                }
            }
        }catch(NullPointerException e){
            System.out.println("Something went wrong in addUserToSession()");
        }
    }


    public List<GameSessionRequest> getRequestByUserID(Long userID){return gameSessionRequestList.getRequestsByUserID(userID);}

    public void deleteUserFromSession(User user, UUID gameSessionID){
        try {
            Objects.requireNonNull(findGameSessionByID(gameSessionID)).deleteUser(user);
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
            GameSession gameSession = new GameSession(host);
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

        int numberOfUsersToInvite = PLAYER_AMOUNT - gameSession.getUserList().size();

        if(!gameSession.getInvitedUsers().isEmpty()){
            String msg =  "Cant use FillUp while there are still pending gameRequests";
            webSocketService.sendGameSessionFillUpError(userService.convertUserNameToSessionIdentity(gameSession.getHostName()), msg);
        }
        else {

            try {
                List<User> usersFromWaitingRoomToFillup = waitingRoom.getXNumberOfUsers(numberOfUsersToInvite);
                List<User> usersInGameSession = gameSession.getUserList();

                gameSession.getUserList().addAll(usersFromWaitingRoomToFillup);
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
            for (User user : gameSession.getUserList()) {
                if (user.getUsername().equals(username)) {
                    return gameSession.getID();
                }
            }
        }
        return null;
    }

    public List<User> getUsersByGameSessionId(UUID gameSessionId){
        return Objects.requireNonNull(findGameSessionByID(gameSessionId)).getUserList();
    }
}
