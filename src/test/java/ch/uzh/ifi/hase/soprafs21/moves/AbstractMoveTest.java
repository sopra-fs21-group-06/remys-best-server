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
    public void goOneForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove oneForwards = new OneForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        oneForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }
    public void goTwoForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove twoForwards = new TwoForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        twoForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }
    public void goThreeForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove threeForwards = new ThreeForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        threeForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }
    public void goFourForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove fourForwards = new FourForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        fourForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }
    public void goFiveForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove fiveForwards = new FiveForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        fiveForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }
    public void goSixFowards(Game game, Marble marble, String targetFieldKey) {
        INormalMove sixForwards = new SixForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        sixForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }

    public void goEightForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove eightForwards = new EightForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        eightForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }
    public void goNineFowards(Game game, Marble marble, String targetFieldKey) {
        INormalMove nineForwards = new NineForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        nineForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }
    public void goTenForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove tenForwards = new TenForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        tenForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }
    public void goElevenForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove elevenForwards = new ElevenForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        elevenForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }
    public void goTwelveForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove twelveForwards = new ElevenForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        twelveForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }
    public void goThirteenForwards(Game game, Marble marble, String targetFieldKey) {
        INormalMove thirteenForwards = new ThirteenForwards();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList = new ArrayList<>();
        marbleIdAndTargetFieldKeyArrayList.add(new MarbleIdAndTargetFieldKey(marble.getMarbleId(), targetFieldKey));
        thirteenForwards.executeMove(game, marbleIdAndTargetFieldKeyArrayList);
    }



}
