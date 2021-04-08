package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.service.DeckService;

public class DeckController {
    private final DeckService deckService;
    DeckController(DeckService deckService){this.deckService = deckService;};
}
