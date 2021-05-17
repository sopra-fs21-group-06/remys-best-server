package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.objects.Card;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardAPIServiceTest {

    @Test
    public void testDrawCards() {
        CardAPIService cardAPIService = new CardAPIService();
        ArrayList<Card> drawnCards = cardAPIService.drawCards(7);
        assertEquals(7, drawnCards.size());
    }

    @Test
    public void testDrawCards_deckIsEmpty_reshuffleDeck() {
        CardAPIService cardAPIService = new CardAPIService();
        ArrayList<Card> drawnCards = cardAPIService.drawCards(50);
        assertEquals(50, drawnCards.size());
        drawnCards = cardAPIService.drawCards(7);
        assertEquals(7, drawnCards.size());
    }

    @Test
    public void testDrawCards_jokersHaveToBeEnabled() {
        CardAPIService cardAPIService = new CardAPIService();
        ArrayList<Card> drawnCards = cardAPIService.drawCards(54);
        assertEquals(54, drawnCards.size());
        ArrayList<Card> onlyJokerCards = (ArrayList<Card>) drawnCards.stream().filter(card -> card.getCode().equals("X1") || card.getCode().equals("X2")).collect(Collectors.toList());
        assertEquals(2, onlyJokerCards.size());
    }
}
