package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

public class GameNotificationDTO {
    private String playerName;
    private String action;
    private String card;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
