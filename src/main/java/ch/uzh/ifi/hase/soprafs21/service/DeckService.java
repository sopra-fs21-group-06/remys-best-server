package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.objects.Deck;

public class DeckService {
    private final Deck deck;

    public DeckService(Deck deck) {
        this.deck = deck;
    }

    public int generateDeck(){
        // generate deck from api and add id to deck
        return 0;
    }
    public void shuffle(){
        // shuffle api deck
    }
}
