package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.RequestStatus;
import ch.uzh.ifi.hase.soprafs21.entity.CompositeIds.FriendRequestID;
import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("friendRequestRepository")
public interface FriendRequestRepository extends JpaRepository<FriendRequest, FriendRequestID> {
    List<FriendRequest> findByReceiverNameAndRequestStatus(String receiverName, RequestStatus requestStatus);
    List<FriendRequest> findBySenderNameAndRequestStatus(String senderName, RequestStatus requestStatus);
    FriendRequest findBySenderNameAndReceiverName(String senderName, String receiverName);

    @Query(
            value = "SELECT * FROM FRIENDREQUEST f where f.requeststatus = 'ACCEPTED'",
            nativeQuery = true)
    List<FriendRequest> findAllAcceptedFriendRequests();



}
