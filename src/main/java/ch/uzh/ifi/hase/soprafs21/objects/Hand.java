package ch.uzh.ifi.hase.soprafs21.objects;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> handDeck;
    public Hand(ArrayList<Card> handDeck){
        this.handDeck = handDeck;
    }
    public List<Card> getHandDeck() {
        return handDeck;
    }

    public void setHandDeck(List<Card> handDeck) {
        this.handDeck = handDeck;
    }
    public Card getCardFromHandDeck(int i){
        return handDeck.get(i);
    }
    public void addCardsToHand(List<Card> cardList){
        handDeck.addAll(cardList);
    }
}
