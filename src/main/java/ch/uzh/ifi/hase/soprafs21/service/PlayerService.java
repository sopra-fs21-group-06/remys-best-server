package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.objects.Card;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.Player;

import static java.util.Objects.isNull;

public class PlayerService {
    private final Player player;
    public PlayerService(Player player){
        this.player = player;
    }
    public Card chooseCard(){}
    public Marble chooseMarble(){
        Marble marble = new Marble;
        return marble = null;
    }

}
