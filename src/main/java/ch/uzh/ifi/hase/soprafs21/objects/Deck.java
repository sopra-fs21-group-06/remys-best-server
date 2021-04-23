package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.CardSuit;

import java.util.List;

public class Deck {
    private int deckID;
    private Deck deck;

    public Deck(){
        // do api call generate deck


    }
    public int getDeckID() {
        return deckID;
    }

    public void setDeckID(int deckID) {
        this.deckID = deckID;
    }
    public void shuffle(){
        // shuffle api deck
    }
    public List<Card> draw(int nrCards) {
        List<Card> cards = null;
        for(int i = 0; i < nrCards; i++){
            //make api call to drw card;
            CardSuit suit = CardSuit.Hearts;
            String cardValue = "7";
            String image = null;
            String card_id = "H8";
            if (cardValue.equals("7")) {
                CardSeven card = new CardSeven(suit, cardValue, image, card_id);
                cards.add(card);
            } else if (cardValue.equals("JOKER")){
                CardJoker card = new CardJoker(suit, cardValue, image, card_id);
                cards.add(card);
            } else {
                Card card = new Card(suit, cardValue, image, card_id);
                cards.add(card);
            }
        }
        return cards;

    }
}
