package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.CardSuit;
import ch.uzh.ifi.hase.soprafs21.moves.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Card {
    private CardSuit cardSuit;
    private ArrayList<Integer> cardMoveValue = new ArrayList<>();
    private String cardValue;
    private String code;
    private boolean canEatWhileSkip = FALSE;
    private List<String> movesToDisplay = new ArrayList<>();
    private Boolean canStart;
    private ArrayList<IMove> moves = new ArrayList<>();

    public Card(String code) {
        this.code = code;
        this.cardValue = code.substring(0, 1);
        this.changeCardValueToCardMoveValue(cardValue);
        try {
            this.initMoves(cardValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMoves(String value) throws Exception {
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

    public ArrayList<IMove> getMoves() {
        return new ArrayList<>(moves);
    }


    public List<String> getMovesToDisplay() {
        return movesToDisplay;
    }

    public String getCardValue() {
        return cardValue;
    }

    public ArrayList<Integer> getCardMoveValue() {
        return cardMoveValue;
    }

    // 15 for changing marbles, 14 for getting out homefields
    // make methods setAce(),setKing() etc.
    public void changeCardValueToCardMoveValue(String value){

        if("X".equals(value)) {
            return;
        } else if ("4".equals(value)) {
            cardMoveValue.add(-4);
            cardMoveValue.add(4);
            movesToDisplay.add("Forward 4");
            movesToDisplay.add("Backwards 4");
            canStart = FALSE;
        } else if ("0".equals(value)){
            cardMoveValue.add(10);
            canStart = FALSE;
            movesToDisplay.add("Forward 10");
        } else if ("J".equals(value)) {
            cardMoveValue.add(-5);
            movesToDisplay.add("Exchange");
            canStart = FALSE;
        } else if ("A".equals(value)){
            cardMoveValue.add(1);
            cardMoveValue.add(11);
            movesToDisplay.add("Forward 11");
            movesToDisplay.add("Forward 1");
            movesToDisplay.add("Go To Start");
            canStart = TRUE;
        } else if ("Q".equals(value)){
            cardMoveValue.add(12);
            canStart = FALSE;
            movesToDisplay.add("Forward 12");
        } else if ("K".equals(value)){
            cardMoveValue.add(13);
            movesToDisplay.add("Forward 13");
            movesToDisplay.add("Go To Start");
            canStart = TRUE;
        } else {
            cardMoveValue.add(Integer.valueOf(value));
            movesToDisplay.add("Forward " + value);
            canStart = FALSE;
        }
    }


    public Boolean getCanStart() {
        return canStart;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}


