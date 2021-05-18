package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

public class RejectUserDTO {
    private String username;

    public RejectUserDTO(String username){
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
