package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.dom4j.util.SingletonStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GameEngine{
    private static GameEngine gameEngine;
    private List<GameSession> gameSessionList;
    private List<Game> runningGamesList;
    private GameSessionRequestList gameSessionRequestList;
    private WaitingRoom waitingRoom;

    private GameEngine(){
        this.gameSessionList= new ArrayList<GameSession>();
        this.runningGamesList= new ArrayList<Game>();
        this.gameSessionRequestList=new GameSessionRequestList();
        this.waitingRoom= new WaitingRoom();
    }

    public static synchronized GameEngine instance() {
        if (gameEngine==null)
            gameEngine = new GameEngine();
        return gameEngine;
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

    public void setWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
    }
    public void addUserToWaitingRoom(User user){
        if(waitingRoom.addUser(user)==4){
            this.createGameFromWaitingRoom();
        }
    };
    private Game createGameFromWaitingRoom(){return new Game(this.waitingRoom.getFirstFour());}

    public Game createGameFromGameSession(GameSession gameSession,List<User> Users){
        return new Game(gameSession.getUserList());
    };
    private boolean gameSessionExists(GameSession gameSession){
        return (gameSessionList.contains(gameSession));
    }
    public void deleteGameSession(GameSession gameSession){
        if(this.gameSessionExists(gameSession)){
            this.gameSessionList.remove(gameSession);
        }
    };
    private GameSession findGameSessionByID(UUID id){
        for(GameSession gameSession: gameSessionList) {
            if (gameSession.getID().equals(id)) {
                return gameSession;
            }
        }
        return null;
    }

    public void userDisconnected(User user){
        if(inWaitingRoom(user)){
            waitingRoom.removeUser(user);
        }else if(inGameSession(user)!=null){
            Objects.requireNonNull(findGameSessionByID(inGameSession(user))).deleteUser(user);
        }
    }
    /** throws UserNotInAnyGameException **/
    private UUID inGameSession(User user){
        for(GameSession gameSession:gameSessionList){
            if (gameSession.userInHere(user)) {
                return gameSession.getID();
            }
        }
        return null;
    }

    private boolean inWaitingRoom(User user) {return waitingRoom.userInHere(user);};

    /** throws noSuchGameException **/
    private Game runningGameByID(UUID id){
        for(Game game:runningGamesList){
            if(game.getGameID().equals(id)){
                return game;
            }
        }
        return null;
    }


    public void clearRequestsByHost(UUID hostID){gameSessionRequestList.clearByHostID(hostID);}

    public void clearRequestByUser(UUID userID, UUID gameSessionID){gameSessionRequestList.clearByUserAndGameSessionID(userID,gameSessionID);}


    public void addRequest(GameSessionRequest gameSessionRequest){gameSessionRequestList.addRequest(gameSessionRequest);}

    /**can throw nullPointerException **/
    public void addUserToSession(User user,UUID gameSessionID){
        for(GameSession gameSession: gameSessionList){
            if(gameSession.getID().equals(gameSessionID)){
                gameSession.addUser(user);
            }
        }
    }


    public List<GameSessionRequest> getRequestByUserID(UUID userID){return gameSessionRequestList.getRequestsByUserID(userID);}

    /**can throw nullPointerException **/
    public void deleteUserFromSession(User user, UUID gameSessionID){
        for(GameSession gameSession: gameSessionList){
            if(gameSession.getID().equals(gameSessionID)){
                gameSession.removeUser(user);
            }
        }
    }

    /**can throw nullPointerException **/
    public void deleteGameByGameID(UUID GameID){
        Game game = runningGameByID(GameID);
        runningGamesList.remove(game);

    };
}
