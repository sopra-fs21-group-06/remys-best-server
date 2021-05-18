package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

public class UserLeftGameSessionDTO {
    private final String username;
    public UserLeftGameSessionDTO(String username){
        this.username=username;
    }
}
