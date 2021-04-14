package ch.uzh.ifi.hase.soprafs21.websocket.dto;

public class GameColourChangePlayer {
    private String username;
    private String colour;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
