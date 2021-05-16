package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * RESTGameControllerTest
 * This is a WebMvcTest which allows to test the RESTGameController i.e. GET/POST request without actually sending them over the network.
 * This tests if the RESTGameController works.
 */
@WebMvcTest(RESTGameController.class)
public class RESTGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameEngine gameEngine;

    @MockBean
    private UserService userService;

    @MockBean
    private GameService gameService;

    @MockBean
    private Game currentGame;

    @Test
    public void getMovesTest() throws Exception {
        //given
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("siddhantsahu");
        user1.setEmail("hello@siddhantsahu.com");
        user1.setPassword("abcd");
        user1.setToken("123456789");
        user1.setStatus(UserStatus.Busy);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("pascal");
        user2.setEmail("hello@pascal.com");
        user2.setPassword("1234");
        user2.setToken("abcdefgh");
        user2.setStatus(UserStatus.Busy);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        WebSocketService webSocketService = new WebSocketService(userService);
        //CardAPIService cardAPIService = new CardAPIService();
        //UserService userService = new UserService();
        //GameService gameService = new GameService(cardAPIService, userService(), webSocketService);

        Game currentRunningGame = new Game(users, webSocketService);
        UUID gameID = currentRunningGame.getGameId();

        //given(userService)
        // this mocks the GameEngine -> we define above what the GameEngine should return when getRunningGameByID() is called
        given(gameEngine.getRunningGameByID(Mockito.any())).willReturn(currentRunningGame);
        //given(game.getCurrentRound().getCurrentPlayer()).willReturn()
        //given(currentRunningGame.getGameService()).willReturn(gameService);
        String cardCode = "2D";

        //when
        MockHttpServletRequestBuilder getRequest = get("/game/{gameId}/moves",gameID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("code", cardCode);

        //then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void getPossibleMarblesTest() throws Exception {
        //given
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("siddhantsahu");
        user1.setEmail("hello@siddhantsahu.com");
        user1.setPassword("abcd");
        user1.setToken("123456789");
        user1.setStatus(UserStatus.Busy);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("pascal");
        user2.setEmail("hello@pascal.com");
        user2.setPassword("1234");
        user2.setToken("abcdefgh");
        user2.setStatus(UserStatus.Busy);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        WebSocketService webSocketService = new WebSocketService(userService);

        Game currentRunningGame = new Game(users, webSocketService);
        UUID gameID = currentRunningGame.getGameId();
        currentRunningGame.setGameService(gameService);
        String cardCode = "2D";

        //Field field = new Field(2, Color.RED);
        Marble currentMarble = new Marble(2, Color.RED);

        //List<String> targetFields = new ArrayList<String>();
        //targetFields.add("abcd");
        //targetFields.add("efgh");

        // this mocks the GameEngine -> we define above what the GameEngine should return when getRunningGameByID() is called
        given(gameEngine.getRunningGameByID(Mockito.any())).willReturn(currentRunningGame);
        //given(currentRunningGame.getGameService()).willReturn(gameService);
        //Mockito.when(currentGame.getGameService()).thenReturn(gameService);
        //given(userService.getUserRepository().findByToken(user1.getToken()).getUsername()).willReturn(user1.getUsername());
        //given(userService.getUserRepository().findByToken(user1.getToken()).getUsername()).willReturn(user1.getUsername());
        //given(userService.convertTokenToUsername(Mockito.any())).willReturn(user1.getUsername());
        //given(DogUtils.convertTokenToUsername(Mockito.any(), Mockito.any())).willReturn(user1.getUsername());
        //given(gameService.getMarbleByMarbleId(currentRunningGame, Mockito.any())).willReturn(currentMarble);
        //given(gameService.getPossibleTargetFields(currentMarble, Mockito.any(), Mockito.any(), currentRunningGame)).willReturn(targetFields);


        //when
        MockHttpServletRequestBuilder getRequest = get("/game/{gameId}/possible-marbles",gameID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("code", cardCode)
                .param("moveName", "2 Forwards");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void getPossibleTargetFields() throws Exception {
        //given
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("siddhantsahu");
        user1.setEmail("hello@siddhantsahu.com");
        user1.setPassword("abcd");
        user1.setToken("123456789");
        user1.setStatus(UserStatus.Busy);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("pascal");
        user2.setEmail("hello@pascal.com");
        user2.setPassword("1234");
        user2.setToken("abcdefgh");
        user2.setStatus(UserStatus.Busy);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        WebSocketService webSocketService = new WebSocketService(userService);

        Game currentRunningGame = new Game(users, webSocketService);
        UUID gameID = currentRunningGame.getGameId();
        String cardCode = "2D";

        //Field field = new Field(2, Color.RED);
        Marble currentMarble = new Marble(2, Color.RED);

        List<String> targetFields = new ArrayList<String>();
        targetFields.add("abcd");
        targetFields.add("efgh");

        // this mocks the GameEngine -> we define above what the GameEngine should return when getRunningGameByID() is called
        given(gameEngine.getRunningGameByID(Mockito.any())).willReturn(currentRunningGame);
        given(gameService.getMarbleByMarbleId(currentRunningGame, Mockito.any())).willReturn(currentMarble);
        given(gameService.getPossibleTargetFields(currentRunningGame, currentMarble, Mockito.any(), Mockito.any())).willReturn(targetFields);


        //when
        MockHttpServletRequestBuilder getRequest = get("/game/{gameId}/possible-target-fields",gameID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("code", cardCode)
                .param("moveName", "fnefnwfn");
    }

    /**
     * Helper Method to convert a DTO into a JSON string such that the input can be processed
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