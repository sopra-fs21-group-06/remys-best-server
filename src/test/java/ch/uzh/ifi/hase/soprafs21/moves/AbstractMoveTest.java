package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.AbstractTest;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public abstract class AbstractMoveTest extends AbstractTest {

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
