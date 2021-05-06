package ch.uzh.ifi.hase.soprafs21.objects;

public class MarbleAndTargetField {
    private Marble marble;
    private Field targetField;

    public MarbleAndTargetField(Marble marble, Field targetField) {
        this.marble = marble;
        this.targetField = targetField;
    }

    public Marble getMarble() {
        return marble;
    }

    public void setMarble(Marble marble) {
        this.marble = marble;
    }

    public Field getTargetField() {
        return targetField;
    }

    public void setTargetField(Field targetField) {
        this.targetField = targetField;
    }
}
