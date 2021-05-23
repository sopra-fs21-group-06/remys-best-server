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
    private final UserService userService;
    private final String hostName;
    private final List<User> acceptedUsers = new ArrayList<>();
    private final List<User> invitedUsers = new ArrayList<>();

    public GameSession(User host, UserService userService){
        this.hostName = host.getUsername();
        acceptedUsers.add(host);
        this.userService = userService;
    }

    public String getHostName() {
        return hostName;
    }

    public UUID getID(){return gameSessionId;}

    public List<User> getAcceptedUsers(){return acceptedUsers;}

    public List<User> getInvitedUsers() {
        return invitedUsers;
    }

    public boolean isInvitedUserInHere(String userName){
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
            userService.updateStatus(user.getToken(), UserStatus.Busy);
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

    public void addAcceptedUser(User user) {
        if(user!=null && !user.getUsername().equals(hostName)){
            deleteInvitedUser(user);
            acceptedUsers.add(user);
            userService.updateStatus(user.getToken(), UserStatus.Busy);
        }
    }

    public boolean isAcceptedUserInHere(User user){
        if(user!=null) {
            for (User iterator : getAcceptedUsers()) {
                if(iterator.getUsername().equals(user.getUsername())){
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteAcceptedUser(User user) {
        if(user!=null){
            if(isAcceptedUserInHere(user)&& !user.getUsername().equals(hostName)){
                acceptedUsers.removeIf(iterator -> iterator.getUsername().equals(user.getUsername()));
                log.info("user was removed");
                userService.updateStatus(user.getToken(), UserStatus.Free);
            }
        }
    }

    public boolean isHost(String username) {
        return username.equals(hostName);
    }
}
