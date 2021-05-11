package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.WaitingRoomUserObjDTO;

import java.util.ArrayList;
import java.util.List;

public class GameSessionInvitedUsersDTO {
    List<WaitingRoomUserObjDTO> invitedUsers = new ArrayList<>();

    public List<WaitingRoomUserObjDTO> getInvitedUsers() {
        return invitedUsers;
    }

    public void setInvitedUsers(List<WaitingRoomUserObjDTO> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }
}
