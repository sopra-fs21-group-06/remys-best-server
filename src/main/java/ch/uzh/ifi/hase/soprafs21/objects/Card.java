package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.moves.*;

import java.util.ArrayList;

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
        if ("2".equals(value)) {
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
            moves.add(new Exchange());
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
            moves.add(new Exchange());
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

    public static String convertCardCodeToCardName(String cardCode) {
        String value = cardCode.substring(0, 1);

        if("X".equals(value)) {
            return "Joker";
        }

        String suit = cardCode.substring(1, 2);
        String suitName = "";
        if ("H".equals(suit)) {
            suitName += "Hearts";
        } else if ("D".equals(suit)){
            suitName += "Diamonds";
        } else if ("C".equals(suit)){
            suitName += "Clubs";
        } else {
            suitName += "Spades";
        }

        String cardName = "";
        if ("2".equals(value)) {
            cardName += "Two";
        } else if ("3".equals(value)){
            cardName += "Three";
        } else if ("4".equals(value)){
            cardName += "Four";
        } else if ("5".equals(value)){
            cardName += "Five";
        } else if ("6".equals(value)) {
            cardName += "Six";
        } else if ("7".equals(value)){
            cardName += "Seven";
        } else if ("8".equals(value)){
            cardName += "Eight";
        } else if ("9".equals(value)){
            cardName += "Nine";
        } else if ("0".equals(value)){
            cardName += "Ten";
        } else if ("J".equals(value)){
            cardName += "Jack";
        } else if ("Q".equals(value)){
            cardName += "Queen";
        } else if ("K".equals(value)){
            cardName += "King";
        } else if ("A".equals(value)){
            cardName += "Ace";
        } else {
            cardName += "Card";
        }

        return suitName + " " + cardName;
    }
}


