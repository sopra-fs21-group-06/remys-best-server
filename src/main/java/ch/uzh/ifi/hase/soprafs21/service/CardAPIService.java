package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.CardSuit;
import ch.uzh.ifi.hase.soprafs21.objects.Card;
import ch.uzh.ifi.hase.soprafs21.objects.CardAPICardJson;
import ch.uzh.ifi.hase.soprafs21.objects.CardAPICardResponseObject;
import ch.uzh.ifi.hase.soprafs21.objects.CardAPIDeckResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardAPIService {

    @Autowired
    public CardAPIService(){

    }

    public CardAPIDeckResponseObject createDeck(){
        final String uri = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1";
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(uri, CardAPIDeckResponseObject.class);
    }

    public List<Card> drawCards(String deckID, String amountOfCards){
        final String uri = String.format("https://deckofcardsapi.com/api/deck/%s/draw/?count=%s", deckID, amountOfCards);
        RestTemplate restTemplate = new RestTemplate();

        CardAPICardResponseObject cardAPICardResponseObject= restTemplate.getForObject(uri, CardAPICardResponseObject.class);
        assert cardAPICardResponseObject != null;
        List<CardAPICardJson> cardList = cardAPICardResponseObject.getCards();

        List<Card> drawnCards = new ArrayList<>();

        for (CardAPICardJson card : cardList) {
            drawnCards.add(new Card(cardValueMapper(card.getSuit()), card.getValue()));
        }

        return drawnCards;
    }

    private CardSuit cardValueMapper(String suit){

        CardSuit cardsuit;

        return switch (suit) {
            case "HEARTS" -> CardSuit.Hearts;
            case "SPADES" -> CardSuit.Spades;
            case "CLUBS" -> CardSuit.Clubs;
            default -> CardSuit.Diamonds;
        };
    }
}
