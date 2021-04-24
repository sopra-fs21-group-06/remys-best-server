package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.WaitingRoomSid;

import java.util.ArrayList;

public class WaitingRoomSidService {

    //@Autowired
    private WaitingRoomSid waitingRoom;

    WaitingRoomSidService() {
        waitingRoom = WaitingRoomSid.getInstance();
        //ArrayList<User> usersInWaitingRoom = waitingRoom.getUsersInWaitingRoom();
        //Game game = Game();
        //GameService gameService = GameService();
    }

    /*
    Adds the user to the waiting room
     */
    public void addUser(User user) {
        //check if the waiting room is filled if so create a new game or else add the User to the waiting room
        if (this.isFull()) {
            // 1) open a new game by calling the game service and passing the value returned this.returnUsersInWaitingRoom()
            // 2) call this.clearWaitingRoom() which clears the waiting room
            // 3) adds the User to the waiting room
        }
        else {
            //add the User to waiting room
            ArrayList<User> usersInWaitingRoom = waitingRoom.getUsersInWaitingRoom();
            usersInWaitingRoom.add(user);
        }
    }

    /*
    Checks if the waiting room is full
     */
    public boolean isFull() {
        int numberOfUsers = waitingRoom.getUsersInWaitingRoom().size();
        return numberOfUsers == 4;
    }

    /*
    Clears the waiting room
    Note* only call this function if isFull() returns true
     */
    private void clearWaitingRoom() {
        waitingRoom.getUsersInWaitingRoom().clear();
    }

    /*
    Returns the current Users in the waiting room
     */
    public ArrayList<User> returnUsersInWaitingRoom() {
        return waitingRoom.getUsersInWaitingRoom();
    }
}