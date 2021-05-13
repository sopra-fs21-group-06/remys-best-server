package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.objects.Card;
import ch.uzh.ifi.hase.soprafs21.objects.CardAPIDeckResponseObject;
import ch.uzh.ifi.hase.soprafs21.objects.Deck;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.cardAPIMapper;
import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


// only used for testing purposes, delete when testing done
@RestController
public class RESTCardAPIController {

    private final CardAPIService cardAPIService;

    public RESTCardAPIController(CardAPIService cardAPIService) {
        this.cardAPIService = cardAPIService;
    }

    @GetMapping("/decks")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Deck createDeck() {
        // convert API user to internal representation

        CardAPIDeckResponseObject cardAPIDeckResponseObject = cardAPIService.createDeck();

        Deck deckID = cardAPIMapper.INSTANCE.convertCardDeckAPIResponseObjectToDeck(cardAPIDeckResponseObject);

        return deckID;
    }

    @GetMapping("/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Card> drawCard(@PathVariable String deckId, HttpServletRequest request){
        String amountOfCards = request.getHeader("count");
        return cardAPIService.drawCards(deckId, amountOfCards);
    }
}
