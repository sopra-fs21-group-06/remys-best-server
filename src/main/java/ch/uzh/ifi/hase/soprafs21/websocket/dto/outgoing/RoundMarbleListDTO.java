package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleDTO;

import java.util.List;

public class RoundMarbleListDTO {
    private List<MarbleDTO> marbles;

    public List<MarbleDTO> getMarbles() {
        return marbles;
    }

    public void setMarbles(List<MarbleDTO> marbles) {
        this.marbles = marbles;
    }
}
