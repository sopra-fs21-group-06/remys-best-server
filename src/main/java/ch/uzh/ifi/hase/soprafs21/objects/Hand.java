package ch.uzh.ifi.hase.soprafs21.objects;

import java.util.List;

public class Hand {
    private List<Card> handDeck;
    public Hand(List<Card> handDeck){
        this.handDeck = handDeck;
    }
    public List<Card> getHandDeck() {
        return handDeck;
    }

    public void setHandDeck(List<Card> handDeck) {
        this.handDeck = handDeck;
    }
}
