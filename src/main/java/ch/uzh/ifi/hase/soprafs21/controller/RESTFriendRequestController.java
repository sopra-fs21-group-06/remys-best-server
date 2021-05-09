package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.RequestStatus;
import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.FriendDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.outgoing.FriendListGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.outgoing.FriendRequestCreatePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.outgoing.FriendRequestResponsePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.incoming.FriendRequestReceivedGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.incoming.FriendRequestSentGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.FriendRequestService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RestController
public class RESTFriendRequestController {

    private final FriendRequestService friendRequestService;


    RESTFriendRequestController(FriendRequestService friendRequestService, UserService userService) {
        this.friendRequestService = friendRequestService;
    }

    @GetMapping("/friendrequests")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FriendRequest> getAllFriendRequests() {
        // fetch all friendRequests in the internal representation
        return friendRequestService.getAllFriendRequests();
    }

    @GetMapping("/friendrequests/received")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FriendRequestReceivedGetDTO> getAllReceivedFriendRequests(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        String userName = DogUtils.convertTokenToUsername(token, friendRequestService.getUserService());

        // fetch all users in the internal representation
        List<FriendRequest> friendRequests = friendRequestService.getFriendRequestsByReceiverName(userName);
        List<FriendRequestReceivedGetDTO> friendRequestReceivedGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (FriendRequest f : friendRequests) {
            friendRequestReceivedGetDTOs.add(DTOMapper.INSTANCE.convertFriendRequestToFriendRequestReceivedGetDTO(f));
        }
        return friendRequestReceivedGetDTOs;
    }

    @GetMapping("/friendrequests/sent")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FriendRequestSentGetDTO> getAllSentFriendRequests(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        String userName = DogUtils.convertTokenToUsername(token, friendRequestService.getUserService());

        // fetch all users in the internal representation
        List<FriendRequest> friendRequests = friendRequestService.getFriendRequestsBySenderName(userName);
        List<FriendRequestSentGetDTO> friendRequestSentGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (FriendRequest f : friendRequests) {
            friendRequestSentGetDTOs.add(DTOMapper.INSTANCE.convertFriendRequestToFriendRequestSentdGetDTO(f));
        }
        return friendRequestSentGetDTOs;
    }


    @GetMapping("/myfriends")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public FriendListGetDTO getFriendsOfUser(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        String userName = DogUtils.convertTokenToUsername(token, friendRequestService.getUserService());

        FriendListGetDTO friendListGetDTO = new FriendListGetDTO();
        List<FriendDTO> friends = friendRequestService.getFriendsOfUser(userName);

        friendListGetDTO.setFriends(friends);

        return friendListGetDTO;
    }


    //TODO Token from header

    @PostMapping("/friendrequests")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createFriendRequest(@RequestBody FriendRequestCreatePostDTO friendRequestCreatePostDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        friendRequestService.createFriendRequest(friendRequestCreatePostDTO, token);
    }

    @PostMapping("/friendrequests/decline")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void declineFriendRequest(@RequestBody FriendRequestResponsePostDTO friendRequestResponsePostDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        friendRequestService.processResponseFriendRequest(friendRequestResponsePostDTO, RequestStatus.DECLINED, token);
    }

    @PostMapping("/friendrequests/accept")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void acceptFriendRequest(@RequestBody FriendRequestResponsePostDTO friendRequestResponsePostDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        friendRequestService.processResponseFriendRequest(friendRequestResponsePostDTO, RequestStatus.ACCEPTED, token);
    }
}
