package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.FREE);

        checkIfUserExists(newUser);

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the username and the name
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
        User userByEmail = userRepository.findByEmail(userToBeCreated.getEmail());

        String baseErrorMessage = "The %s provided %s are already used. Therefore, the user could not be created!";
        if (userByUsername != null && userByEmail != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username and the emailadress", "are"));
        }
        else if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
        }
        else if (userByEmail != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "emailadress", "is"));
        }
    }

    public User logInUser(User user){

        int usernameOrEmail = checkIfLoginDataCorrect(user);

        // 0 means user logged in with emailadress, 1 means user logged in with username
        User userToUpdate = usernameOrEmail == 0 ? userRepository.findByEmail(user.getUsername()): userRepository.findByUsername(user.getUsername());
        userToUpdate.setToken(UUID.randomUUID().toString());
        userToUpdate.setStatus(UserStatus.FREE);

        userToUpdate = userRepository.save(userToUpdate);
        userRepository.flush();

        return userToUpdate;
    }

    public void updateUserIdentity(String identity, String token){
        User userToUpdate = userRepository.findByToken(token);
        log.info(identity);
        userToUpdate.setSessionIdentity(identity);
        userRepository.save(userToUpdate);
        userRepository.flush();
    }

    private int checkIfLoginDataCorrect(User userToBeLoggedIn){

        String usernameOrEmail = userToBeLoggedIn.getUsername();
        String passwordWrongErrorMessage = "The provided password is wrong.";
        String userDoesNotExistErrorMessage = "There is no userAccount with this %s. Haven't an userAccount yet? Please Register.";

        if (usernameOrEmail.contains("@")){
            User userByEmail = userRepository.findByEmail(userToBeLoggedIn.getUsername());
            if(userByEmail == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(userDoesNotExistErrorMessage, "emailadress"));
            }
            else if(!userToBeLoggedIn.getPassword().equals(userByEmail.getPassword())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, passwordWrongErrorMessage);
            }
            return 0;
        }

        else{
            User userByUsername = userRepository.findByUsername(userToBeLoggedIn.getUsername());

            if (userByUsername == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(userDoesNotExistErrorMessage, "username"));
            }
            else if(!userToBeLoggedIn.getPassword().equals(userByUsername.getPassword())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, passwordWrongErrorMessage);
            }
            return 1;
        }
    }

    public void logOutUser(User user){

        User userToUpdate = userRepository.getOne(userRepository.findByToken(user.getToken()).getId());
        userToUpdate.setToken(null);
        userToUpdate.setStatus(UserStatus.OFFLINE);

        userRepository.save(userToUpdate);
        userRepository.flush();
    }
}
