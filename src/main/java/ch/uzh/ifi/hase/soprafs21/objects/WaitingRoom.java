package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WaitingRoom {
    private int userCount;
    private List<User> userQueue;

    public WaitingRoom(){
        userCount = 0;
        userQueue = new ArrayList<User>();
    }

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

    public List<User> getFirstFour(){
        userCount=0;
        return new ArrayList<User>(userQueue);
    };
    public boolean userInHere(User user){
        if(user!=null){
            return userQueue.contains(user);
        }else{
            return false;
        }
    }
    public void removeUser(User user){
        if(user!=null){
            userQueue.remove(user);
        }
    }

    public int addUser(User user){if(user!=null){userQueue.add(user);userCount++;return userQueue.size();}else{return -1;}}

}
