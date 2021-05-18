package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;

import java.util.UUID;


public class GameSessionIdDTO {
    private UUID gameSessionId;

    public GameSessionIdDTO(String username){
       this.gameSessionId = GameEngine.instance().findGameSessionIdByUsername(username);
    }
    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(UUID gameSession) {
        gameSessionId = gameSession;
    }

}
