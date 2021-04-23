package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class RESTUserController {

    private final UserService userService;

    RESTUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @PostMapping("/users/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public UserLoginGetDTO loginUser(@RequestBody UserLoginPostDTO userLoginPostDTO){
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserLoginPostDTOtoEntity(userLoginPostDTO);

        //update user
        User loggedInUser = userService.logInUser(userInput);

        // return UserGetDTO
        return DTOMapper.INSTANCE.convertEntityToUserLoginGetDTO(loggedInUser);
    }

    @PostMapping("/users/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void logoutUser(@RequestBody UserLogoutPostDTO userLogoutPostDTO){
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserLogOutPostDTOtoEntity(userLogoutPostDTO);

        //update user
        userService.logOutUser(userInput);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserLoginGetDTO createUser(@RequestBody UserRegisterPostDTO userRegisterPostDTO, HttpServletRequest request) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserRegisterPostDTOtoEntity(userRegisterPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        return DTOMapper.INSTANCE.convertEntityToUserLoginGetDTO(createdUser);
    }
}
