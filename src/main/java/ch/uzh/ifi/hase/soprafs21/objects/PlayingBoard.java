package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

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

    public PlayingBoard(){

        LinkedList<Field> listPlayingFields = new LinkedList<>();

        StartField startFieldBlue = new StartField(5);
        startFieldBlue.setColor(Color.BLUE);
        listPlayingFields.add(startFieldBlue);
        for (int i = 6; i <= 20; i++) {
            Field field;
            field = new Field(i);
            listPlayingFields.add(field);
        }

        StartField startFieldGreen = new StartField(21);
        startFieldBlue.setColor(Color.GREEN);
        listPlayingFields.add(startFieldGreen);
        for (int i = 22; i <= 36; i++) {
            Field field;
            field = new Field(i);
            listPlayingFields.add(field);
        }

        StartField startFieldRed = new StartField(37);
        startFieldRed.setColor(Color.RED);
        listPlayingFields.add(startFieldRed);
        for (int i = 38; i <= 52; i++) {
            Field field;
            field = new Field(i);
            listPlayingFields.add(field);
        }

        StartField startFieldYellow = new StartField(53);
        startFieldYellow.setColor(Color.YELLOW);
        listPlayingFields.add(startFieldYellow);
        for (int i = 54; i <= 68; i++) {
            Field field;
            field = new Field(i);
            listPlayingFields.add(field);
        }
        setListPlayingFields(listPlayingFields);
        for (Color c: Color.values()) {
            LinkedList<Field> finish = new LinkedList<>();
            for (int i = 0; i <= 4; i++) {
                FinishField finishField = new FinishField(i, c);
                finish.add(finishField);
            }
            if (c == Color.BLUE){
                finish.add(0,startFieldBlue);
                setBlueField(finish);
            }
            else if (c == Color.RED){
                finish.add(0, startFieldRed);
                setRedFields(finish);
            }
            else if (c == Color.GREEN){
                finish.add(0,startFieldGreen);
                setGreenFields(finish);
            }
            else if( c == Color.YELLOW){
                finish.add(0,startFieldYellow);
                setYellowFields(finish);
            }
        }
        Stack blueHome = new Stack();
        Stack greenHome = new Stack();
        Stack yellowHome = new Stack();
        Stack redHome = new Stack();
        for(int i = 0; i < 5; i++){
            GreenMarble greenMarble = new GreenMarble(i);
            greenMarble.setHome(TRUE);
            greenHome.push(greenMarble);
            RedMarble redMarble = new RedMarble(i);
            redMarble.setHome(TRUE);
            redHome.push(redMarble);
            BlueMarble blueMarble = new BlueMarble(i);
            blueMarble.setHome(TRUE);
            blueHome.push(blueMarble);
            YellowMarble yellowMarble = new YellowMarble(i);
            yellowMarble.setHome(TRUE);
            yellowHome.push(yellowMarble);
        }
        setBlueHome(blueHome);
        setGreenHome(greenHome);
        setRedHome(redHome);
        setYellowHome(yellowHome);
    }

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
