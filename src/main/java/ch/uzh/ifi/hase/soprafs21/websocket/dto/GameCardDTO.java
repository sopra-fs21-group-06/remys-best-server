package ch.uzh.ifi.hase.soprafs21.websocket.dto;

public class GameCardDTO {
    private String code;
    private int idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
