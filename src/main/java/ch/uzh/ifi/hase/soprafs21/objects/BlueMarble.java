package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

public class BlueMarble extends Marble{
    private Color color = Color.blue;
    private int startFieldNr = 4;
    public BlueMarble(int nr){
        super(nr);
    }
    public int getStartFieldNr() {
        return startFieldNr;
    }
}
