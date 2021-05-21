package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.Card;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoToStartMoveTest extends AbstractMoveTest {
    //Move marble at home to starting field using KING #82
    //Move marble at home to starting field using ACE #80
    @Test
    public void testGoToStart(){

        Game game = setupGame();
        Marble marbleToGoOut = game.getCurrentRound().getCurrentPlayer().getMarbleList().get(3);
        INormalMove goToStart = new GoToStart();

        List<Marble> possibleMarbles = goToStart.getPlayableMarbles(game, game.getGameService());
        assertEquals(possibleMarbles.size(),1);
        assertEquals(possibleMarbles.get(0),marbleToGoOut);

        String targetFieldKey = String.valueOf(16) + marbleToGoOut.getColor();
        List<String> possibleTargetFields = goToStart.getPossibleTargetFields(game, marbleToGoOut);
        assertEquals(possibleTargetFields.size(),1);
        assertEquals(possibleTargetFields.get(0),targetFieldKey);

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marbleToGoOut.getMarbleId(), targetFieldKey));
        marbleIdAndTargetFieldKeyArrayList = goToStart.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
        assertEquals(marbleIdAndTargetFieldKeyArrayList.size(),1);
        assertFalse(marbleToGoOut.getHome());
        assertEquals(marbleIdAndTargetFieldKeyArrayList.get(0).getFieldKey(), targetFieldKey);
        assertEquals(marbleIdAndTargetFieldKeyArrayList.get(0).getMarbleId(), marbleToGoOut.getMarbleId());
    }
}
