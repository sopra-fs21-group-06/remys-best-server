package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;


public class PlayingBoard {
    Logger log = LoggerFactory.getLogger(PlayingBoard.class);
    private final LinkedList<Field> listPlayingFields = new LinkedList<>();
    private final Stack<Marble> blueHome = new Stack<>();
    private final Stack<Marble> greenHome = new Stack<>();
    private final Stack<Marble> yellowHome = new Stack<>();
    private final Stack<Marble> redHome = new Stack<>();
    private final LinkedList<Field> redFields = new LinkedList<>();
    private final LinkedList<Field> blueFields = new LinkedList<>();
    private final LinkedList<Field> yellowFields = new LinkedList<>();
    private final LinkedList<Field> greenFields = new LinkedList<>();
    private final List<Marble> blueMarbles = new ArrayList<>();
    private final List<Marble> redMarbles = new ArrayList<>();
    private final List<Marble> greenMarbles = new ArrayList<>();
    private final List<Marble> yellowMarbles = new ArrayList<>();

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
                if(i == 16){
                    StartField startField = new StartField(i, enumColor);
                    startField.setFieldStatus(FieldStatus.FREE);
                    listPlayingFields.add(startField);
                } else {
                    Field field = new Field(i, enumColor);
                    field.setFieldStatus(FieldStatus.FREE);
                    listPlayingFields.add(field);
                }
            }
            for(int j = 17; j < 21; j++){
                FinishField finishField = new FinishField(j, enumColor);
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
            Marble blueMarble = new Marble(i, Color.BLUE);
            blueMarble.setHome(TRUE);
            blueMarble.setFinish(FALSE);
            blueHome.push(blueMarble);
            blueMarbles.add(blueMarble);
            Marble greenMarble = new Marble(i + 4, Color.GREEN);
            greenMarble.setHome(TRUE);
            greenMarble.setFinish(FALSE);
            greenHome.push(greenMarble);
            greenMarbles.add(greenMarble);
            Marble redMarble = new Marble(i + 8, Color.RED);
            redMarble.setHome(TRUE);
            redMarble.setFinish(FALSE);
            redHome.push(redMarble);
            redMarbles.add(redMarble);
            Marble yellowMarble = new Marble(i + 12, Color.YELLOW);
            yellowMarble.setHome(TRUE);
            yellowMarble.setFinish(FALSE);
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

    public LinkedList<Field> getListPlayingFields() {
        return listPlayingFields;
    }

    public Boolean hasMarbleOnHomeStack(Color color){
        if(color == Color.GREEN){
            return (!(greenHome.isEmpty()));
        } else if (color == Color.RED){
            return (!(redHome.isEmpty()));
        } else if (color == Color.BLUE){
            return (!(blueHome.isEmpty()));
        } else {
            return (!(yellowHome.isEmpty()));
        }
    }
    public int getNumberMarblesAtHome(Color color){
        if(color == Color.GREEN){
            return greenHome.size();
        } else if (color == Color.RED){
            return redHome.size();
        } else if (color == Color.BLUE){
            return blueHome.size();
        } else {
            return yellowHome.size();
        }
    }

    public Marble getFirstHomeMarble(Color color, boolean removeFromStack){
        Marble m = null;
        if(color == Color.GREEN){
            if(removeFromStack) {
                m = greenHome.pop();
            } else {
                m = greenHome.peek();
            }
        } else if (color == Color.RED){
            if(removeFromStack) {
                m = redHome.pop();
            } else {
                m = redHome.peek();
            }
        } else if (color == Color.BLUE){
            if(removeFromStack) {
                m = blueHome.pop();
            } else {
                m = blueHome.peek();
            }
        } else if (color == Color.YELLOW) {
            if(removeFromStack) {
                m = yellowHome.pop();
            } else {
                m = yellowHome.peek();
            }
        }
        return m;
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
        }
        return fieldToSend;

    }
    public Field getFieldByFieldKey(String key) {
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
    public Marble makeStartMove(Color color) {
        Marble marble = getFirstHomeMarble(color, true);
        StartField targetField = getRightColorStartField(color);
        targetField.setFieldStatus(FieldStatus.BLOCKED);
        targetField.setMarble(marble);
        marble.setCurrentField(targetField);
        marble.setHome(FALSE);
        return marble;
    }

    public void makeJackMove(Field fieldToChange, Marble m){
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
        if(finishFieldIsFinishMoveField(fieldToChange)){
            m.setFinish(TRUE);
        }
    }

    public boolean finishFieldIsFinishMoveField(Field finishField){
        List<Field> finishFields = getFinishFields(finishField.getColor());
        int stepsCount = nrStepsToNextFreeFinishSpot(finishField);
        int positionField = 20 - finishField.getFieldValue();
        int countMarble = 0;
        for(Field f: finishFields) {
            if (f.getFieldValue() > finishField.getFieldValue()) {
                if (f.getFieldStatus().equals(FieldStatus.BLOCKED)) {
                    countMarble++;
                }
            }
        }
        if(stepsCount == 0 && positionField == countMarble){
            log.info("Finishfield" + finishField.getFieldKey() + "is actual finishfield in finsifieldmove");
            return TRUE;
        } else {
            log.info("Finishfield" + finishField.getFieldKey() + "is not finishfield in finsifieldmove");
            return FALSE;
        }
    }

    // Start is at first HomeField if all are interesting, first check new fieldKey is bigger than old and if the field is occupierd
    public int nrStepsToNextFreeFinishSpot(Field startField) {
        Color c = startField.getColor();
        List<Field> finishFields = getFinishFields(c);
        int count = 0;
        for(Field f: finishFields){
            if(f.getFieldValue() > startField.getFieldValue()){
                if(f.getFieldStatus().equals(FieldStatus.OCCUPIED)){
                    return count;
                }
                count++;
            }
        }
        return count;
    }

    // return nr to next start block if the next one is blocked else return 100
    public int nrStepsToNextStartFieldBlock(Field startingFieldMove){
        Color currentColor = startingFieldMove.getColor();
        if(getNextStartFieldIsBlocked(currentColor) && !startingFieldMove.getFieldStatus().equals(FieldStatus.BLOCKED)){
            return 16 - startingFieldMove.getFieldValue();
        } else {
            return 100;
        }
    }

    public StartField getRightColorStartField(Color color)  {
        return (StartField) this.getField(16,color);
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
        } else if (m.getColor() == Color.RED){
            redHome.push(m);
        } else if (m.getColor() == Color.BLUE){
            blueHome.push(m);
        } else if (m.getColor() == Color.YELLOW) {
            yellowHome.push(m);
        }
        m.setHome(TRUE);
        m.setCurrentField(null);
    }

    public List<Field> getFinishFields(Color color){
        if(color == Color.GREEN){
            return greenFields;
        } else if (color == Color.RED){
            return redFields;
        } else if (color == Color.BLUE){
            return blueFields;
        } else {
            return yellowFields;
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
    //Only for marbles outside finishpsot
    public boolean marbleCanGetToFinishSpot(Marble marble, Field field, int valueCard){
        boolean marbleCanFinish = FALSE;
        int distanceToStart = 16 - field.getFieldValue();
        int distanceFreeFinish = nrStepsToNextFreeFinishSpot(field);
        if(marble.getColor().equals(field.getColor()) && !field.getFieldStatus().equals(FieldStatus.BLOCKED) && !getNextStartFieldIsBlocked(field.getColor())){
            if(distanceToStart + distanceFreeFinish >= valueCard && distanceToStart < valueCard){
                String s = String.valueOf(marble.getMarbleId());
                log.info("Marlbe " + s +" can finish");
                marbleCanFinish = TRUE;
            }
        }
        return marbleCanFinish;
    }

}

