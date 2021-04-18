package ch.uzh.ifi.hase.soprafs21.websocket.dto;

public class MarbleExecuteCardDTO {
    private String marbleId;
    private String targetFieldId;

    public String getMarbleId() {
        return marbleId;
    }

    public void setMarbleId(String marbleId) {
        this.marbleId = marbleId;
    }

    public String getTargetFieldId() {
        return targetFieldId;
    }

    public void setTargetFieldId(String targetFieldId) {
        this.targetFieldId = targetFieldId;
    }
}
