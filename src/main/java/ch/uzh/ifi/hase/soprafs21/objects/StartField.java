package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

import static java.lang.Boolean.TRUE;

public class StartField extends Field {
    private Boolean isStart = TRUE;

    public StartField(int fieldValue, Color color, String fieldKey) {
        super(fieldValue, color, fieldKey);
    }

}
