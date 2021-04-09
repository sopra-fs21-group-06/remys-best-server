package ch.uzh.ifi.hase.soprafs21.entity.CompositeIds;

import javax.persistence.Embeddable;
import java.io.Serializable;

public class FriendRequestID implements Serializable {
    private String senderId;
    private String receiverId;
}
