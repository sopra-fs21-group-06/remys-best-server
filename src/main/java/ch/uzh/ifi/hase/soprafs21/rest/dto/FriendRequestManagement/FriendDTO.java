package ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

public class FriendDTO {
    private String username;
    private UserStatus status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
