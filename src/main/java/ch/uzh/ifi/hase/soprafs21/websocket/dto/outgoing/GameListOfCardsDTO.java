package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.GameCardDTO;

import java.util.List;

public class GameListOfCardsDTO {
    private List<GameCardDTO> cardHand;

    public List<GameCardDTO> getCardHand() {
        return cardHand;
    }

    public void setCardHand(List<GameCardDTO> cardHand) {
        this.cardHand = cardHand;
    }
}
