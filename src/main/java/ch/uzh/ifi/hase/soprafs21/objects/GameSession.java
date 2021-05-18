package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.controller.WSGameController;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameSession {

    Logger log = LoggerFactory.getLogger(WSGameController.class);

    private final UUID gameSessionId = UUID.randomUUID();
    private final UserService userService = GameEngine.instance().getUserService();
    private final String hostName;
    private List<User> userList= new ArrayList<>();
    private final List<User> invitedUsers = new ArrayList<>();

    public GameSession(User host){
        this.hostName = host.getUsername();
        userList.add(host);
    }

    public String getHostName() {
        return hostName;
    }

    public UUID getID(){return gameSessionId;}

    public List<User> getUserList(){return userList;}

    public List<User> getInvitedUsers() {
        return invitedUsers;
    }

    public boolean userInInvitedUsers(String userName){
        for(User u: invitedUsers){
            if(u.getUsername().equals(userName)){
                return true;
            }
        }
        return false;
    }

    public void addInvitedUser(User user){
        if(user != null){
            invitedUsers.add(user);
        }
    }

    public void deleteInvitedUser(User user){
        for(User u: invitedUsers){
            if(u.getUsername().equals(user.getUsername())) {
                invitedUsers.remove(u);
                userService.updateStatus(user.getToken(), UserStatus.Free);
                break;
            }
        }
    }

    public void addUser(User user) {
        if(user!=null){
            userList.add(user);
        }
    }

    public boolean userInHere(User user){
        if(user!=null) {
            for (User iterator : getUserList()) {
                if(iterator.getUsername().equals(user.getUsername())){
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteUser(User user) {
        if(user!=null){
            if(userInHere(user)&& !user.getUsername().equals(hostName)){
                userList.removeIf(iterator -> iterator.getUsername().equals(user.getUsername()));
                log.info("user was removed");
            }
        }
    }

    public boolean isHost(String username) {
        return username.equals(hostName);
    }
}
