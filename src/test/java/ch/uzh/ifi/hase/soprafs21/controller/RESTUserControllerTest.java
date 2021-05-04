package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserManagment.UserLoginPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserManagment.UserRegisterPostDTO;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */

@WebMvcTest(RESTUserController.class)
public class RESTUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setEmail("fioef@nfjnf.com");
        user.setToken("12345678");
        user.setId(45678L);
        user.setStatus(UserStatus.OFFLINE);

        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].password", is(user.getPassword())))
                .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
    }


    @Test
    public void createUser_validInput_userCreated() throws Exception {
        // given
        User user = new User();
        user.setUsername("iamsiddhantsahu");
        user.setPassword("abcd");
        user.setEmail("hello@siddhantsahu.com");

        UserRegisterPostDTO userRegisterPostDTO = new UserRegisterPostDTO();
        userRegisterPostDTO.setUsername("iamsiddhantsahu");
        userRegisterPostDTO.setPassword("abcd");
        userRegisterPostDTO.setEmail("hello@siddhantsahu.com");

        given(userService.createUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRegisterPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                //.andExpect(jsonPath("$.id", is(user.getId().intValue())))
                //.andExpect(jsonPath("$.name", is(user.getPassword())))
                .andExpect(jsonPath("$.token", is(user.getToken())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));
                //.andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }

    @Test
    public void createUser_invalidInput_userCreated() throws Exception {
        //given
        User user = new User();
        user.setUsername("iamsiddhantsahu");
        user.setPassword("abcd");
        user.setEmail("hello@siddhantsahu.com");

        UserRegisterPostDTO userRegisterPostDTO = new UserRegisterPostDTO();
        userRegisterPostDTO.setUsername("iamsiddhantsahu");
        userRegisterPostDTO.setPassword("abcd");
        userRegisterPostDTO.setEmail("hello@siddhantsahu.com");

        ResponseStatusException ex = new ResponseStatusException(HttpStatus.CONFLICT);
        given(userService.createUser(Mockito.any())).willThrow(ex);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRegisterPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    @Test
    public void loginUser_validInput_userLoggedIn() throws Exception {
        //given
        User user = new User();
        user.setUsername("iamsiddhantsahu");
        user.setPassword("abcd");
        user.setEmail("hello@siddhantsahu.com");

        UserLoginPostDTO userLoginPostDTO = new UserLoginPostDTO();
        userLoginPostDTO.setPassword("abcd");
        userLoginPostDTO.setUsernameOrEmail("iamsiddhantsahu");

        //ResponseStatusException ex = new ResponseStatusException(HttpStatus.CONFLICT);
        given(userService.logInUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userLoginPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isAccepted());
    }

    @Test
    public void loginUser_invalidInput_userLoggedIn() throws Exception {
        //given
        User user = new User();
        user.setUsername("iamsiddhantsahu");
        user.setPassword("abcd");
        user.setEmail("hello@siddhantsahu.com");

        UserLoginPostDTO userLoginPostDTO = new UserLoginPostDTO();
        userLoginPostDTO.setPassword("abcd");
        userLoginPostDTO.setUsernameOrEmail("iamsiddhantsahu");

        ResponseStatusException ex = new ResponseStatusException(HttpStatus.CONFLICT);
        given(userService.logInUser(Mockito.any())).willThrow(ex);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userLoginPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

//    @Test
//    public void logoutUser_validInput_userLoggedOut() throws Exception {
//        //given
//        User user = new User();
//        user.setUsername("iamsiddhantsahu");
//        user.setPassword("abcd");
//        user.setEmail("hello@siddhantsahu.com");
//
//        UserLoginPostDTO userLoginPostDTO = new UserLoginPostDTO();
//        userLoginPostDTO.setPassword("abcd");
//        userLoginPostDTO.setUsernameOrEmail("iamsiddhantsahu");
//
//        ResponseStatusException ex = new ResponseStatusException(HttpStatus.CONFLICT);
//        given(userService.logInUser(Mockito.any())).willThrow(ex);
//
//        // when/then -> do the request + validate the result
//        MockHttpServletRequestBuilder postRequest = post("/users/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userLoginPostDTO));
//
//        // then
//        mockMvc.perform(postRequest)
//                .andExpect(status().isConflict());
//    }


    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     */

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
