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
        final String uri = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1&jokers_enabled=true";

      //To generate only partial deck with working cards for M3
       // final String uri = "https://deckofcardsapi.com/api/deck/new/shuffle/?cards=AS,2S,3S,4S,5S,6S,7S,8S,9S,10S,QS,KS,AD,2D,3D,4D,5D,6D,7D,8D,9D,10D,QD,KD,AC,2C,3C,4C,5C,6C,7C,8C,9C,10C,QC,KC,AH,2H,3H,4H,5H,6H,7H,8H,9H,10H,QH,KH";*/
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
