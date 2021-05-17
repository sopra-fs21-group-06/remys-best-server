package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

public class TurnCountDownDTO {
    private String username;
    private int currentCounter;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCurrentCounter() {
        return currentCounter;
    }

    public void setCurrentCounter(int currentCounter) {
        this.currentCounter = currentCounter;
    }
}
