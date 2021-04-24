package ch.uzh.ifi.hase.soprafs21.websocket.dto;

public class GameCardDTO {
    private String cardCode;
    private String url;

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
