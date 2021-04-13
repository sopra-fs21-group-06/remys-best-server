package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Handles WaitingRoom functionalities (queue, dequeue etc)
 */
@Service
@Transactional
public class WaitingRoomService {

    Logger log = LoggerFactory.getLogger(WaitingRoomService.class);

    public WaitingRoomService() {

    }


    //public List<User> getFirstFourUsers(){};
    public synchronized void enqueueWaitingRoom(User user){}
    //public synchronized User dequeueWaitingRoom(){};
}
