package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
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
        marblesOnFieldAndNotFinishedPlayer.addAll(marblesOnFieldAndNotFinishedTeamMate);
        for(Marble m: marblesOnFieldAndNotFinishedPlayer){
            if(marbleToMove.getMarbleNr() != m.getMarbleNr()){
                if (!(m.getCurrentField().getFieldStatus().equals(FieldStatus.BLOCKED))){
                    possibleTargetFieldKeys.add(m.getCurrentField().getFieldKey());
                }
            }
        }
        return possibleTargetFieldKeys;
    }

    @Override
    public List<Marble> getPlayableMarbles(Game game, GameService gameService) {
        List<Marble> possibleMarbles = new ArrayList<>();
        List<Marble> marblesOnFieldAndNotFinished = game.getCurrentRound().getCurrentPlayer().getMarblesOnFieldAndNotFinished();
        for(Marble m: marblesOnFieldAndNotFinished) {
            if (!(m.getCurrentField().getFieldStatus().equals(FieldStatus.BLOCKED))){
                possibleMarbles.add(m);
            }
        }
        return possibleMarbles;
    }

    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Marble marbleToMove, Field targetField, Game game) {
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        String fieldKeyNewTeamMate = marbleToMove.getCurrentField().getFieldKey();
        Marble marbleTeamMate = targetField.getMarble();
        MarbleIdAndTargetFieldKey result1 = new MarbleIdAndTargetFieldKey(marbleToMove.getMarbleNr(), targetField.getFieldKey());
        MarbleIdAndTargetFieldKey result2 = new MarbleIdAndTargetFieldKey(marbleTeamMate.getMarbleNr(), fieldKeyNewTeamMate);
        marbleIdAndTargetFieldKeys.add(result1);
        marbleIdAndTargetFieldKeys.add(result2);
        game.getPlayingBoard().makeJackMove(targetField, marbleToMove);
        return marbleIdAndTargetFieldKeys;
    }
}