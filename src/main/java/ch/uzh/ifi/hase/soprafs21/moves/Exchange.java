package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.objects.Field;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Exchange implements IMove {
    Logger log = LoggerFactory.getLogger(Exchange.class);
    @Override
    public String getName() {
        return "Exchange";
    }


    @Override
    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove) {
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        List<Marble> marblesOnFieldAndNotFinishedPlayer = game.getCurrentRound().getCurrentPlayer().getMarblesOnFieldAndNotFinished();
        List<Marble> marblesOnFieldAndNotFinishedTeamMate = game.getCurrentRound().getCurrentPlayer().getTeamMate().getMarblesOnFieldAndNotFinished();
        boolean allMarblesToChangeWith = marblesOnFieldAndNotFinishedPlayer.addAll(marblesOnFieldAndNotFinishedTeamMate);
        for(Marble m: marblesOnFieldAndNotFinishedPlayer){
            if(marbleToMove.getMarbleNr() != m.getMarbleNr()){
                possibleTargetFieldKeys.add(m.getCurrentField().getFieldKey());
            }
        }
        return possibleTargetFieldKeys;
    }

    @Override
    public List<Marble> getPlayableMarbles(Game game, GameService gameService) {
        List<Marble> marblesOnFieldAndNotFinished = game.getCurrentRound().getCurrentPlayer().getMarblesOnFieldAndNotFinished();
        return marblesOnFieldAndNotFinished;
    }

    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Game game, MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey) {
        Field targetField = game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKey.getFieldKey());
        Marble marbleToMove = targetField.getMarble();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeysOutgoing = new ArrayList<>();
        String fieldKeyNewTeamMate = marbleToMove.getCurrentField().getFieldKey();
        Marble marbleTeamMate = targetField.getMarble();
        MarbleIdAndTargetFieldKey result1 = new MarbleIdAndTargetFieldKey(marbleToMove.getMarbleNr(), targetField.getFieldKey());
        MarbleIdAndTargetFieldKey result2 = new MarbleIdAndTargetFieldKey(marbleTeamMate.getMarbleNr(), fieldKeyNewTeamMate);
        marbleIdAndTargetFieldKeysOutgoing.add(result1);
        marbleIdAndTargetFieldKeysOutgoing.add(result2);
        game.getPlayingBoard().makeJackMove(targetField, marbleToMove);
        return marbleIdAndTargetFieldKeysOutgoing;
    }
}
