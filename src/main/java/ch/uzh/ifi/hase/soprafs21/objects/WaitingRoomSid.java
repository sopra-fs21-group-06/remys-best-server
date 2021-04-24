package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.ArrayList;

/*
Singleton Waiting Room Class, it stores the current users in the waiting room
 */
public class WaitingRoomSid {

    private ArrayList<User> usersInWaitingRoom;

    /*
    configuration for setting this class as singleton
     */
    static WaitingRoomSid obj = new WaitingRoomSid();
    private WaitingRoomSid() {
        this.usersInWaitingRoom = new ArrayList<User>(4); //Size of waiting room is 4
    }

    public static WaitingRoomSid getInstance() {
        return obj;
    }

/*
    //Adds User to the waiting room

    public void addUser(User user) {
        this.usersInWaitingRoom.add(user);
    }


    //Checks if the waiting room is full

    public boolean isFull() {
        int numberOfUsers = this.usersInWaitingRoom.size();
        return numberOfUsers == 4;
    }


    //Clears the waiting room
    //Note* only call this function if isFull() returns true

    public void clearWaitingRoom() {
        this.usersInWaitingRoom.clear();
    }*/

    public ArrayList<User> getUsersInWaitingRoom() {
        return this.usersInWaitingRoom;
    }

}
