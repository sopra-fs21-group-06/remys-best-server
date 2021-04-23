package ch.uzh.ifi.hase.soprafs21.websocket.dto;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

public class ChooseColorPlayerDTO {
    private String playerName;
    private Color color;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
