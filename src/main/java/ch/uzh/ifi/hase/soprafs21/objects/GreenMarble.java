package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

public class GreenMarble extends Marble{
    private Color color = Color.GREEN;
    private int startFieldNr = 20;
    public GreenMarble(int nr){
        super(nr);
    }
    public int getStartFieldNr() {
        return startFieldNr;
    }
}
