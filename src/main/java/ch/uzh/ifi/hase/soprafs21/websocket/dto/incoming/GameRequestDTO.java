package ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming;

import java.util.UUID;

public class GameRequestDTO {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
