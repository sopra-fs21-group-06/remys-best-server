package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.service.GameEngineService;

public class GameEngineController {
    private final GameEngineService gameEngineService;

    GameEngineController(GameEngineService gameEngineService) {
        this.gameEngineService = gameEngineService;
    }
}
