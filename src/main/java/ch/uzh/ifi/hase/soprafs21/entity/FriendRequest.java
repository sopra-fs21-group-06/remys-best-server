package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.entity.CompositeIds.FriendRequestID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(FriendRequestID.class)
public class FriendRequest {
    @Id
    private String senderId;

    @Id
    private String receiverId;

    @Column (nullable = false)
    private String RequestStatus;
}
