package ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleDTO;

import java.util.ArrayList;
import java.util.List;

public class CanPlayGetDTO {
    private String cardCode;
    private String moveName;
    private List<Integer> marbles;

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }

    public List<Integer> getMarbles() {
        return marbles;
    }

    public void setMarbles(List<Integer> marbles) {
        this.marbles = marbles;
    }
}
