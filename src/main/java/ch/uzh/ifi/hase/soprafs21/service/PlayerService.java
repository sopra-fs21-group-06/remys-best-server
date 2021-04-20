package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.objects.Card;
import ch.uzh.ifi.hase.soprafs21.objects.Marble;
import ch.uzh.ifi.hase.soprafs21.objects.Player;


public class PlayerService {

    private final Player player;
    public PlayerService(Player player){
        this.player = player;
    }
    public Card chooseCard(){return null;}
    public Marble chooseMarble(){return null;}

}

