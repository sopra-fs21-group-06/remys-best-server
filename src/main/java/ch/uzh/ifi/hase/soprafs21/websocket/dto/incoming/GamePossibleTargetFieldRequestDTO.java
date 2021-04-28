package ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming;

public class GamePossibleTargetFieldRequestDTO {
    private String token;
    private String cardCode;
    private String marbleID;
    private String moveName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getMarbleID() {
        return marbleID;
    }

    public void setMarbleID(String marbleID) {
        this.marbleID = marbleID;
    }

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }
}
