package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameSession {

    private final UUID GameSessionId = UUID.randomUUID();
    private final String hostName;
    private int userCount;
    private List<User> userList= new ArrayList<User>();

    public GameSession(User host){
        this.hostName = host.getUsername();
        userList.add(host);
    }

    public UUID getID(){return GameSessionId;}
    public int getUserCount(){return userCount;}
    public List<User> getUserList(){return userList;}
    public void setUserCount(int userCount){this.userCount = userCount;}
    public void setUserList(List<User> userList){this.userList = userList;}

    public boolean userAlreadyExists(User user){
        if(user==null){
            return false;
        }
        for(User x: userList){if(x.equals(user)){return true;}}
        return false;
    }

    public void addUser(User user){if(user!=null){userList.add(user);userCount++;};}

    public boolean userInHere(User user){if(user!=null){return userList.contains(user);}else{return false;}}

    public void deleteUser(User user) {if(user!=null){if(userInHere(user)&& !user.getUsername().equals(hostName)){userList.remove(user);userCount--;}}}

    public boolean isHost(String username) {
        return username.equals(hostName);
    }
}
