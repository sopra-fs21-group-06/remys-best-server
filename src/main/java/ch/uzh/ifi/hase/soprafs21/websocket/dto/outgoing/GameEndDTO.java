package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import java.util.List;

public class GameEndDTO {
    List<String> won;
    String aborted;

    public List<String> getWon() {
        return won;
    }

    public void setWon(List<String> won) {
        this.won = won;
    }

    public String getAborted() {
        return aborted;
    }

    public void setAborted(String aborted) {
        this.aborted = aborted;
    }
}
