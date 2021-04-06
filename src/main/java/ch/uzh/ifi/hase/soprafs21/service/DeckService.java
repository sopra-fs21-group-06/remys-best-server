package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.objects.Deck;

public class DeckService {
    private final Deck deck;

    public DeckService(Deck deck) {
        this.deck = deck;
    }

    public int generateDeck(){
        // generate deck from api and add id to deck
    }
    public void shuffle(){
        // shuffle api deck
    }
}
