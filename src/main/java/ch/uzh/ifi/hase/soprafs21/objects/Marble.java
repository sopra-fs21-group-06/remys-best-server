package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

import java.util.List;

public class Marble {
    private Color color;
    private StartField startField;
    private List<FinishField> finishFields;
    private List<HomeField> homeFields;
    private Field currentField;
    private Boolean isHome;
    private Boolean isFinish;

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

    public List<FinishField> getFinishFields() {
        return finishFields;
    }

    public List<HomeField> getHomeFields() {
        return homeFields;
    }

    public StartField getStartField() {
        return startField;
    }

    public void setCurrentField(Field currentField) {
        this.currentField = currentField;
    }

    public void setFinishFields(List<FinishField> finishFields) {
        this.finishFields = finishFields;
    }

    public void setHomeFields(List<HomeField> homeFields) {
        this.homeFields = homeFields;
    }

    public void setStartField(StartField startField) {
        this.startField = startField;
    }

}
