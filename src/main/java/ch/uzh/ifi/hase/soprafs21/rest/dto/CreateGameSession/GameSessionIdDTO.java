package ch.uzh.ifi.hase.soprafs21.rest.dto.CreateGameSession;

import java.util.UUID;

public class GameSessionIdDTO {
    private UUID gameSessionId;

    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
    }
}
