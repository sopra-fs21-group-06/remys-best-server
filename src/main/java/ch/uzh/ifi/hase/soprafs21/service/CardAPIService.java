package ch.uzh.ifi.hase.soprafs21.service;

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
        //To generate the full shuffled deck with jokers
        //final String uri = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1&jokers_enabled=true";

        //To generate only partial deck with working cards for M3
        final String uri = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1&jokers_enabled=true";
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(uri, CardAPIDeckResponseObject.class);
    }

    public ArrayList<Card> drawCards(String deckID, String amountOfCards){
        final String uri = String.format("https://deckofcardsapi.com/api/deck/%s/draw/?count=%s", deckID, amountOfCards);
        RestTemplate restTemplate = new RestTemplate();

        CardAPICardResponseObject cardAPICardResponseObject= restTemplate.getForObject(uri, CardAPICardResponseObject.class);
        assert cardAPICardResponseObject != null;
        List<CardAPICardJson> cardList = cardAPICardResponseObject.getCards();

        ArrayList<Card> drawnCards = new ArrayList<>();

        for (CardAPICardJson card : cardList) {
            drawnCards.add(new Card(card.getCode()));
        }
        return drawnCards;
    }

    public void shuffle(String deckID){
        final String uri = String.format("https://deckofcardsapi.com/api/deck/%s/shuffle/", deckID);
    }
}
