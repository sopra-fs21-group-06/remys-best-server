package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.objects.Card;
import ch.uzh.ifi.hase.soprafs21.objects.CardAPICardJson;
import ch.uzh.ifi.hase.soprafs21.objects.CardAPICardResponseObject;
import ch.uzh.ifi.hase.soprafs21.objects.CardAPIDeckResponseObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;

@Service
public class CardAPIService {

    private final String deckId;
    private int remaining;
    private final RestTemplate restTemplate;

    public CardAPIService() {
        this.restTemplate = createRestTemplate();
        CardAPIDeckResponseObject deckResponseObject = createDeck();
        this.deckId = deckResponseObject.getDeck_id();
        this.remaining = deckResponseObject.getRemaining();
    }

    private RestTemplate createRestTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return builder
                .setConnectTimeout(Duration.ofSeconds(3000))
                .setReadTimeout(Duration.ofSeconds(3000))
                .build();
    }

    private CardAPIDeckResponseObject createDeck() {
        //To generate the full shuffled deck with jokers
        String uri = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1&jokers_enabled=true";

        CardAPIDeckResponseObject cardAPIDeckResponseObject = this.restTemplate.getForObject(uri, CardAPIDeckResponseObject.class);

        return cardAPIDeckResponseObject;
    }

    private synchronized void shuffle() {
        String uri = String.format("https://deckofcardsapi.com/api/deck/%s/shuffle/", this.deckId);

        CardAPIDeckResponseObject cardAPIDeckResponseObject = this.restTemplate.getForObject(uri, CardAPIDeckResponseObject.class);

        this.remaining = cardAPIDeckResponseObject.getRemaining();
    }

    private synchronized ArrayList<Card> drawCardsInternal(int amountOfCards) {
        String uri = String.format("https://deckofcardsapi.com/api/deck/%s/draw/?count=%s", this.deckId, amountOfCards);

        CardAPICardResponseObject cardAPICardResponseObject = this.restTemplate.getForObject(uri, CardAPICardResponseObject.class);

        // transform cardCodes to cards
        ArrayList<Card> drawnCards = new ArrayList<>();
        for (CardAPICardJson card : cardAPICardResponseObject.getCards()) {
            drawnCards.add(new Card(card.getCode()));
        }

        // update remaining
        this.remaining = cardAPICardResponseObject.getRemaining();

        return drawnCards;
    }

    public ArrayList<Card> drawCards(int amountOfCardsToDraw) {
        int currentRemaining = this.remaining;
        ArrayList<Card> drawnCards = new ArrayList<>();

        if(amountOfCardsToDraw > currentRemaining) {
            ArrayList<Card> lastCardsOfDeck = drawCardsInternal(currentRemaining);
            drawnCards.addAll(lastCardsOfDeck);
            shuffle();
            amountOfCardsToDraw -= currentRemaining;
        }

        ArrayList<Card> cardsFromDeck = drawCardsInternal(amountOfCardsToDraw);
        drawnCards.addAll(cardsFromDeck);

        return drawnCards;
    }

}
