package ch.uzh.ifi.hase.soprafs21.rest.dto.GameManagement;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleExecuteCardDTO;

import java.util.List;

public class PossibleTargetFieldsDTO {
    private String code;
    private String moveName;
    private int marbleId;
    private List<MarbleExecuteCardDTO> sevenMoves;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }

    public int getMarbleId() {
        return marbleId;
    }

    public void setMarbleId(int marbleId) {
        this.marbleId = marbleId;
    }

    public List<MarbleExecuteCardDTO> getSevenMoves() {
        return sevenMoves;
    }

    public void setSevenMoves(List<MarbleExecuteCardDTO> sevenMoves) {
        this.sevenMoves = sevenMoves;
    }
}
