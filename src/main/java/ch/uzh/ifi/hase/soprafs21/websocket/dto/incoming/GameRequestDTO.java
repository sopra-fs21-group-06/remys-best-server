package ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming;

public class GameRequestDTO {
    private String token;
    private String username;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
