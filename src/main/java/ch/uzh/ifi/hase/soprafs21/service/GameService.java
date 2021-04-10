package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Player;

import java.util.List;

public class GameService {
    private final Game game;

    public GameService(Game game) {
        this.game = game;
    }

    public void InitiateRound(int nrCards, Player startPlayer){
        game.setNrCards(nrCards);
        game.setStartPlayer(startPlayer);
    }
    public void deleteGame(){

    }
    public List<Player> getPlayers(){
        return game.getPlayerList();
    }


}
