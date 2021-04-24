package ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming;

public class CardMoveRequestDTO {
    private String token;
    private String code;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
