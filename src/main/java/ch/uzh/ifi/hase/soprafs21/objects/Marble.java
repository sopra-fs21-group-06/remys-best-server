package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

import java.util.List;
import java.util.Stack;

public class Marble {
    private int marbleNr;
    private Color color;

    private Field currentField;
    private Boolean isHome;
    private Boolean isFinish;


    Marble(int nr){
        this.marbleNr = nr;
    }

    public void setHome(Boolean home) {
        isHome = home;
    }

    public void setFinish(Boolean finish) {
        isFinish = finish;
    }

    public Boolean getHome() {
        return isHome;
    }

    public Boolean getFinish() {
        return isFinish;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Field getCurrentField() {
        return currentField;
    }


    public void setCurrentField(Field currentField) {
        this.currentField = currentField;
    }


}
