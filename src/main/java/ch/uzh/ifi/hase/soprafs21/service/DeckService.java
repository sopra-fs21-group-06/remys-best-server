package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.objects.Card;
import ch.uzh.ifi.hase.soprafs21.objects.Deck;
import ch.uzh.ifi.hase.soprafs21.objects.Hand;

import java.util.List;

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
    public List<Card> draw(int nrCards){
        // draw from api deck
    public Hand draw(int nrCards){
        // get nr crads

    }
}