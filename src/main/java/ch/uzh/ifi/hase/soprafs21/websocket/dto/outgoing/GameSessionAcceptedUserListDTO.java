package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import java.util.List;

public class GameSessionAcceptedUserListDTO {
    private List<GameSessionUserDTO> acceptedUsers;
    private String startGame;

    public List<GameSessionUserDTO> getAcceptedUsers() {
        return acceptedUsers;
    }

    public void setAcceptedUsers(List<GameSessionUserDTO> acceptedUsers) {
        this.acceptedUsers = acceptedUsers;
    }

    public void setStartGame(String enoughUsers) {
        this.startGame=enoughUsers;
    }
}
