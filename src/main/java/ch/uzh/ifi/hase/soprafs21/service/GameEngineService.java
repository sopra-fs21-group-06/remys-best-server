package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;

public class GameEngineService {
    private final GameEngine gameEngine;

    public GameEngineService(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public int addUserToWaitingRoom(User user){

    };
    public Game createGameFromWaitingRoom(List<users> Users, GameSession gameSession){};
    public Game createGameFromGameSession(GameSession gameSession,List<users> Users){};
    public void deleteGameSession(GameSession gameSession){};
    public void userDisconnected(User user){};
    public void clearRequestsByHost(UUID hostID){};
    public void clearRequestByUser(UUID userID, UUID gameSessionID){};
    public void addRequest(GameSessionRequestService gameSessionRequest){};
    public void addUserToSession(User user){};
    public List<GameSessionRequestService> getRequestByUserID(UUID userID){};
    public void addUserToSession(User user){};
    public void deleteUserFromSession(User user){};
    public void deleteGameByGameID(User user){};







}
