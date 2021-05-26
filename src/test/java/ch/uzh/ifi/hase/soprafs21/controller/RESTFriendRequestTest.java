package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.RequestStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.FriendDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.outgoing.FriendRequestCreatePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.outgoing.FriendRequestResponsePostDTO;
import ch.uzh.ifi.hase.soprafs21.service.FriendRequestService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RESTFriendRequestController.class)
public class RESTFriendRequestTest extends AbstractRESTControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private FriendRequestService friendRequestService;

    @Test
    public void getAllFriendRequestsTests() throws Exception {

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderName("Siddhant");
        friendRequest.setReceiverName("Pascal");
        friendRequest.setCreationDate("7 January, 2021");
        friendRequest.setRequestStatus(RequestStatus.PENDING);

        List<FriendRequest> allFriendRequests = Collections.singletonList(friendRequest);

        given(friendRequestService.getAllFriendRequests()).willReturn(allFriendRequests);

        MockHttpServletRequestBuilder getRequest = get("/friendrequests")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getAllSentFriendRequestsTests() throws Exception {

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderName("Pascal");
        friendRequest.setReceiverName("Siddhant");
        friendRequest.setCreationDate("7 Janurary, 2021");
        friendRequest.setRequestStatus(RequestStatus.PENDING);

        List<FriendRequest> allFriendRequests = Collections.singletonList(friendRequest);

        given(userService.convertTokenToUsername(Mockito.any())).willReturn(friendRequest.getReceiverName());
        given(friendRequestService.getFriendRequestsBySenderName(Mockito.any())).willReturn(allFriendRequests);

        MockHttpServletRequestBuilder getRequest = get("/friendrequests/sent")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "abcdef");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());

    }

    @Test
    public void getAllReceivedFriendRequestsTests() throws Exception {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderName("Pascal");
        friendRequest.setReceiverName("Siddhant");
        friendRequest.setCreationDate("7 Janurary, 2021");
        friendRequest.setRequestStatus(RequestStatus.PENDING);

        List<FriendRequest> allFriendRequests = Collections.singletonList(friendRequest);

        given(userService.convertTokenToUsername(Mockito.any())).willReturn(friendRequest.getReceiverName());
        given(friendRequestService.getFriendRequestsBySenderName(Mockito.any())).willReturn(allFriendRequests);

        MockHttpServletRequestBuilder getRequest = get("/friendrequests/received")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "abecef");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void getFriendsOfUser() throws Exception {

        FriendDTO friendDTO = new FriendDTO();
        friendDTO.setUsername("Siddhant");
        friendDTO.setStatus(UserStatus.Free);

        List<FriendDTO> allFriends = Collections.singletonList(friendDTO);

        given(userService.convertTokenToUsername(Mockito.any())).willReturn("abcd");
        given(friendRequestService.getFriendsOfUser(Mockito.any())).willReturn(allFriends);

        MockHttpServletRequestBuilder getRequest = get("/myfriends")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "abcd");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void createFriendRequestTest() throws Exception {

        FriendRequestCreatePostDTO friendRequestCreatePostDTO = new FriendRequestCreatePostDTO();
        friendRequestCreatePostDTO.setReceiverName("Siddhant");

        doNothing().when(friendRequestService).createFriendRequest(friendRequestCreatePostDTO, "abcdef");

        MockHttpServletRequestBuilder postRequest = post("/friendrequests")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "abcdef")
                .content(asJsonString(friendRequestCreatePostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void declineFriendRequest() throws Exception {

        FriendRequestResponsePostDTO friendRequestResponsePostDTO = new FriendRequestResponsePostDTO();
        friendRequestResponsePostDTO.setSenderName("Siddhant");

        doNothing().when(friendRequestService).processResponseFriendRequest(friendRequestResponsePostDTO, RequestStatus.DECLINED , "abcdef");

        MockHttpServletRequestBuilder postRequest = post("/friendrequests/decline")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "abcdef")
                .content(asJsonString(friendRequestResponsePostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void acceptFriendRequest() throws Exception {

        FriendRequestResponsePostDTO friendRequestResponsePostDTO = new FriendRequestResponsePostDTO();
        friendRequestResponsePostDTO.setSenderName("Siddhant");

        doNothing().when(friendRequestService).processResponseFriendRequest(friendRequestResponsePostDTO, RequestStatus.ACCEPTED , "abcdef");

        MockHttpServletRequestBuilder postRequest = post("/friendrequests/accept")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "abcdef")
                .content(asJsonString(friendRequestResponsePostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isOk());
    }
}
