package ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming;

public class GameCardExchangeObj {
    private String token;
    private String cardCode;

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
}
