package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.List;

public class GameSessionUserListDTO {
    private List<GameSessionUserDTO> users;
    private String startGame;

    public List<GameSessionUserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<GameSessionUserDTO> users) {
        this.users = users;
    }

    public void setStartGame(String enoughUsers) {
        this.startGame=enoughUsers;
    }
}
