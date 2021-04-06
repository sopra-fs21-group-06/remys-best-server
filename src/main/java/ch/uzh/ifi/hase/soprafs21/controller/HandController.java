package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.service.CardService;
import ch.uzh.ifi.hase.soprafs21.service.HandService;

public class HandController {
    private final HandService handService;
    HandController(HandService handService){this.handService = handService;};
}
