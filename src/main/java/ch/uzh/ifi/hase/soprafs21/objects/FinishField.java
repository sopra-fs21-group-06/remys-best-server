package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

import static java.lang.Boolean.TRUE;

public class FinishField extends Field{
    private Color color;
    private Boolean isFinish = TRUE;
    private String fieldName;


    public FinishField(int fieldValue, Color color) {
        super(fieldValue);
        this.color = color;
    }

}
