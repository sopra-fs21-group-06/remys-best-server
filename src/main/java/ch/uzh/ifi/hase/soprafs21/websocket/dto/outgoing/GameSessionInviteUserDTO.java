package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import java.util.UUID;

public class GameSessionInviteUserDTO {
    private String hostName;
    private UUID gameSessionId;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
    }
}
