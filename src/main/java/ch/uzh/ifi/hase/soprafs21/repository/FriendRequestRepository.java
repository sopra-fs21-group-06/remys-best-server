package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.CompositeIds.FriendRequestID;
import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("friendRequestRepository")
public interface FriendRequestRepository extends JpaRepository<FriendRequest, FriendRequestID> {
}
