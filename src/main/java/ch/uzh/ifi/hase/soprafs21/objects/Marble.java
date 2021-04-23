package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;

import java.util.List;
import java.util.Stack;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Marble {
    private int marbleNr;
    private Color color;

    private Field currentField;
    private Boolean isHome;
    private Boolean isFinish;


    public Marble(int nr){
        this.marbleNr = nr;
    }

    public Boolean isOnFieldandNotOnStart(){
        if(!this.isHome && !this.isFinish && (this.currentField.getFieldStatus() != FieldStatus.blocked)){
            if(this.currentField.getFinish())
            return TRUE;
        }
        return FALSE;
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
<<<<<<< Updated upstream
=======
    public int nextStartFieldValue(){
        int i = this.getCurrentField().getFieldValue();
        int distance = 20 - (i%16);
        return distance + i;
    }
    public int distanceToNextStartField(){
        int i = this.getCurrentField().getFieldValue();
        return 20 - (i%16);
    }
    public Boolean getMarbleIsBlockingAndOnStart(){
        if (this.getCurrentField() instanceof StartField){
            if(this.getColor().equals(this.getCurrentField().getColor())){
                if(this.currentField.getFieldStatus().equals(FieldStatus.blocked)){
                    return TRUE;
                }
            }
        }
        return FALSE;
    }



>>>>>>> Stashed changes


}
