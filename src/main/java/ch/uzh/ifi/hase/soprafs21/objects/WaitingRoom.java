package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.List;

public class WaitingRoom {
    private int userCount;
    private List<User> userQueue;

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getUserCount() {
        return userCount;
    }

    public List<User> getUserQueue() {
        return userQueue;
    }

    public void setUserQueue(List<User> userQueue) {
        this.userQueue = userQueue;
    }
}
