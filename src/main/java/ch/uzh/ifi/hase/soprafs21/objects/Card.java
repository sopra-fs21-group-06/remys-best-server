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
        switch (value) {
            case "2" -> moves.add(new TwoForwards());
            case "3" -> moves.add(new ThreeForwards());
            case "4" -> {
                moves.add(new FourForwards());
                moves.add(new FourBackwards());
            }
            case "5" -> moves.add(new FiveForwards());
            case "6" -> moves.add(new SixForwards());
            case "7" -> moves.add(new SplitSeven());
            case "8" -> moves.add(new EightForwards());
            case "9" -> moves.add(new NineForwards());
            case "0" -> moves.add(new TenForwards());
            case "J" -> moves.add(new Exchange());
            case "Q" -> moves.add(new TwelveForwards());
            case "K" -> {
                moves.add(new ThirteenForwards());
                moves.add(new GoToStart());
            }
            case "A" -> {
                moves.add(new OneForwards());
                moves.add(new ElevenForwards());
                moves.add(new GoToStart());
            }
            case "X" -> {
                moves.add(new OneForwards());
                moves.add(new TwoForwards());
                moves.add(new ThreeForwards());
                moves.add(new FourForwards());
                moves.add(new FourBackwards());
                moves.add(new FiveForwards());
                moves.add(new SixForwards());
                moves.add(new SplitSeven());
                moves.add(new EightForwards());
                moves.add(new NineForwards());
                moves.add(new TenForwards());
                moves.add(new ElevenForwards());
                moves.add(new TwelveForwards());
                moves.add(new ThirteenForwards());
                moves.add(new Exchange());
                moves.add(new GoToStart());
            }
            default -> throw new Exception(String.format("Could not init moves for card value %s", value));
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
        switch (suit) {
            case "H" -> suitName += "Hearts";
            case "D" -> suitName += "Diamonds";
            case "C" -> suitName += "Clubs";
            default -> suitName += "Spades";
        }

        String cardName = "";
        switch (value) {
            case "2" -> cardName += "Two";
            case "3" -> cardName += "Three";
            case "4" -> cardName += "Four";
            case "5" -> cardName += "Five";
            case "6" -> cardName += "Six";
            case "7" -> cardName += "Seven";
            case "8" -> cardName += "Eight";
            case "9" -> cardName += "Nine";
            case "0" -> cardName += "Ten";
            case "J" -> cardName += "Jack";
            case "Q" -> cardName += "Queen";
            case "K" -> cardName += "King";
            case "A" -> cardName += "Ace";
            default -> cardName += "Card";
        }

        return suitName + " " + cardName;
    }
}


