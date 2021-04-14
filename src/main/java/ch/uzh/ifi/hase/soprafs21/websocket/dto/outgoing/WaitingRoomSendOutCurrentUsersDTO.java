package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.WaitingRoomUserObjDTO;

import java.util.List;

public class WaitingRoomSendOutCurrentUsersDTO {
    List<WaitingRoomUserObjDTO> currentUsers;

    public List<WaitingRoomUserObjDTO> getCurrentUsers() {
        return currentUsers;
    }

    public void setCurrentUsers(List<WaitingRoomUserObjDTO> currentUsers) {
        this.currentUsers = currentUsers;
    }
}
