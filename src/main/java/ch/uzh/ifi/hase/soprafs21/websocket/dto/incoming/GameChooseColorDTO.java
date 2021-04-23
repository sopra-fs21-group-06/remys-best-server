package ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

public class GameChooseColorDTO {
    private String token;
    private String color;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
