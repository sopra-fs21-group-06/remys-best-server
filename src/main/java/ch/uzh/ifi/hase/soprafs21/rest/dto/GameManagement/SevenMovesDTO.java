package ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleExecuteCardDTO;

import java.util.List;

public class SevenMovesDTO {

    private List<MarbleExecuteCardDTO> sevenMoves;

    public List<MarbleExecuteCardDTO> getSevenMoves() {
        return sevenMoves;
    }

    public void setSevenMoves(List<MarbleExecuteCardDTO> sevenMoves) {
        this.sevenMoves = sevenMoves;
    }
}
