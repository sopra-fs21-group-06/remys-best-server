package ch.uzh.ifi.hase.soprafs21.entity.CompositeIds;

import javax.persistence.Embeddable;
import java.io.Serializable;

public class FriendRequestID implements Serializable {
    private String senderName;
    private String receiverName;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
