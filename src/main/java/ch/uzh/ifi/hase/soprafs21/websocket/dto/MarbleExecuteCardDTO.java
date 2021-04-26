package ch.uzh.ifi.hase.soprafs21.websocket.dto;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

public class MarbleExecuteCardDTO {
    private int marbleId;
    private int targetFieldId;
    private Color targetFieldColor;

    public MarbleExecuteCardDTO(int marbleId, int targetFieldId, Color color) {
        this.marbleId = marbleId;
        this.targetFieldId = targetFieldId;
        this.targetFieldColor = color;
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

    public Color getTargetFieldColor() {
        return targetFieldColor;
    }

    public void setTargetFieldColor(Color targetFieldColor) {
        this.targetFieldColor = targetFieldColor;
    }
}
