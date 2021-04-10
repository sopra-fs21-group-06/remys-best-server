package ch.uzh.ifi.hase.soprafs21.objects;

import java.util.LinkedList;
import java.util.Stack;

import static java.lang.Boolean.TRUE;

public class PlayingBoard {
    private LinkedList<Field> listPlayingFields;
    private Boolean allStartFieldsFree = TRUE;
    private Stack blueHome = new Stack();
    private Stack greenHome = new Stack();
    private Stack yellowHome = new Stack();
    private Stack redHome = new Stack();
    private LinkedList<Field> redFields = new LinkedList<>();
    private LinkedList<Field> blueField = new LinkedList<>();
    private LinkedList<Field> yellowFields = new LinkedList<>();
    private LinkedList<Field> greenFields = new LinkedList<>();

    public LinkedList<Field> getListPlayingFields() {
        return listPlayingFields;
    }

    public void setBlueHome(Stack blueHome) {
        this.blueHome = blueHome;
    }

    public void setGreenHome(Stack greenHome) {
        this.greenHome = greenHome;
    }

    public void setRedHome(Stack redHome) {
        this.redHome = redHome;
    }

    public void setYellowHome(Stack yellowHome) {
        this.yellowHome = yellowHome;
    }

    public void setListPlayingFields(LinkedList<Field> listPlayingFields) {
        this.listPlayingFields = listPlayingFields;
    }

    public void setBlueField(LinkedList<Field> blueField) {
        this.blueField = blueField;
    }

    public void setGreenFields(LinkedList<Field> greenFields) {
        this.greenFields = greenFields;
    }

    public void setRedFields(LinkedList<Field> redFields) {
        this.redFields = redFields;
    }

    public void setYellowFields(LinkedList<Field> yellowFields) {
        this.yellowFields = yellowFields;
    }
}
