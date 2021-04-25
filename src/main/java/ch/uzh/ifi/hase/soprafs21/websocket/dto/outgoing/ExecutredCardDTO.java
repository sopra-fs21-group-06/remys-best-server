package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.GameCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleExecuteCardDTO;

import java.util.List;

public class ExecutredCardDTO {
    private String username;
    private GameCardDTO card;
    private List<MarbleExecuteCardDTO> marbles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GameCardDTO getCard() {
        return card;
    }

    public void setCard(GameCardDTO card) {
        this.card = card;
    }

    public List<MarbleExecuteCardDTO> getMarbles() {
        return marbles;
    }

    public void setMarbles(List<MarbleExecuteCardDTO> marbles) {
        this.marbles = marbles;
    }
}
