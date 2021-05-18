package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;

public class Field {
    private FieldStatus fieldStatus;
    private Marble marble;
    private int fieldValue;
    private Boolean isHome;
    private Color color;
    private String fieldKey;

    public Field(int fieldValue, Color color){
        this.fieldValue = fieldValue;
        this.color = color;
        this.fieldKey = String.valueOf(fieldValue).concat(color.getId());
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public Boolean getHome() {
        return isHome;
    }

    public FieldStatus getFieldStatus() {
        return fieldStatus;
    }

    public int getFieldValue() {
        return fieldValue;
    }

    public Marble getMarble() {
        return marble;
    }

    public void setFieldStatus(FieldStatus fieldStatus) {
        this.fieldStatus = fieldStatus;
    }

    public void setHome(Boolean home) {
        isHome = home;
    }

    public void setMarble(Marble marble) {
        this.marble = marble;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}

