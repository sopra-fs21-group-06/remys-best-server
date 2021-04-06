package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.service.GameSessionRequestService;

public class GameSessionRequestController {
    private final GameSessionRequestService gameSessionRequestService;

    public GameSessionRequestController(GameSessionRequestService gameSessionRequestService) {
        this.gameSessionRequestService = gameSessionRequestService;
    }
}
