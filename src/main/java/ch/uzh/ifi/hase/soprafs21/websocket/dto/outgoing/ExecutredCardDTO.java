package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.GameCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleExecuteCardDTO;

import java.util.List;

public class ExecutredCardDTO {
    private String token;
    private GameCardDTO card;
    private List<MarbleExecuteCardDTO> marbleList;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public GameCardDTO getCard() {
        return card;
    }

    public void setCard(GameCardDTO card) {
        this.card = card;
    }

    public List<MarbleExecuteCardDTO> getMarbleList() {
        return marbleList;
    }

    public void setMarbleList(List<MarbleExecuteCardDTO> marbleList) {
        this.marbleList = marbleList;
    }
}
