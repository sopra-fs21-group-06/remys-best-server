package ch.uzh.ifi.hase.soprafs21.moves;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FourBackwards implements INormalMove {
    private static final int NUMBER_TO_GO_FORWARDS = -4;
    Logger log = LoggerFactory.getLogger(FourBackwards.class);

    @Override
    public String getName() {
        return "4 Backwards";
    }

    @Override
    public List<Marble> getPlayableMarbles(Game game, GameService gameService) {
        List<Marble> possibleMarbles = new ArrayList<>();
        Player p = game.getCurrentRound().getCurrentPlayer();
        for (Marble m : p.getMarblesOnFieldAndNotFinished()) {
            if (!(m.getCurrentField() instanceof FinishField)) {
                if (gameService.checkMoveBackward(m, NUMBER_TO_GO_FORWARDS, game)) {
                    possibleMarbles.add(m);
                }
            }
        }
        return possibleMarbles;
    }

    @Override
    public List<String> getPossibleTargetFields(Game game, Marble marbleToMove) {
        List<String> possibleTargetFieldKeys = new ArrayList<>();
        int fieldNr = marbleToMove.getCurrentField().getFieldValue() - 4;
        Color colorEndField = marbleToMove.getCurrentField().getColor();
        if (fieldNr < 1){
            fieldNr = 16 + fieldNr;
            colorEndField = DogUtils.getPreviousColor(colorEndField);
        }
        Field possTargetField = game.getPlayingBoard().getField(fieldNr, colorEndField);
        possibleTargetFieldKeys.add(possTargetField.getFieldKey());
        return possibleTargetFieldKeys;
    }

    @Override
    public ArrayList<MarbleIdAndTargetFieldKey> executeMove(Game game, ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeyArrayList) {
        Field targetField = game.getPlayingBoard().getFieldByFieldKey(marbleIdAndTargetFieldKeyArrayList.get(0).getFieldKey());
        Marble marbleToMove = null;
        for(Marble m: game.getCurrentRound().getCurrentPlayer().getMarbleList()){
            if(m.getMarbleId() == marbleIdAndTargetFieldKeyArrayList.get(0).getMarbleId()){
                marbleToMove = m;
            }
        }
        assert marbleToMove != null;

        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        MarbleIdAndTargetFieldKey resultEating = game.getGameService().eat(targetField,game);
        game.getPlayingBoard().makeMove(targetField, marbleToMove);
        log.info("marble move forward/backward successful");
        MarbleIdAndTargetFieldKey result = new MarbleIdAndTargetFieldKey(marbleToMove.getMarbleId(), targetField.getFieldKey());
        marbleIdAndTargetFieldKeys.add(result);
        if(!(resultEating == null)){
            marbleIdAndTargetFieldKeys.add(resultEating);
        }
        return marbleIdAndTargetFieldKeys;
    }
}



