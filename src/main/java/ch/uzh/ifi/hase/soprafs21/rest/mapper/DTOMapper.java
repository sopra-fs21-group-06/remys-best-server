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
import ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing.GameSessionUserDTO;
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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "sessionIdentity", ignore = true)
    User convertUserRegisterPostDTOtoEntity(UserRegisterPostDTO userRegisterPostDTO);

    @Mapping(source = "usernameOrEmail", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "sessionIdentity", ignore = true)
    User convertUserLoginPostDTOtoEntity(UserLoginPostDTO userLoginPostDTO);

    @Mapping(source = "token", target = "token")
    @Mapping(source = "username", target = "username")
    UserLoginGetDTO convertEntityToUserLoginGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "username", target="username")
    WaitingRoomUserObjDTO convertUserToWaitingRoomUserObjDTO(User user);


    //Game
    @Mapping(source = "playerName", target="playerName")
    @Mapping(source = "color", target="color")
    ChooseColorPlayerDTO convertPlayerToChooseColorPlayerDTO(Player player);

    @Mapping(source = "code", target = "code")
    @Mapping(target = "idx", ignore = true)
    GameCardDTO convertCardToGameCardDTO(Card card);

    @Mapping(source = "moveName", target = "moveName")
    MoveDTO convertCardMoveToMoveDTO(CardMove cardMove);

    @Mapping(source = "marbleId", target = "marbleId")
    MarbleDTO convertMarbleToMarbleDTO(Marble marble);

    //FriendRequests
    @Mapping(source = "senderName", target = "senderName")
    FriendRequestReceivedGetDTO convertFriendRequestToFriendRequestReceivedGetDTO(FriendRequest friendRequest);

    @Mapping(source = "receiverName", target = "receiverName")
    FriendRequestSentGetDTO convertFriendRequestToFriendRequestSentGetDTO(FriendRequest friendRequest);

    @Mapping(source = "status", target = "status")
    @Mapping(source = "username",  target = "username")
    FriendDTO convertUserToFriendDTO(User user);

    @Mapping(source="username", target="username")
    GameSessionUserDTO convertUserToGameSessionUserDTO(User user);

    @Mapping(source="email", target="email")
    @Mapping(source="password", target="password")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "sessionIdentity", ignore = true)
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);
}
