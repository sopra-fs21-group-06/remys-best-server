package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.CardAPIDeckResponseObject;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;
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

import static org.mockito.BDDMockito.given;

@SpringBootTest
public abstract class AbstractMoveTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @MockBean
    private WebSocketService websocketService;

    @MockBean
    private CardAPIService cardAPIService;

    @BeforeEach
    public void clearDatabase() {
        userService.getUserRepository().deleteAll();

        CardAPIDeckResponseObject mockedCardAPIDeckResponseObject = new CardAPIDeckResponseObject();
        mockedCardAPIDeckResponseObject.setDeck_id("test");
        given(cardAPIService.createDeck()).willReturn(mockedCardAPIDeckResponseObject);
        given(cardAPIService.drawCards(Mockito.any(), Mockito.any())).willReturn(new ArrayList<>());
    }

    private User createTestUser(String username, String email) {
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
        Game game = new Game(users, websocketService);
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

    public Marble goToStart(Game game) {
        Marble marbleToGoOut = game.getCurrentRound().getCurrentPlayer().getMarbleList().get(0);
        INormalMove goToStart = new GoToStart();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        String targetFieldKey = String.valueOf(16) + marbleToGoOut.getColor();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marbleToGoOut.getMarbleId(), targetFieldKey));
        marbleIdAndTargetFieldKeyArrayList = goToStart.executeMove(game, marbleIdAndTargetFieldKeyArrayList);

        for(Marble marble : game.getCurrentRound().getCurrentPlayer().getMarbleList()) {
            if(marble.getMarbleId() == marbleIdAndTargetFieldKeyArrayList.get(0).getMarbleId()) {
                marbleToGoOut = marble;
                break;
            }
        }

        return marbleToGoOut;
    }

    public void goEightForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove eightForwards = new EightForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        eightForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }

    public void goTenForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove tenForwards = new TenForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        tenForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }

    public void goTwoForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove two = new TwoForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        two.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }
}
