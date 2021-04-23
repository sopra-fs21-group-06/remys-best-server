package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Player;
import ch.uzh.ifi.hase.soprafs21.objects.Round;

import java.util.List;

public class GameService {
    private final Game game;

    public GameService(Game game) {
        this.game = game;
    }

    public void InitiateRound(){
        Round currentRound = new Round(game.getPlayerList(),game.getStartPlayer(),game.getNrCards(),game);
        int indexCurrent = game.getPlayerList().indexOf(game.getStartPlayer());
        int indexNext = (indexCurrent + 1) % 4;
        game.setStartPlayer(game.getPlayerList().get(indexNext));
        game.addToRoundCount();
        game.changeNrCards();
        currentRound.initializeRound();
    }

    public List<Player> getPlayers(){
        return game.getPlayerList();
    }


}
