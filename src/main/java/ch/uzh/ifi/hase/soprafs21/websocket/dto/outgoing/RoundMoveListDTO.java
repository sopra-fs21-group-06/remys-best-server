package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.MoveDTO;

import java.util.List;

public class RoundMoveListDTO {
    private List<MoveDTO> moveName;

    public List<MoveDTO> getMoveName() {
        return moveName;
    }

    public void setMoveName(List<MoveDTO> moveName) {
        this.moveName = moveName;
    }
}
