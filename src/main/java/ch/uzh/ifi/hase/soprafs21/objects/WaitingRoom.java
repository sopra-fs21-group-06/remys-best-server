package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WaitingRoom {
    private int userCount;
    private List<User> userQueue;
    Logger log = LoggerFactory.getLogger(WaitingRoom.class);

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
    }

    public boolean userInHere(User user){
        for(User iterator: userQueue){
            if(iterator.getUsername().equals(user.getUsername())){
                return true;
            }
        }
        return false;
        /**if(user!=null){
            return userQueue.contains(user);
        }else{
            return false;
        }**/
    }
    public void removeUser(User userToRemove){
        if(userToRemove!=null){
            String usernameToRemove = userToRemove.getUsername();
            userQueue.removeIf(user -> user.getUsername().equals(usernameToRemove));
        }
    }

    public int addUser(User userToAdd){
        String usernameToAdd = userToAdd.getUsername();
        for(User user : userQueue) {
            if(user.getUsername().equals(usernameToAdd)) {
                return userQueue.size();
            }
        }

        userQueue.add(userToAdd);
        userCount++;
        return userQueue.size();
    }

    public synchronized List<User> getXNumberOfUsers(int numberOfUsers) throws Exception {
        List<User> usersToAddToGameSession = new ArrayList<>();
        if(getUserCount() < numberOfUsers){
            throw new Exception("There are too few users in WaitingRoom to fillUp");
        }
        for(int i = 0; i < numberOfUsers; i++){
            User newUserToAddToGamesession = userQueue.remove(0);
            usersToAddToGameSession.add(newUserToAddToGamesession);

        }
        return usersToAddToGameSession;
    }
}
