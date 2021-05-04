package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.Card;
import ch.uzh.ifi.hase.soprafs21.objects.CardMove;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.Player;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.FriendDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.incoming.FriendRequestReceivedGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.incoming.FriendRequestSentGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserManagment.*;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    //Users

    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    User convertUserRegisterPostDTOtoEntity(UserRegisterPostDTO userRegisterPostDTO);

    @Mapping(source = "usernameOrEmail", target = "username")
    @Mapping(source = "password", target = "password")
    User convertUserLoginPostDTOtoEntity(UserLoginPostDTO userLoginPostDTO);

    @Mapping(source = "token", target = "token")
    @Mapping(source = "username", target = "username")
    UserLoginGetDTO convertEntityToUserLoginGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source="token", target="token")
    User convertUserLogOutPostDTOtoEntity(UserLogoutPostDTO userLogoutPostDTO);

    @Mapping(source = "username", target="username")
    WaitingRoomUserObjDTO convertUsertoWaitingRoomUserObjDTO(User user);


    //Game

    @Mapping(source = "playerName", target="playerName")
    @Mapping(source = "color", target="color")
    ChooseColorPlayerDTO convertPlayertoChooseColorPlayerDTO(Player player);

    @Mapping(source = "code", target = "code")
    GameCardDTO convertCardtoGameCardDTO(Card card);

    @Mapping(source = "moveName", target = "moveName")
    MoveDTO convertCardMovetoMoveDTO(CardMove cardMove);

    @Mapping(source = "marbleNr", target = "marbleId")
    MarbleDTO convertMarbletoMarbleDTO(Marble marble);


    //FriendRequests
    @Mapping(source = "senderName", target = "senderName")
    FriendRequestReceivedGetDTO convertFriendRequestToFriendRequestReceivedGetDTO(FriendRequest friendRequest);

    @Mapping(source = "receiverName", target = "receiverName")
    FriendRequestSentGetDTO convertFriendRequestToFriendRequestSentdGetDTO(FriendRequest friendRequest);

    @Mapping(source = "status", target = "status")
    @Mapping(source = "username",  target = "username")
    FriendDTO convertUserToFriendDTO(User user);
}
