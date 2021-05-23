package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.RequestStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.FriendRequestRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.FriendDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.outgoing.FriendRequestCreatePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FriendRequestServiceTest {

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendRequestService friendRequestService;

    private FriendRequest testFriendRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testFriendRequest = new FriendRequest();
        testFriendRequest.setSenderName("Siddhant");
        testFriendRequest.setReceiverName("Pascal");
        testFriendRequest.setRequestStatus(RequestStatus.PENDING);
        testFriendRequest.setCreationDate("21 May 2021");

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(friendRequestRepository.save(Mockito.any())).thenReturn(testFriendRequest);
    }

    @Test
    public void getAllFriendRequestsTest() {

        List<FriendRequest> friendRequestList = new ArrayList<FriendRequest>();
        friendRequestList.add(testFriendRequest);
        Mockito.when(friendRequestRepository.findAll()).thenReturn(friendRequestList);

        List<FriendRequest>response = friendRequestService.getAllFriendRequests();

        Mockito.verify(friendRequestRepository, Mockito.times(1)).findAll();

        assertEquals(response.size(), friendRequestList.size());
        assertEquals(response.get(0).getReceiverName(), friendRequestList.get(0).getReceiverName());
        assertEquals(response.get(0).getRequestStatus(), friendRequestList.get(0).getRequestStatus());
        assertEquals(response.get(0).getSenderName(), friendRequestList.get(0).getSenderName());
        assertEquals(response.get(0).getCreationDate(), friendRequestList.get(0).getCreationDate());
    }

    @Test
    public void getFriendRequestsByReceiverNameTest() {

        List<FriendRequest> friendRequestList = new ArrayList<FriendRequest>();
        friendRequestList.add(testFriendRequest);

        Mockito.when(friendRequestRepository.findByReceiverNameAndRequestStatus(Mockito.anyString(), Mockito.any()))
                .thenReturn(friendRequestList);

        List<FriendRequest>response = friendRequestService.getFriendRequestsByReceiverName("Pascal");

        Mockito.verify(friendRequestRepository, Mockito.times(1))
                .findByReceiverNameAndRequestStatus("Pascal", RequestStatus.PENDING);

        assertEquals(response.size(), friendRequestList.size());
    }

    @Test
    public void getFriendRequestsBySenderNameTest() {
        List<FriendRequest> friendRequestList = new ArrayList<FriendRequest>();
        friendRequestList.add(testFriendRequest);

        Mockito.when(friendRequestRepository.findBySenderNameAndRequestStatus(Mockito.anyString(), Mockito.any()))
                .thenReturn(friendRequestList);

        List<FriendRequest>response = friendRequestService.getFriendRequestsBySenderName("Siddhant");

        Mockito.verify(friendRequestRepository, Mockito.times(1))
                .findBySenderNameAndRequestStatus("Siddhant", RequestStatus.PENDING);

        assertEquals(response.size(), friendRequestList.size());
    }
}
