package ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.outgoing;

import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.FriendDTO;

import java.util.List;

public class FriendListGetDTO {
    List<FriendDTO> friends;

    public List<FriendDTO> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendDTO> friends) {
        this.friends = friends;
    }
}
