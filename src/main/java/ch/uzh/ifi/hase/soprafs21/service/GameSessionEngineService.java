package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameSession;

public class GameSessionEngineService {
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
    private final GameSession gameSession;

    public GameSessionEngineService(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public void addUserToSession(User user){
        if(!this.gameSession.userAlreadyExists(user)){gameSession.addUser(user);}
    }
    public void deleteUserFromSession(User user){
        if(this.gameSession.userAlreadyExists(user)){gameSession.deleteUser(user);}
    }

}
