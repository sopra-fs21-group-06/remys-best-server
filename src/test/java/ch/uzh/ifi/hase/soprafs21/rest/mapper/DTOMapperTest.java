package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserRegisterPostDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserRegisterPostDTO userRegisterPostDTO = new UserRegisterPostDTO();
        userRegisterPostDTO.setPassword("name");
        userRegisterPostDTO.setUsername("username");
        userRegisterPostDTO.setEmail("email");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserRegisterPostDTOtoEntity(userRegisterPostDTO);

        // check content
        assertEquals(userRegisterPostDTO.getPassword(), user.getPassword());
        assertEquals(userRegisterPostDTO.getUsername(), user.getUsername());
        assertEquals(userRegisterPostDTO.getEmail(), user.getEmail());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getPassword(), userGetDTO.getPassword());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }
}
