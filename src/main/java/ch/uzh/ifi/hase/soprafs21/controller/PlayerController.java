package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.objects.GameSession;
import ch.uzh.ifi.hase.soprafs21.service.PlayerService;

public class PlayerController {
    private final PlayerService playerService;

    PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }
}
