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

    public void addCardsToHand(List<Card> cardList){
        handDeck.addAll(cardList);
    }

    public void deleteCardFromHand(Card c){
        String valueC = c.getCode();
        for(Card card: this.handDeck){
            String valueCard = card.getCode();

            if(valueC.equals(valueCard)) {
                this.handDeck.remove(card);
                break;
            }
        }
    }

    public void throwAwayHand(){
        List<Card> newHandDeck = new ArrayList<>();
        setHandDeck(newHandDeck);
    }
}


