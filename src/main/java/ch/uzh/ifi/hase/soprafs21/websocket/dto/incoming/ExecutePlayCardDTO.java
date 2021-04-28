package ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleExecuteCardDTO;

import java.util.List;

public class ExecutePlayCardDTO {
    private String token;
    private String cardCode;
    private String moveName;
    private String targetFieldKey;
    private List<MarbleExecuteCardDTO> marbleIdList;

    public String getTargetFieldKey() {
        return targetFieldKey;
    }

    //TODO map targetfieldkey to field

    public void setTargetFieldKey(String targetFieldKey) {
        this.targetFieldKey = targetFieldKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }

    public List<MarbleExecuteCardDTO> getMarbleIdList() {
        return marbleIdList;
    }

    public void setMarbleIdList(List<MarbleExecuteCardDTO> marbleIdList) {
        this.marbleIdList = marbleIdList;
    }
}
