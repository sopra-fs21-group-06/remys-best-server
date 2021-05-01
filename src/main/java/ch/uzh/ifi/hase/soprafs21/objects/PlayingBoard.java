package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.data.geo.Circle;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Node;

import javax.persistence.criteria.CriteriaBuilder;

import java.util.ArrayList;
import java.util.LinkedList;

import java.util.List;
import java.util.Stack;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;


public class PlayingBoard {
    private LinkedList<Field> listPlayingFields = new LinkedList<>();

    private Stack blueHome = new Stack();
    private Stack greenHome = new Stack();
    private Stack yellowHome = new Stack();
    private Stack redHome = new Stack();
    private LinkedList<Field> redFields = new LinkedList<>();
    private LinkedList<Field> blueFields = new LinkedList<>();
    private LinkedList<Field> yellowFields = new LinkedList<>();
    private LinkedList<Field> greenFields = new LinkedList<>();
    private List<Marble> blueMarbles = new ArrayList<>();
    private List<Marble> redMarbles = new ArrayList<>();
    private List<Marble> greenMarbles = new ArrayList<>();
    private List<Marble> yellowMarbles = new ArrayList<>();

    public PlayingBoard(){
        String[] colorBoard = {"BLUE", "GREEN", "RED", "YELLOW"};
        for(String c: colorBoard){
            Color enumColor;

            if(c.equals("BLUE")){
                enumColor = Color.BLUE;
            } else if (c.equals("GREEN")){
                enumColor = Color.GREEN;
            } else if (c.equals("RED")){
                enumColor = Color.RED;
            } else {
                enumColor = Color.YELLOW;
            }
            for(int i = 1; i <17; i++){
                String s = String.valueOf(i);
                if(i == 16){
                    StartField startField = new StartField(i, enumColor, s+c);
                    startField.setFieldStatus(FieldStatus.FREE);
                    listPlayingFields.add(startField);
                } else {
                    Field field = new Field(i, enumColor, s+c);
                    field.setFieldStatus(FieldStatus.FREE);
                    listPlayingFields.add(field);
                }


            }
            for(int j = 17; j < 21; j++){
                String s = String.valueOf(j);
                FinishField finishField = new FinishField(j, enumColor, s+c);
                finishField.setFieldStatus(FieldStatus.FREE);
                if(c.equals("BLUE")){
                    blueFields.add(finishField);
                } else if (c.equals("GREEN")){
                    greenFields.add(finishField);
                } else if (c.equals("RED")){
                    redFields.add(finishField);
                } else {
                    yellowFields.add(finishField);
                }
            }
        }


        for(int i = 3; i >= 0; i--){
            Marble blueMarble = new BlueMarble(i);
            blueMarble.setColor(Color.BLUE);
            blueMarble.setHome(TRUE);
            blueMarble.setFinish(FALSE);
            blueHome.push(blueMarble);
            blueMarbles.add(blueMarble);
            Marble greenMarble = new GreenMarble(i + 4);
            greenMarble.setColor(Color.GREEN);
            greenMarble.setHome(TRUE);
            greenMarble.setFinish(FALSE);
            greenHome.push(greenMarble);
            greenMarbles.add(greenMarble);
            Marble redMarble = new RedMarble(i + 8);
            redMarble.setHome(TRUE);
            redMarble.setFinish(FALSE);
            redMarble.setColor(Color.RED);
            redHome.push(redMarble);
            redMarbles.add(redMarble);
            Marble yellowMarble = new YellowMarble(i + 12);
            yellowMarble.setHome(TRUE);
            yellowMarble.setFinish(FALSE);
            yellowMarble.setColor(Color.YELLOW);
            yellowHome.push(yellowMarble);
            yellowMarbles.add(yellowMarble);
        }
    }

    public List<Marble> getBlueMarbles() {
        return blueMarbles;
    }

    public List<Marble> getRedMarbles() {
        return redMarbles;
    }

    public List<Marble> getGreenMarbles() {
        return greenMarbles;
    }

    public List<Marble> getYellowMarbles() {
        return yellowMarbles;
    }


    // Check Function if certain finishFiel is free.
    public Boolean checkFinishFieldOccupied(int i, Color color){
        if ((getField(i,color).getFieldStatus().equals(FieldStatus.FREE))){
            return FALSE;
        } else {
            return TRUE;
        }
    }
    public Boolean hasMarbleOnHomeStack(Color color){
        if(color == Color.GREEN){
            return (!(greenHome.isEmpty()));
        } else if (color == Color.RED){
            return (!(redHome.isEmpty()));
        } else if (color == Color.BLUE){
            return (!(blueHome.isEmpty()));
        } else if (color == Color.YELLOW) {
            return (!(yellowHome.isEmpty()));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No color get finishfield"));
        }
    }

    public Marble getFirstHomeMarble(Color color, boolean removeFromStack){
        Marble m = null;

        if(color == Color.GREEN){
            if(removeFromStack) {
                m = (Marble) greenHome.pop();
            } else {
                m = (Marble) greenHome.peek();
            }
        } else if (color == Color.RED){
            if(removeFromStack) {
                m = (Marble) redHome.pop();
            } else {
                m = (Marble) redHome.peek();
            }
        } else if (color == Color.BLUE){
            if(removeFromStack) {
                m = (Marble) blueHome.pop();
            } else {
                m = (Marble) blueHome.peek();
            }
        } else if (color == Color.YELLOW) {
            if(removeFromStack) {
                m = (Marble) yellowHome.pop();
            } else {
                m = (Marble) yellowHome.peek();
            }
        }
        return m;
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



    public Field getField(int i, Color c){
        Field fieldToSend = null;
        for(Field f: listPlayingFields) {

            if (f.getFieldValue() == i && f.getColor().equals(c)) {
                fieldToSend = f;
            }
        }
        for(Field f: greenFields){
            if (f.getFieldValue() == i && f.getColor().equals(c)) {
                fieldToSend = f;
            }
        }
        for(Field f: redFields){
            if (f.getFieldValue() == i && f.getColor().equals(c)) {
                fieldToSend = f;
            }
        }
        for(Field f: yellowFields){
            if (f.getFieldValue() == i && f.getColor().equals(c)) {
                fieldToSend = f;
            }
        }
        for(Field f: blueFields){
            if (f.getFieldValue() == i && f.getColor().equals(c)) {
                fieldToSend = f;
            }
        }
        if (isNull(fieldToSend)) {
            System.out.println("Something went wrong in getField()");
        } else {
            System.out.println("all ok  getField()");
        }
        return fieldToSend;

    }
    public Field getFieldWithFieldKey(String key) {
        Field fieldToSend = null;
        for (Field f : listPlayingFields) {

            if (f.getFieldKey().equals(key)) {
                fieldToSend = f;
            }
        }
        for (Field f : greenFields) {
            if (f.getFieldKey().equals(key)) {
                fieldToSend = f;
            }
        }
        for (Field f : redFields) {
            if (f.getFieldKey().equals(key)) {
                fieldToSend = f;
            }
        }
        for (Field f : yellowFields) {
            if (f.getFieldKey().equals(key)) {
                fieldToSend = f;
            }
        }
        for (Field f : blueFields) {
            if (f.getFieldKey().equals(key)) {
                fieldToSend = f;
            }
        }
        return fieldToSend;
    }
    // Marble m is the first on the stack,
    public void marbleGoesToStart(Color c){
        Marble m = getFirstHomeMarble(c, true);
        StartField field = getRightColorStartField(c);
        field.setFieldStatus(FieldStatus.BLOCKED);
        field.setMarble(m);
        m.setCurrentField(field);
        m.setHome(FALSE);
    }
    public void marbleMoveJack(Field fieldToChange, Marble m){
        Field playerField = m.getCurrentField();
        Field mate = fieldToChange;
        Marble marbleMate = fieldToChange.getMarble();
        marbleMate.setCurrentField(playerField);
        m.setCurrentField(fieldToChange);
        playerField.setMarble(m);
        mate.setMarble(marbleMate);

    }
    public void makeMove(Field fieldToChange, Marble m){
        m.getCurrentField().setFieldStatus(FieldStatus.FREE);
        m.getCurrentField().setMarble(null);
        m.setCurrentField(fieldToChange);
        fieldToChange.setMarble(m);
        fieldToChange.setFieldStatus(FieldStatus.OCCUPIED);
    }
    public void makeFinishMove(Field fieldToChange, Marble m){
        m.getCurrentField().setFieldStatus(FieldStatus.FREE);
        m.getCurrentField().setMarble(null);
        m.setCurrentField(fieldToChange);
        fieldToChange.setMarble(m);
        fieldToChange.setFieldStatus(FieldStatus.OCCUPIED);
        m.setFinish(TRUE);
    }
    public StartField getRightColorStartField(Color color)  {
        return (StartField) this.getField(16,color);
    }
    //Takes in Color and return true if this HomeField is not blocked
    public Boolean getStartFieldIsNotBlocked(Color color){
        if(!( getRightColorStartField(color).getFieldStatus().equals(FieldStatus.BLOCKED) )){
            return TRUE;
        } else {
            return FALSE;
        }
    }
    public Boolean getNextStartFieldIsBlocked(Color color){
        if ( getRightColorStartField(color).getFieldStatus().equals(FieldStatus.BLOCKED) ){
            return TRUE;
        } else {
            return FALSE;
        }
    }
    public void sendHome(Marble m){
        if(m.getColor() == Color.GREEN){
            greenHome.push(m);
            m.setHome(TRUE);
        } else if (m.getColor() == Color.RED){
            redHome.push(m);
            m.setHome(TRUE);
        } else if (m.getColor() == Color.BLUE){
            blueHome.push(m);
            m.setHome(TRUE);
        } else if (m.getColor() == Color.YELLOW) {
            yellowHome.push(m);
            m.setHome(TRUE);
        }
    }

    //Done
    public List<Field> getFinishFields(Color color){
        if(color == Color.GREEN){
            return greenFields;
        } else if (color == Color.RED){
            return redFields;
        } else if (color == Color.BLUE){
            return blueFields;
        } else if (color == Color.YELLOW) {
            return yellowFields;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No color get finishfield"));
        }

    }
    public Color getNextColor(Color color){
        if(color == Color.GREEN){
            return Color.RED;
        } else if (color == Color.RED){
            return Color.YELLOW;
        } else if (color == Color.BLUE){
            return Color.GREEN;
        } else {
            return Color.BLUE;
        }
    }
    public Color getPreviousColor(Color color){
        if(color == Color.GREEN){
            return Color.BLUE;
        } else if (color == Color.RED){
            return Color.GREEN;
        } else if (color == Color.BLUE){
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }
    public int changeForwardMoveToValue(String move){
        int moveInt = 0;
        if(move.equals("Forward 1")) {
            moveInt = 1;
        } else if (move.equals("Forward 2")){
            moveInt = 2;
        } else if (move.equals("Forward 3")){
            moveInt = 3;
        } else if (move.equals("Forward 4")){
            moveInt = 4;
        } else if (move.equals("Forward 5")){
            moveInt = 5;
        } else if (move.equals("Forward 6")){
            moveInt = 6;
        }  else if (move.equals("Forward 8")){
            moveInt = 8;
        } else if (move.equals("Forward 9")){
            moveInt = 9;
        }else if (move.equals("Forward 10")){
            moveInt = 10;
        } else if (move.equals("Forward 11")){
            moveInt = 11;
        } else if (move.equals("Forward 12")){
            moveInt = 12;
        } else if (move.equals("Forward 13")) {
            moveInt = 13;
        }
        return moveInt;
    }

}

