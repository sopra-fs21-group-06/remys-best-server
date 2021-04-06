package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.service.PlayingBoardService;

public class PlayingBoardController {
    private final PlayingBoardService playingBoardService;

    public PlayingBoardController(PlayingBoardService playingBoardService) {
        this.playingBoardService = playingBoardService;
    }
}
