package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

public class Marble {
    private final int marbleNr;
    private Color color;
    private Field currentField;
    private Boolean isHome;
    private Boolean isFinish;


    public Marble(int marbleId, Color color){
        this.marbleNr = marbleId;
        this.color = color;
    }

    public int getMarbleNr() {
        return marbleNr;
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
