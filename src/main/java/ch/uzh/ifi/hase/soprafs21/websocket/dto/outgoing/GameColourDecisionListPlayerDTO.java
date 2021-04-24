package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.GameColourChangePlayer;

import java.util.List;

public class GameColourDecisionListPlayerDTO {
    private List<GameColourChangePlayer> player;

    public List<GameColourChangePlayer> getPlayer() {
        return player;
    }

    public void setPlayer(List<GameColourChangePlayer> player) {
        this.player = player;
    }
}
