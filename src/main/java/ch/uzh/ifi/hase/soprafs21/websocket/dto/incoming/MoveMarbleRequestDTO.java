package ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming;

public class MoveMarbleRequestDTO {
    private String token;
    private String cardCode;
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

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }
}
