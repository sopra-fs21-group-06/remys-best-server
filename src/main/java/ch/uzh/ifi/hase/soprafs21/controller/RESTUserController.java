package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserManagment.*;
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

    public RESTUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserLoginGetDTO createUser(@RequestBody UserRegisterPostDTO userRegisterPostDTO, HttpServletRequest request) {
        User userInput = DTOMapper.INSTANCE.convertUserRegisterPostDTOtoEntity(userRegisterPostDTO);
        User createdUser = userService.createUser(userInput);
        return DTOMapper.INSTANCE.convertEntityToUserLoginGetDTO(createdUser);
    }

    @PutMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void modifyUser(@RequestBody UserPutDTO userPutDTO){
        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        userService.updateUser(user);
    }

    @PostMapping("/users/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public UserLoginGetDTO loginUser(@RequestBody UserLoginPostDTO userLoginPostDTO){
        User userInput = DTOMapper.INSTANCE.convertUserLoginPostDTOtoEntity(userLoginPostDTO);
        User loggedInUser = userService.logInUser(userInput);
        return DTOMapper.INSTANCE.convertEntityToUserLoginGetDTO(loggedInUser);
    }

    @PostMapping("/users/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void logoutUser(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        userService.logOutUser(token);
    }
}
