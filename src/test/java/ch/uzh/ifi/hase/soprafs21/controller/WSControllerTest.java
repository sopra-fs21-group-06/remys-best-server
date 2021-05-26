package ch.uzh.ifi.hase.soprafs21.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WSControllerTest extends AbstractWSControllerTest {

    @AfterEach
    public void tearDown() {
        // do nothing
    }

    @Test
    void testDisconnect() {
        stompSession.disconnect();
    }
}
