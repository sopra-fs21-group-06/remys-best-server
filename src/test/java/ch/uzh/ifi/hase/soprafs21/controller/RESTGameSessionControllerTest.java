package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.GameEngine;
import ch.uzh.ifi.hase.soprafs21.objects.GameSession;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.CreateGameSession.GameSessionIdDTO;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RESTGameSessionController.class)
public class RESTGameSessionControllerTest extends AbstractRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameEngine gameEngine;

    @Autowired
    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void createGameSessionTest() throws Exception {

        User host = createTestUser("iamsiddhantsahu", "hello@siddhantsahu.com");
        host.setToken("abcd");

        GameSession gameSession = new GameSession(host, userService);

        GameSessionIdDTO gameSessionIdDTO = new GameSessionIdDTO();
        gameSessionIdDTO.setGameSessionId(gameSession.getID());

        Mockito.when(userService.getUserRepository())
                .thenReturn(userRepository);
        Mockito.when(userRepository.findByToken(host.getToken()))
                .thenReturn(host);
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(gameEngine.newGameSession(host))
                .thenReturn(gameSession);
        Mockito.when(gameEngine.findGameSessionByHostName(host.getUsername()))
                .thenReturn(gameSession);

        MockHttpServletRequestBuilder getRequest = get("/create-gamesession")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", host.getToken());

        mockMvc.perform(getRequest)
                .andExpect(status().isCreated());
    }

}
