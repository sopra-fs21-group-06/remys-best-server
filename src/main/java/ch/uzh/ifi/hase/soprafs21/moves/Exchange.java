package ch.uzh.ifi.hase.soprafs21.moves;


import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.objects.*;
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
    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove, int remainSeven) {
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        for (Player p: game.getPlayerList()) {
            if (!p.equals(game.getCurrentRound().getCurrentPlayer())) {
                for (Marble m : p.getMarblesOnFieldNotHomeNotOnStart()) {
                    if (marbleToMove.getMarbleNr() != m.getMarbleNr()) {
                        possibleTargetFieldKeys.add(m.getCurrentField().getFieldKey());
                    }
                }
            }
        }
        return possibleTargetFieldKeys;
    }

    @Override
    public List<Marble> getPlayableMarbles(Game game, GameService gameService, int remainSeven) {
        List<Marble> possibleMarbles = new ArrayList<>();
        for (Marble m : game.getCurrentRound().getCurrentPlayer().getMarblesOnFieldNotHomeNotOnStart()) {
            if (!(m.getCurrentField().getFieldStatus().equals(FieldStatus.BLOCKED))) {
                possibleMarbles.add(m);
            }
        }

        return possibleMarbles;
    }
    

    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Game game, ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList) {
        Field targetField =game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKeyArrayList.get(0).getFieldKey());
        Marble marbleToMove = null;
        for(Marble m: game.getCurrentRound().getCurrentPlayer().getMarbleList()){
            if(m.getMarbleNr() == marbleIdAndTargetFieldKeyArrayList.get(0).getMarbleId()){
                marbleToMove = m;
            }
        }
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
