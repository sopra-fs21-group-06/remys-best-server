package ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming;

public class MoveMarbleRequestDTO {
    private String token;
    private String code;
    private String moveName;


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

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }
}
