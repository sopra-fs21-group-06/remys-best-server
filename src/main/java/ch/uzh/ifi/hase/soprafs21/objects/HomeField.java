package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

import static java.lang.Boolean.TRUE;

public class HomeField extends Field{
    private final Color color;
    private Boolean isHome = TRUE;

    public HomeField(int fieldValue, Color color) {
        super(fieldValue);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
