package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.GameCardDTO;

import java.util.List;

public class GameListOfCardsDTO {
    private List<GameCardDTO> cards;

    public List<GameCardDTO> getCards() {
        return cards;
    }

    public void setCards(List<GameCardDTO> cards) {
        this.cards = cards;
    }
}
