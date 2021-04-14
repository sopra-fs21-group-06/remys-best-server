package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleDTO;

import java.util.List;

public class RoundMarbleListDTO {
    private List<MarbleDTO> marbleList;

    public List<MarbleDTO> getMarbleList() {
        return marbleList;
    }

    public void setMarbleList(List<MarbleDTO> marbleList) {
        this.marbleList = marbleList;
    }
}
