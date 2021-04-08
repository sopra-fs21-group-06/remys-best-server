package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.List;
import java.util.UUID;

public class GameSession {
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
    private final UUID GameSessionId = UUID.randomUUID();
    private int userCount;
    private List<User> userList;

    public UUID getID(){return GameSessionId;}
    public int getUserCount(){return userCount;}
    public List<User> getUserList(){return userList;}
    public void setUserCount(int userCount){this.userCount = userCount;}
    public void setUserList(List<User> userList){this.userList = userList;}

<<<<<<< Updated upstream
    //Function is used in GameSessionEngineService to check if a list already contains a user
    public boolean userAlreadyExists(User user){
        for(User x: userList){if(x.equals(user)){return true;}}
        return false;
    }
    /**can throw nullPointerException **/
    //Function is used in GameSessionEngineService to add a user
    public void addUser(User user){userList.add(user);userCount++;}

    /**can throw nullPointerException **/
    //Function is used in GameSessionEngineService to remove a user
    public void deleteUser(User user){userList.remove(user);userCount--;}

    /**can throw nullPointerException **/
    public boolean userInHere(User user){return userList.contains(user);}

    /**can throw nullPointerException **/
=======

    //Function is used in GameSessionEngineService to check if a list already contains a user
    public boolean userAlreadyExists(User user){
        for(User x: userList){if(x.equals(user)){return true;}}
        return false;
    }

    /**can throw nullPointerException **/
    //Function is used in GameSessionEngineService to add a user
    public void addUser(User user){userList.add(user);userCount++;}

    /**can throw nullPointerException **/
    //Function is used in GameSessionEngineService to remove a user
    public void deleteUser(User user){userList.remove(user);userCount--;}

    /**can throw nullPointerException **/
    public boolean userInHere(User user){return userList.contains(user);}

    /**can throw nullPointerException **/
>>>>>>> Stashed changes
    public void removeUser(User user) {userList.remove(user);userCount--;
    }
}
