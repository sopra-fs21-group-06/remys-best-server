package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.HomeRegisterDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WSHomeControllerTest extends AbstractWSControllerTest {

    @Test
    void testRegisterUser() {
        User testUser1 = createTestUser("abcd_sid", "hello@abcd_sid.com");

        HomeRegisterDTO homeRegisterDTO = new HomeRegisterDTO();
        homeRegisterDTO.setToken(testUser1.getToken());

        stompSession.send("/app/home/register", homeRegisterDTO);
    }

    @Test
    void testUnregisterUser() {
        User testUser1 = createTestUser("abcd_sid", "hello@abcd_sid.com");

        HomeRegisterDTO homeRegisterDTO = new HomeRegisterDTO();
        homeRegisterDTO.setToken(testUser1.getToken());

        stompSession.send("/app/home/unregister", homeRegisterDTO);
    }
}
