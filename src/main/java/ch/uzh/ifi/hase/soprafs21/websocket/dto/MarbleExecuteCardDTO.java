package ch.uzh.ifi.hase.soprafs21.websocket.dto;

public class MarbleExecuteCardDTO {
    private int marbleId;
    private String targetFieldKey;

    public MarbleExecuteCardDTO(int marbleId, String targetFieldKey) {
        this.marbleId = marbleId;
        this.targetFieldKey = targetFieldKey;
    }

    public int getMarbleId() {
        return marbleId;
    }

    public void setMarbleId(int marbleId) {
        this.marbleId = marbleId;
    }

    public String getTargetFieldKey() {
        return targetFieldKey;
    }

    public void setTargetFieldKey(String targetFieldKey) {
        this.targetFieldKey = targetFieldKey;
    }
}
