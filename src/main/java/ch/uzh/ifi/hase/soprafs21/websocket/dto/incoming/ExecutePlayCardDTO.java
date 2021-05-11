package ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming;

import ch.uzh.ifi.hase.soprafs21.objects.MarbleIdAndTargetFieldKey;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleExecuteCardDTO;

import java.util.ArrayList;
import java.util.List;

public class ExecutePlayCardDTO {
    private String token;
    private String code;
    private String moveName;
    private List<MarbleExecuteCardDTO> marbles;
<<<<<<< Updated upstream
    private MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey;

    public MarbleIdAndTargetFieldKey getMarbleIdAndTargetFieldKey() {
        return marbleIdAndTargetFieldKey;
    }

    public void setMarbleIdAndTargetFieldKey(MarbleIdAndTargetFieldKey marbleIdAndTargetFieldKey) {
        this.marbleIdAndTargetFieldKey = marbleIdAndTargetFieldKey;
=======

    public List<MarbleExecuteCardDTO>  getMarbles() {
        return marbles;
    }

    public void setMarbleIdAndTargetFieldKeys(List<MarbleExecuteCardDTO> marbles) {
        this.marbles = marbles;
>>>>>>> Stashed changes
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

}
