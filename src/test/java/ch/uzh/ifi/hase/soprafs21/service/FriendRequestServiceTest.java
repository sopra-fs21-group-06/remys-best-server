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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void getFriendsOfUserTest() {

        User user = new User();
        user.setStatus(UserStatus.Free);
        user.setUsername("Siddhant");

        User user_pascal = new User();
        user_pascal.setStatus(UserStatus.Free);
        user_pascal.setUsername("Pascal");

        User user_andrina = new User();
        user_andrina.setStatus(UserStatus.Free);
        user_andrina.setUsername("Andrina");

        List<FriendDTO> allFriends = new ArrayList<FriendDTO>();

        List<FriendRequest> senderFriends = new ArrayList<FriendRequest>();
        List<FriendRequest> receiverFriends = new ArrayList<FriendRequest>();

        FriendRequest test_FriendRequest = new FriendRequest();
        test_FriendRequest.setRequestStatus(RequestStatus.ACCEPTED);
        test_FriendRequest.setSenderName("Siddhant");
        test_FriendRequest.setReceiverName("Pascal");
        test_FriendRequest.setCreationDate("29 May 2021");

        senderFriends.add(test_FriendRequest);

        FriendRequest test_FriendRequest_2 = new FriendRequest();
        test_FriendRequest_2.setRequestStatus(RequestStatus.ACCEPTED);
        test_FriendRequest_2.setSenderName("Andrina");
        test_FriendRequest_2.setReceiverName("Siddhant");
        test_FriendRequest_2.setCreationDate("29 May 2021");

        receiverFriends.add(test_FriendRequest_2);

        Mockito.when(friendRequestRepository.findByReceiverNameAndRequestStatus(Mockito.any(), Mockito.any()))
                .thenReturn(senderFriends);
        Mockito.when(friendRequestRepository.findBySenderNameAndRequestStatus(Mockito.any(), Mockito.any()))
                .thenReturn(receiverFriends);

        Mockito.when(userService.findByUsername(Mockito.any()))
                .thenReturn(user);

        //allFriends.add(DTOMapper.INSTANCE.convertUserToFriendDTO(user));
        allFriends.add(DTOMapper.INSTANCE.convertUserToFriendDTO(user_pascal));
        allFriends.add(DTOMapper.INSTANCE.convertUserToFriendDTO(user_andrina));

        List<FriendDTO> response = friendRequestService.getFriendsOfUser("Siddhant");

        assertEquals(response.size(), allFriends.size());
    }

    @Test
    public void checkIfSenderAndReceiverExistTest_SameSenderAndReceiver() {

        User user_sid_sender = new User();
        user_sid_sender.setUsername("Siddhant");
        user_sid_sender.setStatus(UserStatus.Free);
        user_sid_sender.setToken("abcd");

        User user_sid_receiver = new User();
        user_sid_receiver.setUsername("Siddhant");
        user_sid_receiver.setStatus(UserStatus.Free);

        //userService.createUser(user_sid_sender);
        //userService.createUser(user_sid_receiver);

        //Mockito.doNothing().when(userService).getUserRepository();
        //Mockito.when(userService.getUserRepository().findByToken(Mockito.any()))
        //        .thenReturn(user_sid_sender);
        //Mockito.doNothing().when(userService).getUserRepository().findByToken(Mockito.any());
        Mockito.when(userService.getUserRepository())
                .thenReturn(userRepository);
        Mockito.when(userRepository.findByToken(user_sid_sender.getToken()))
                .thenReturn(user_sid_sender);
        Mockito.when(userRepository.findByUsername(user_sid_receiver.getUsername()))
                .thenReturn(user_sid_receiver);

        //userService.getUserRepository().deleteAll();

        assertThrows(ResponseStatusException.class, () -> friendRequestService.checkIfSenderAndReceiverExist(user_sid_sender.getToken(), user_sid_receiver.getUsername()));
        //friendRequestService.checkIfSenderAndReceiverExist(user_sid_sender.getToken(), user_sid_receiver.getUsername());
    }

    @Test
    public void checkIfSenderAndReceiverExistTest_SenderAndReceiverNull() {

        User user_sid_sender = new User();
        user_sid_sender.setUsername("Siddhant");
        user_sid_sender.setStatus(UserStatus.Free);
        user_sid_sender.setToken("abcd");

        User user_sid_receiver = new User();
        user_sid_receiver.setUsername("Siddhant");
        user_sid_receiver.setStatus(UserStatus.Free);

        //userService.createUser(user_sid_sender);
        //userService.createUser(user_sid_receiver);

        //Mockito.doNothing().when(userService).getUserRepository();
        //Mockito.when(userService.getUserRepository().findByToken(Mockito.any()))
        //        .thenReturn(user_sid_sender);
        //Mockito.doNothing().when(userService).getUserRepository().findByToken(Mockito.any());
        Mockito.when(userService.getUserRepository())
                .thenReturn(userRepository);
        Mockito.when(userRepository.findByToken(user_sid_sender.getToken()))
                .thenReturn(null);
        Mockito.when(userRepository.findByUsername(user_sid_receiver.getUsername()))
                .thenReturn(null);

        //userService.getUserRepository().deleteAll();

        assertThrows(ResponseStatusException.class, () -> friendRequestService.checkIfSenderAndReceiverExist(user_sid_sender.getToken(), user_sid_receiver.getUsername()));
        //friendRequestService.checkIfSenderAndReceiverExist(user_sid_sender.getToken(), user_sid_receiver.getUsername());
    }

    @Test
    public void checkIfSenderAndReceiverExistTest_SenderNull() {

        User user_sid_sender = new User();
        user_sid_sender.setUsername("Siddhant");
        user_sid_sender.setStatus(UserStatus.Free);
        user_sid_sender.setToken("abcd");

        User user_sid_receiver = new User();
        user_sid_receiver.setUsername("Siddhant");
        user_sid_receiver.setStatus(UserStatus.Free);

        //userService.createUser(user_sid_sender);
        //userService.createUser(user_sid_receiver);

        //Mockito.doNothing().when(userService).getUserRepository();
        //Mockito.when(userService.getUserRepository().findByToken(Mockito.any()))
        //        .thenReturn(user_sid_sender);
        //Mockito.doNothing().when(userService).getUserRepository().findByToken(Mockito.any());
        Mockito.when(userService.getUserRepository())
                .thenReturn(userRepository);
        Mockito.when(userRepository.findByToken(user_sid_sender.getToken()))
                .thenReturn(null);
        Mockito.when(userRepository.findByUsername(user_sid_receiver.getUsername()))
                .thenReturn(user_sid_receiver);

        //userService.getUserRepository().deleteAll();

        assertThrows(ResponseStatusException.class, () -> friendRequestService.checkIfSenderAndReceiverExist(user_sid_sender.getToken(), user_sid_receiver.getUsername()));
        //friendRequestService.checkIfSenderAndReceiverExist(user_sid_sender.getToken(), user_sid_receiver.getUsername());
    }

    @Test
    public void checkIfSenderAndReceiverExistTest_ReceiverNull() {

        User user_sid_sender = new User();
        user_sid_sender.setUsername("Siddhant");
        user_sid_sender.setStatus(UserStatus.Free);
        user_sid_sender.setToken("abcd");

        User user_sid_receiver = new User();
        user_sid_receiver.setUsername("Siddhant");
        user_sid_receiver.setStatus(UserStatus.Free);

        //userService.createUser(user_sid_sender);
        //userService.createUser(user_sid_receiver);

        //Mockito.doNothing().when(userService).getUserRepository();
        //Mockito.when(userService.getUserRepository().findByToken(Mockito.any()))
        //        .thenReturn(user_sid_sender);
        //Mockito.doNothing().when(userService).getUserRepository().findByToken(Mockito.any());
        Mockito.when(userService.getUserRepository())
                .thenReturn(userRepository);
        Mockito.when(userRepository.findByToken(user_sid_sender.getToken()))
                .thenReturn(user_sid_sender);
        Mockito.when(userRepository.findByUsername(user_sid_receiver.getUsername()))
                .thenReturn(null);

        //userService.getUserRepository().deleteAll();

        assertThrows(ResponseStatusException.class, () -> friendRequestService.checkIfSenderAndReceiverExist(user_sid_sender.getToken(), user_sid_receiver.getUsername()));
        //friendRequestService.checkIfSenderAndReceiverExist(user_sid_sender.getToken(), user_sid_receiver.getUsername());
    }
}
