package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement.PossibleMarblesDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement.PossibleTargetFieldsDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * RESTGameControllerTest
 * This is a WebMvcTest which allows to test the RESTGameController i.e. GET/POST request without actually sending them over the network.
 * This tests if the RESTGameController works.
 */
@WebMvcTest(RESTGameController.class)
public class RESTGameControllerTest extends AbstractRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameEngine gameEngine;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @Test
    public void getMovesTest_cardExists_returnMoves() throws Exception {
        Game currentRunningGame = setupGame();
        UUID gameID = currentRunningGame.getGameId();
        Card card = new Card("2D");

        // when
        MockHttpServletRequestBuilder getRequest = get("/game/{gameId}/moves", gameID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("code", card.getCode());

        //then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.moves", hasSize(1)))
                .andExpect(jsonPath("$.moves[0].moveName", is(card.getMoves().get(0).getName())));
    }

    public void getMovesTest_cardDoesNotExist_returnError() throws Exception {
        // TODO
    }

    @Test
    public void getPossibleMarblesTest_cardAndMoveNameExists_returnPossibleMarbles() throws Exception {
        Game currentRunningGame = setupGame();
        UUID gameID = currentRunningGame.getGameId();
        currentRunningGame.setGameService(gameService);
        Card card = new Card("2D");
        Marble mockedPossibleMarble = new Marble(5, Color.RED);

        // this mocks the GameEngine -> we define above what the GameEngine should return when getRunningGameByID() is called
        given(gameEngine.getRunningGameByID(Mockito.any())).willReturn(currentRunningGame);
        given(gameService.getPlayableMarbles(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).willReturn(List.of(mockedPossibleMarble));

        //when
        PossibleMarblesDTO possibleMarblesDTO = new PossibleMarblesDTO();
        possibleMarblesDTO.setCode(card.getCode());
        possibleMarblesDTO.setMoveName(card.getMoves().get(0).getName());
        possibleMarblesDTO.setSevenMoves(new ArrayList<>());
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/game/{gameId}/possible-marbles", gameID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "userToken")
                .content(asJsonString(possibleMarblesDTO));

        //then
        MarbleDTO marbleDTO = new MarbleDTO();
        marbleDTO.setMarbleId(mockedPossibleMarble.getMarbleId());
        mockMvc.perform(postRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.marbles", hasSize(1)))
                .andExpect(jsonPath("$.marbles[0].marbleId", is(marbleDTO.getMarbleId())));
    }

    @Test
    public void getPossibleMarblesTest_cardDoesNotExist_returnError() throws Exception {
        // TODO
    }

    @Test
    public void getPossibleMarblesTest_moveNameDoesNotExist_returnError() throws Exception {
        // TODO
    }

    @Test
    public void getPossibleTargetFields_cardMarbleIdAndMoveNameExist_returnPossibleTargetFields() throws Exception {
        Game currentRunningGame = setupGame();
        UUID gameID = currentRunningGame.getGameId();
        currentRunningGame.setGameService(gameService);
        Card card = new Card("2D");
        Marble mockedPossibleMarble = new Marble(5, Color.RED);


        List<String> mockedTargetFieldKeys = new ArrayList<>();
        mockedTargetFieldKeys.add("5GREEN");
        mockedTargetFieldKeys.add("9GREEN");
        mockedTargetFieldKeys.add("1YELLOW");

        // this mocks the GameEngine -> we define above what the GameEngine should return when getRunningGameByID() is called
        given(gameEngine.getRunningGameByID(Mockito.any())).willReturn(currentRunningGame);
        given(gameService.getMarbleByMarbleId(Mockito.any(), Mockito.anyInt())).willReturn(mockedPossibleMarble);
        given(gameService.getPossibleTargetFields(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).willReturn(mockedTargetFieldKeys);

        //when
        PossibleTargetFieldsDTO possibleTargetFieldsDTO = new PossibleTargetFieldsDTO();
        possibleTargetFieldsDTO.setCode(card.getCode());
        possibleTargetFieldsDTO.setMarbleId(mockedPossibleMarble.getMarbleId());
        possibleTargetFieldsDTO.setMoveName(card.getMoves().get(0).getName());
        possibleTargetFieldsDTO.setSevenMoves(new ArrayList<>());
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/game/{gameId}/possible-target-fields",gameID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "userToken")
                .content(asJsonString(possibleTargetFieldsDTO));

        //then
        mockMvc.perform(postRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.targetFieldKeys", hasSize(3)))
                .andExpect(jsonPath("$.targetFieldKeys[0]", is(mockedTargetFieldKeys.get(0))))
                .andExpect(jsonPath("$.targetFieldKeys[1]", is(mockedTargetFieldKeys.get(1))))
                .andExpect(jsonPath("$.targetFieldKeys[2]", is(mockedTargetFieldKeys.get(2))));
    }

    @Test
    public void getPossibleTargetFields_cardDoesNotExist_returnError() throws Exception {
        // TODO
    }

    @Test
    public void getPossibleTargetFields_marbleIdDoesNotExist_returnError() throws Exception {
        // TODO
    }

    @Test
    public void getPossibleTargetFields_moveNameDoesNotExist_returnError() throws Exception {
        // TODO
    }
}