package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;
import ch.uzh.ifi.hase.soprafs21.objects.Player;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExchangeMoveTest extends AbstractMoveTest {

    Logger log = LoggerFactory.getLogger(FourBackwards.class);
    @Test
    public void testExchange() {
        //SetUp
        Game game = setupGame();
        Marble marble1Player1 = goToStart(game);
        Player player1 = game.getCurrentRound().getCurrentPlayer();
        game.getCurrentRound().changeCurrentPlayer();
        Marble marble1Player2 = goToStart(game);
        Player player2 = game.getCurrentRound().getCurrentPlayer();


        game.getCurrentRound().setCurrentPlayer(player1);
        String targetFieldKeyPlayer1 = "1GREEN";
        INormalMove oneForwards = new OneForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys1 = new ArrayList<>();
        marbleIdAndTargetFieldKeys1.add(new MarbleIdAndTargetFieldKey(marble1Player1.getMarbleId(), targetFieldKeyPlayer1));
        oneForwards.executeMove(game, marbleIdAndTargetFieldKeys1);

        game.getCurrentRound().setCurrentPlayer(player2);
        String targetFieldKeyPlayer2 = "1RED";
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys2 = new ArrayList<>();
        marbleIdAndTargetFieldKeys2.add(new MarbleIdAndTargetFieldKey(marble1Player2.getMarbleId(), targetFieldKeyPlayer2));
        oneForwards.executeMove(game, marbleIdAndTargetFieldKeys2);


        //CurrentPlayer player 2
        INormalMove exchange = new Exchange();
        List<Marble> playableMarbles = exchange.getPlayableMarbles(game, game.getGameService());
        assertTrue(playableMarbles.contains(marble1Player2));

        //TargetFIeld for marble Player2 is the field of marble player 1
        List<String> possibleTargetFields = exchange.getPossibleTargetFields(game, marble1Player2);
        assertEquals(possibleTargetFields.size(), 1);
        assertEquals(possibleTargetFields.get(0), targetFieldKeyPlayer1);


        //Marble of Player 2 is TargetField of Player1 and vise vers
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new  ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble1Player2.getMarbleId(), targetFieldKeyPlayer1));
        ArrayList<MarbleIdAndTargetFieldKey> executedMoves = exchange.executeMove(game, marbleIdAndTargetFieldKeys);
        assertEquals(executedMoves.size(), 2);
        assertEquals(executedMoves.get(0).getMarbleId(), marble1Player2.getMarbleId());
        assertEquals(executedMoves.get(0).getFieldKey(), targetFieldKeyPlayer1);
        assertEquals(executedMoves.get(1).getMarbleId(), marble1Player1.getMarbleId());
        assertEquals(executedMoves.get(1).getFieldKey(), targetFieldKeyPlayer2);



    }

    @Test
    public void testExchangeNoPossibleMarbles() {
        //SetUp Player 1 moves into finish and has marble on start ->no marble to exchange with for player2
        Game game = setupGame();
        Marble marble1Player1 = goToStart(game);
        Player player1 = game.getCurrentRound().getCurrentPlayer();

        String targetFieldKeyPlayer1 = "12BLUE";
        INormalMove fourBackwards = new FourBackwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        marbleIdAndTargetFieldKeys.add(new MarbleIdAndTargetFieldKey(marble1Player1.getMarbleId(), targetFieldKeyPlayer1));
        fourBackwards.executeMove(game, marbleIdAndTargetFieldKeys);
        targetFieldKeyPlayer1 = "20BLUE";
        INormalMove eightForward = new EightForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys1 = new ArrayList<>();
        marbleIdAndTargetFieldKeys1.add(new MarbleIdAndTargetFieldKey(marble1Player1.getMarbleId(), targetFieldKeyPlayer1));
        eightForward.executeMove(game, marbleIdAndTargetFieldKeys1);

        Marble marble2Player1 = goToStart(game);




        game.getCurrentRound().changeCurrentPlayer();
        Marble marble1Player2 = goToStart(game);
        Player player2 = game.getCurrentRound().getCurrentPlayer();
        INormalMove oneForwards = new OneForwards();
        game.getCurrentRound().setCurrentPlayer(player2);
        String targetFieldKeyPlayer2 = "1RED";
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys2 = new ArrayList<>();
        marbleIdAndTargetFieldKeys2.add(new MarbleIdAndTargetFieldKey(marble1Player2.getMarbleId(), targetFieldKeyPlayer2));
        oneForwards.executeMove(game, marbleIdAndTargetFieldKeys2);
        //CurrentPlayer player 2
        INormalMove exchange = new Exchange();
        List<Marble> playableMarbles = exchange.getPlayableMarbles(game, game.getGameService());
        for(Marble m: playableMarbles){
            log.info(m.getCurrentField().getFieldKey() + "Marble " + String.valueOf(m.getMarbleId()));
        }
        assertEquals(playableMarbles.size(), 0);

    }

}
