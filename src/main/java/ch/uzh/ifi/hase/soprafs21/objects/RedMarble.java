package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

public class RedMarble extends Marble{
    private Color color = Color.red;
    private int startFieldNr = 36;
    public RedMarble(int nr){
        super(nr);
    }

    public int getStartFieldNr() {
        return startFieldNr;
    }
}
