package ch.uzh.ifi.hase.soprafs21.objects;

public class MarbleIdAndTargetFieldKey {

    private int marbleId;
    private String fieldKey;

    public MarbleIdAndTargetFieldKey(int marbleId, String fieldKey) {
        this.marbleId = marbleId;
        this.fieldKey = fieldKey;
    }

    public int getMarbleId() {
        return marbleId;
    }

    public void setMarbleId(int marbleId) {
        this.marbleId = marbleId;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }
}
