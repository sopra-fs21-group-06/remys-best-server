package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.moves.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Card {
    private final String code; // e.g. "2D"
    private final ArrayList<IMove> moves = new ArrayList<>();

    public Card(String code) {
        this.code = code;
        try {
            this.initMoves(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMoves(String code) throws Exception {
        String value = code.substring(0, 1);
        if("1".equals(value)) {
            moves.add(new OneForwards());
        } else if ("2".equals(value)) {
            moves.add(new TwoForwards());
        } else if ("3".equals(value)){
            moves.add(new ThreeForwards());
        } else if ("4".equals(value)){
            moves.add(new FourForwards());
            moves.add(new FourBackwards());
        } else if ("5".equals(value)){
            moves.add(new FiveForwards());
        } else if ("6".equals(value)) {
            moves.add(new SixForwards());
        } else if ("7".equals(value)){
            //moves.add(new SplitSeven());
        } else if ("8".equals(value)){
            moves.add(new EightForwards());
        } else if ("9".equals(value)){
            moves.add(new NineForwards());
        } else if ("0".equals(value)){
            moves.add(new TenForwards());
        } else if ("J".equals(value)){
            //moves.add(new Exchange());
        } else if ("Q".equals(value)){
            moves.add(new TwelveForwards());
        } else if ("K".equals(value)){
            moves.add(new ThirteenForwards());
            moves.add(new GoToStart());
        } else if ("A".equals(value)){
            moves.add(new OneForwards());
            moves.add(new ElevenForwards());
            moves.add(new GoToStart());
        } else if ("X".equals(value)){
            moves.add(new OneForwards());
            moves.add(new TwoForwards());
            moves.add(new ThreeForwards());
            moves.add(new FourForwards());
            moves.add(new FourBackwards());
            moves.add(new FiveForwards());
            moves.add(new SixForwards());
            //moves.add(new SplitSeven());
            moves.add(new EightForwards());
            moves.add(new NineForwards());
            moves.add(new TenForwards());
            moves.add(new ElevenForwards());
            moves.add(new TwelveForwards());
            moves.add(new ThirteenForwards());
            //moves.add(new Exchange());
            moves.add(new GoToStart());
        } else {
            throw new Exception(String.format("Could not init moves for card value %s", value));
        }
    }

    public String getCode() {
        return code;
    }

    public ArrayList<IMove> getMoves() {
        return new ArrayList<>(moves);
    }
}


