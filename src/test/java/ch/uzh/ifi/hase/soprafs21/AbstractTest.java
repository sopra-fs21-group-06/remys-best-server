package ch.uzh.ifi.hase.soprafs21;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.Card;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Player;
import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public abstract class AbstractTest {

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @MockBean
    private WebSocketService websocketService;

    @MockBean
    private CardAPIService cardAPIService;

    @BeforeEach
    public void setup() {
        userService.getUserRepository().deleteAll();
        Mockito.when(cardAPIService.drawCards(Mockito.anyInt())).thenReturn(new ArrayList<>());
    }

    public User createTestUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password");
        user.setSessionIdentity(UUID.randomUUID().toString());
        userService.createUser(user);
        return user;
    }

    public Game setupGame() {
        User testUser1 = createTestUser("user1", "user1@user1.ch");
        User testUser2 = createTestUser("user2", "user2@user2.ch");
        User testUser3 = createTestUser("user3", "user3@user3.ch");
        User testUser4 = createTestUser("user4", "user4@user4.ch");

        List<User> users = new ArrayList<>();
        users.add(testUser1);
        users.add(testUser2);
        users.add(testUser3);
        users.add(testUser4);
        Game game = new Game(users, websocketService, cardAPIService);
        game.setGameService(gameService);
        game.updatePlayerColor(testUser1.getUsername(), Color.BLUE);
        game.updatePlayerColor(testUser2.getUsername(), Color.GREEN);
        game.updatePlayerColor(testUser3.getUsername(), Color.RED);
        game.updatePlayerColor(testUser4.getUsername(), Color.YELLOW);
        game.setPlayerToReady(testUser1.getUsername());
        game.setPlayerToReady(testUser2.getUsername());
        game.setPlayerToReady(testUser3.getUsername());
        game.setPlayerToReady(testUser4.getUsername());

        return game;
    }

    public void addCardsToHand(Player player, List<String> cardCodes) {
        List<Card> cardsToAdd = new ArrayList<>();
        for(String cardCode: cardCodes) {
            cardsToAdd.add(new Card(cardCode));
        }
        player.getHand().addCardsToHand(cardsToAdd);
    }
}
