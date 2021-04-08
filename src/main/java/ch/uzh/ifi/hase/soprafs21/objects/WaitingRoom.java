package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.List;
import java.util.ArrayList;

public class WaitingRoom {
    private int userCount;
    private List<User> userQueue;

    /** here we do not need to specifically look for the first four since this method is only called when the size is exactly four **/
    public List<User> getFirstFour(){
        return (List<User>) new ArrayList<User>(userQueue);
    };
    public boolean userInHere(User user){
        return userQueue.contains(user);
    }
    public void removeUser(User user){
        userQueue.remove(user);
    }

    public int addUser(User user){userQueue.add(user);userCount++;return userQueue.size();}
    
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
