package ch.uzh.ifi.hase.soprafs21.objects;

public class Deck {
    private String deckID;
    private Deck deck;

    public void Deck() {}
    public String getDeckID() {
        return deckID;
    }

    public void setDeckID (String deckID){
        this.deckID = deckID;
    }

}
