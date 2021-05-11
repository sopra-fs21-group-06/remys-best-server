package ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleDTO;

import java.util.ArrayList;
import java.util.List;

public class CanPlayGetDTO {
    private List<String> cardCodes;

    public List<String> getCardCodes() {
        return cardCodes;
    }

    public void setCardCodes(List<String> cardCodes) {
        this.cardCodes = cardCodes;
    }
}
