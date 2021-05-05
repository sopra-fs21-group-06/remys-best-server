package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.constant.RequestStatus;
import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.FriendRequestRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.FriendDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.outgoing.FriendRequestCreatePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.outgoing.FriendRequestResponsePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FriendRequestService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;

    @Autowired
    public FriendRequestService(@Qualifier("friendRequestRepository") FriendRequestRepository friendRequestRepository, UserService userService) {
        this.friendRequestRepository = friendRequestRepository;
        this.userService = userService;
    }


    public List<FriendRequest> getAllFriendRequests(){
        return friendRequestRepository.findAll();
    }

    public List<FriendRequest> getFriendRequestsByReceiverName(String receiverName){
        return friendRequestRepository.findByReceiverNameAndRequestStatus(receiverName, RequestStatus.PENDING);
    }

    public List<FriendRequest> getFriendRequestsBySenderName(String senderName){
        return friendRequestRepository.findBySenderNameAndRequestStatus(senderName, RequestStatus.PENDING);
    }

    public List<FriendDTO> getFriendsOfUser(String userName){
        List<FriendDTO> allFriends = new ArrayList<>();

        List<FriendRequest> senderFriends = new ArrayList<>(friendRequestRepository.findByReceiverNameAndRequestStatus(userName, RequestStatus.ACCEPTED));
        List<FriendRequest> receiverFriends = new ArrayList<>(friendRequestRepository.findBySenderNameAndRequestStatus(userName, RequestStatus.ACCEPTED));

        for(FriendRequest f : senderFriends){
            allFriends.add(DTOMapper.INSTANCE.convertUserToFriendDTO(userService.findByUsername(f.getSenderName())));
        }
        for(FriendRequest f: receiverFriends){
            allFriends.add(DTOMapper.INSTANCE.convertUserToFriendDTO(userService.findByUsername(f.getReceiverName())));
        }
        return allFriends;
    }

    public void createFriendRequest(FriendRequestCreatePostDTO friendRequestCreatePostDTO){

        checkIfSenderAndReceiverExist(friendRequestCreatePostDTO.getToken(), friendRequestCreatePostDTO.getReceiverName());

        String senderName = DogUtils.convertTokenToUsername(friendRequestCreatePostDTO.getToken(), userService);
        String receiverName = friendRequestCreatePostDTO.getReceiverName();

        if(!checkIfFriendRequestAlreadyExists(friendRequestCreatePostDTO.getToken(), friendRequestCreatePostDTO.getReceiverName())){
            String tmp;
            tmp = senderName;
            senderName = receiverName;
            receiverName = tmp;
        }
        
        FriendRequest newFriendRequest = new FriendRequest();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate= formatter.format(date);

        newFriendRequest.setSenderName(senderName);
        newFriendRequest.setReceiverName(receiverName);
        newFriendRequest.setCreationDate(strDate);
        newFriendRequest.setRequestStatus(RequestStatus.PENDING);

        friendRequestRepository.saveAndFlush(newFriendRequest);

    }

    public void processResponseFriendRequest(FriendRequestResponsePostDTO friendRequestResponsePostDTO, RequestStatus requestStatus){
        checkIfSenderAndReceiverExist(friendRequestResponsePostDTO.getToken(), friendRequestResponsePostDTO.getSenderName());
        FriendRequest requestToUpdate = checkIfFriendRequestExists(friendRequestResponsePostDTO.getToken(), friendRequestResponsePostDTO.getSenderName());
        requestToUpdate.setRequestStatus(requestStatus);
        friendRequestRepository.saveAndFlush(requestToUpdate);
    }


    private void checkIfSenderAndReceiverExist(String senderToken, String receiverName) {
        User sender = userService.getUserRepository().findByToken(senderToken);
        User receiver = userService.getUserRepository().findByUsername(receiverName);

        String inputErrorMessage = "The %s provided %s exist.";

        if (sender == null && receiver == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(inputErrorMessage, "Sender and the Receiver", "dont"));
        }
        else if (sender == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(inputErrorMessage, "Sender", "doesnt"));
        }
        else if (receiver == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(inputErrorMessage, "Receiver", "doesnt"));
        }
        else if(sender.getUsername().equals(receiverName)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender and Receiver cant be the same person.");
        }
    }

    private boolean checkIfFriendRequestAlreadyExists(String senderToken, String receiverName){

        User sender = userService.getUserRepository().findByToken(senderToken);
        User receiver = userService.getUserRepository().findByUsername(receiverName);

        FriendRequest friendRequestExists = friendRequestRepository.findBySenderNameAndReceiverName(sender.getUsername(), receiverName);
        FriendRequest friendRequestExistsInverted = friendRequestRepository.findBySenderNameAndReceiverName(receiverName, sender.getUsername());

        String AlreadyExistErrorMessage = "The FriendRequest is already created and still pending";
        if (friendRequestExists != null && friendRequestExists.getRequestStatus() == RequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AlreadyExistErrorMessage);
        }
        else if(friendRequestExists != null && friendRequestExists.getRequestStatus() == RequestStatus.ACCEPTED){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Looks like the guy is already your friend");
        }
        else if(friendRequestExistsInverted != null && friendRequestExistsInverted.getRequestStatus() == RequestStatus.PENDING){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Looks like your friend was faster. Your friend already sent you a friendRequest");
        }
        else if(friendRequestExistsInverted != null && friendRequestExistsInverted.getRequestStatus() == RequestStatus.ACCEPTED){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You already accepted his/her friendrequest.");
        }

        return friendRequestExistsInverted == null;
    }

    private FriendRequest checkIfFriendRequestExists(String senderToken, String senderName){
        User sender = userService.getUserRepository().findByToken(senderToken);
        User receiver = userService.getUserRepository().findByUsername(senderName);

        FriendRequest friendRequestExists = friendRequestRepository.findBySenderNameAndReceiverName(sender.getUsername(), senderName);
        FriendRequest friendRequestExistsInverted = friendRequestRepository.findBySenderNameAndReceiverName(senderName, sender.getUsername());

        String DoesntExistErrorMessage = "The Friendrequest doesnt exist";

        if(friendRequestExists == null && friendRequestExistsInverted == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DoesntExistErrorMessage);
        }
        return friendRequestExists == null ? friendRequestExistsInverted : friendRequestExists;
    }


    public UserService getUserService() {
        return userService;
    }
}
