package ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming;

import java.util.UUID;

public class GameRequestDeniedDTO {
    private UUID gameSessionId;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
    }
}
