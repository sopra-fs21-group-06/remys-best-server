package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

public class RequestCountDownDTO {
    private String userName;
    private int currentCounter;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCurrentCounter() {
        return currentCounter;
    }

    public void setCurrentCounter(int currentCounter) {
        this.currentCounter = currentCounter;
    }
}
