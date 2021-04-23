package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.data.geo.Circle;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Node;

import javax.persistence.criteria.CriteriaBuilder;

import java.util.LinkedList;

import java.util.List;
import java.util.Stack;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class PlayingBoard {
    private LinkedList<Field> listPlayingFields;

    private Stack blueHome = new Stack();
    private Stack greenHome = new Stack();
    private Stack yellowHome = new Stack();
    private Stack redHome = new Stack();
    private LinkedList<Field> redFields = new LinkedList<>();
    private LinkedList<Field> blueField = new LinkedList<>();
    private LinkedList<Field> yellowFields = new LinkedList<>();
    private LinkedList<Field> greenFields = new LinkedList<>();
    private StartField startFieldBlue;
    private StartField startFieldGreen;
    private StartField startFieldRed;
    private StartField startFieldYellow;


    public PlayingBoard(){

        LinkedList<Field> listPlayingFields = new LinkedList<>();

        startFieldBlue = new StartField(4);
        startFieldBlue.setColor(Color.BLUE);
        startFieldBlue.setFieldStatus(FieldStatus.FREE);
        listPlayingFields.add(startFieldBlue);
        for (int i = 5; i <= 19; i++) {
            Field field;
            field = new Field(i);
            listPlayingFields.add(field);
            field.setFieldStatus(FieldStatus.FREE);
        }

        startFieldGreen = new StartField(20);
        startFieldBlue.setColor(Color.GREEN);
        listPlayingFields.add(startFieldGreen);
        startFieldBlue.setFieldStatus(FieldStatus.FREE);
        for (int i = 21; i <= 35; i++) {
            Field field;
            field = new Field(i);
            listPlayingFields.add(field);
            field.setFieldStatus(FieldStatus.FREE);
        }

        startFieldRed = new StartField(36);
        startFieldRed.setColor(Color.RED);
        listPlayingFields.add(startFieldRed);
        startFieldBlue.setFieldStatus(FieldStatus.FREE);
        for (int i = 37; i <= 51; i++) {
            Field field;
            field = new Field(i);
            listPlayingFields.add(field);
            field.setFieldStatus(FieldStatus.FREE);
        }

        startFieldYellow = new StartField(52);
        startFieldYellow.setColor(Color.YELLOW);
        listPlayingFields.add(startFieldYellow);
        startFieldBlue.setFieldStatus(FieldStatus.FREE);
        for (int i = 53; i <= 67; i++) {
            Field field;
            field = new Field(i);
            listPlayingFields.add(field);
            field.setFieldStatus(FieldStatus.FREE);
        }
        setListPlayingFields(listPlayingFields);


        for (Color c: Color.values()) {
            LinkedList<Field> finish = new LinkedList<>();
            for (int i = 0; i <= 4; i++) {
                FinishField finishField = new FinishField(i, c);
                finish.add(finishField);
                finishField.setFieldStatus(FieldStatus.FREE);

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
        for(int i = 0; i < 4; i++){
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
    public List<Integer> getBlockedFieldsValue(){
        int[] postStartField = {4,20,36,52};
        List<Integer> blockedFields = null;
        for (int i = 0; i < 4; i++){
            if(getField(postStartField[i]).getFieldStatus() != FieldStatus.BLOCKED){
                blockedFields.add(postStartField[i]);
            }
        }
        return blockedFields;
    }

    public Boolean checkFinishFieldOccupied(int i, Color color){
        if(color == Color.GREEN){
            if (greenFields.get(i).getFieldStatus() == FieldStatus.FREE) {
                return TRUE;
            }
        } else if (color == Color.RED){
            if (redFields.get(i).getFieldStatus() == FieldStatus.FREE) {
                return TRUE;
            }
        } else if (color == Color.BLUE){
            if (blueField.get(i).getFieldStatus() == FieldStatus.FREE) {
                return TRUE;
            }
        } else if (color == Color.YELLOW) {
            if (yellowFields.get(i).getFieldStatus() == FieldStatus.FREE) {
                return TRUE;
            }

        }
        return FALSE;
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


    public Marble getFirstHomeMarble(Color color){
        Marble m = null;
        if(color == Color.GREEN){
            m = (Marble) greenHome.pop();
        } else if (color == Color.RED){
            m = (Marble) redHome.pop();
        } else if (color == Color.BLUE){
            m =  (Marble) blueHome.pop();
        } else if (color == Color.YELLOW) {
            m = (Marble) yellowHome.pop();
        }
        return m;
    }
    public StartField getStartFieldBlue() {
        return startFieldBlue;
    }

    public StartField getStartFieldGreen() {
        return startFieldGreen;
    }

    public StartField getStartFieldRed() {
        return startFieldRed;
    }

    public StartField getStartFieldYellow() {
        return startFieldYellow;
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
    public Field getField(int i){
        try {
            for(Field f: listPlayingFields)
                if(f.getFieldValue() == i){
                    return f;
            }
        } catch(NullPointerException e){
            System.out.println("Something went wrong in getField()");

        }
        return null;
    }
    public void marbleGoesToStart(Color c){
        Marble m = getFirstHomeMarble(c);
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
        if(color == Color.GREEN){
            return this.getStartFieldGreen();
        } else if (color == Color.RED){
            return this.getStartFieldRed();
        } else if (color == Color.BLUE){
            return this.getStartFieldBlue();
        } else if (color == Color.YELLOW) {
            return this.getStartFieldYellow();
        }
        return null;
    }
    public Boolean getHomeFieldIsNotBlocked(Color color){
        if(color == Color.GREEN && getStartFieldGreen().getFieldStatus() != FieldStatus.BLOCKED){
            return TRUE;
        } else if (color == Color.RED && getStartFieldRed().getFieldStatus() != FieldStatus.BLOCKED){
            return TRUE;
        } else if (color == Color.BLUE && getStartFieldBlue().getFieldStatus() != FieldStatus.BLOCKED){
            return TRUE;
        } else if (color == Color.YELLOW && getStartFieldYellow().getFieldStatus() != FieldStatus.BLOCKED){
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
    public int distanceToNextFreeFinishSpot(Color color, Field field){
        int fieldval = field.getFieldValue();
        int count = 0;
        boolean condition = TRUE;
        int valFieldToCheck = fieldval+1;
        for (Field f : listPlayingFields) {
            if (f.equals(field)) {
                for (int i = fieldval; i < fieldval+63; i++) {
                    if (valFieldToCheck > 67) {
                        valFieldToCheck = valFieldToCheck - 67 + 3;
                    }
                    Field fieldToCheck = listPlayingFields.get(valFieldToCheck);
                    if(fieldToCheck instanceof HomeField && fieldToCheck.getColor().equals(color)){
                        List<Field> finishFields = getFinishFields(color);
                        for (Field finishf: finishFields){
                            if(!(finishf.getFieldStatus().equals(FieldStatus.OCCUPIED))){
                                return count;
                            }
                            count++;
                        }

                    }
                    count++;
                }



            }
    }return count;
    }
    public List<Field> getFinishFields(Color color){

        if(color == Color.GREEN){
            return greenFields;
        } else if (color == Color.RED){
            return redFields;
        } else if (color == Color.BLUE){
            return blueField;
        } else if (color == Color.YELLOW) {
            return yellowFields;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No color get finishfield"));
        }

    }



}
