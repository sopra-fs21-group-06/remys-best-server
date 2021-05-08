package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.RequestStatus;
import ch.uzh.ifi.hase.soprafs21.entity.CompositeIds.FriendRequestID;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "FRIENDREQUEST")
@IdClass(FriendRequestID.class)
public class FriendRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String senderName;

    @Id
    private String receiverName;


    @Column(nullable = false)
    private String creationDate;

    @Column (nullable = false)
    private RequestStatus requestStatus;


    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
