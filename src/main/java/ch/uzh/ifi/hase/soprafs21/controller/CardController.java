package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.service.CardService;
import ch.uzh.ifi.hase.soprafs21.service.DeckService;

public class CardController {
    private final CardService cardService;
    CardController(CardService cardService){this.cardService = cardService;};
}
