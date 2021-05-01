package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.FactDTO;

import java.util.List;

public class GameFactsDTO {

    private List<FactDTO> facts;

    public List<FactDTO> getFacts() {
        return facts;
    }

    public void setFacts(List<FactDTO> facts) {
        this.facts = facts;
    }
}
