package ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming;

import java.util.UUID;

public class GameRequestDTO {
    private UUID gameSessionId;
    private String username;

    public String getUsername() {
        return username;
    }

    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
