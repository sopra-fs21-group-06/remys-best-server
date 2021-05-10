package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import java.util.List;

public class GameThrowAwayDTO {
    private String playerName;
    private List<String> cardCodes;


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<String> getCardCodes() {
        return cardCodes;
    }

    public void setCardCodes(List<String> cardcodes) {
        this.cardCodes = cardcodes;
    }
}
