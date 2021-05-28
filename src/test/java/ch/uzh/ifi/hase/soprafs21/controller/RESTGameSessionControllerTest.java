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

    }

}
