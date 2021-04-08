package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.List;

public class GameSession {
    private int userCount;
    private List<User> userList;

    public int getUserCount() {
        return userCount;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
