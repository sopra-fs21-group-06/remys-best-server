package ch.uzh.ifi.hase.soprafs21.websocket.dto;

public class MarbleExecuteCardDTO {
    private int marbleId;
    private int targetFieldId;

    public MarbleExecuteCardDTO(int marbleId, int targetFieldId){
        this.marbleId = marbleId;
        this.targetFieldId = targetFieldId;
    }

    public int getMarbleId() {
        return marbleId;
    }

    public void setMarbleId(int marbleId) {
        this.marbleId = marbleId;
    }

    public int getTargetFieldId() {
        return targetFieldId;
    }

    public void setTargetFieldId(int targetFieldId) {
        this.targetFieldId = targetFieldId;
    }
}
