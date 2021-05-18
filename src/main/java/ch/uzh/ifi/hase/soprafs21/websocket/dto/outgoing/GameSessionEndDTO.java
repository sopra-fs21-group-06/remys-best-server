package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

public class GameSessionEndDTO {
    public String getUsername() {
        return username;
    }

    public GameSessionEndDTO(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

}
