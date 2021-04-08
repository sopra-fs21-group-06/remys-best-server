package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameSession;

public class GameSessionEngineService {
    private final GameSession gameSession;

    public GameSessionEngineService(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public void addUserToSession(User user){

    }
    public void deleteUserFromSession(User user){}

}
