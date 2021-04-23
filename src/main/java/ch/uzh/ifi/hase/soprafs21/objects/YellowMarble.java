package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;

public class YellowMarble extends Marble{
    private Color color = Color.yellow;
    private int startFieldNr = 52;
    public YellowMarble(int nr){
        super(nr);
    }
    public int getStartFieldNr() {
        return startFieldNr;
    }
}
