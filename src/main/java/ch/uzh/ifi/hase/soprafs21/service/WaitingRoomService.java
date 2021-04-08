package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.WaitingRoom;

import java.util.List;

public class WaitingRoomService {
    private final WaitingRoom waitingRoom;

    public WaitingRoomService(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
    }
    public List<User> getFirstFourUsers(){}
    public void enqueueWaitingRoom(User user){}
    public User dequeueWaitingRoom(){}
}
