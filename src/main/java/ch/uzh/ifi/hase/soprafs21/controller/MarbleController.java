package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.service.MarbleService;

public class MarbleController {
    public final MarbleService marbleService;

    public MarbleController(MarbleService marbleService) {
        this.marbleService = marbleService;
    }
}
