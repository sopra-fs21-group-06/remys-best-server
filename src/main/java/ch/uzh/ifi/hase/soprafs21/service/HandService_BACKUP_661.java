package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.objects.Card;
import ch.uzh.ifi.hase.soprafs21.objects.Hand;

public class HandService {
    private final Hand hand;



    public HandService(Hand hand) {
        this.hand = hand;
        hand.setHandDeck();
    }


    public void deleteCardFromHand(Card card){
        this.hand.getHandDeck().remove(card);
    }
    public void addCardToHand(Card card){
        this.hand.getHandDeck().add(card);
    }
<<<<<<< HEAD
    public void deleteCardFromHand(Card card){}
    public Card addCardToHand(Card card){return null;}
=======
>>>>>>> dev

}
