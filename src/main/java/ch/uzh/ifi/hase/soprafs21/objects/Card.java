package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.CardSuit;

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

    public Card(String code){
        this.code = code;
        this.cardValue = code.substring(0, 1);
        this.changeCardValueToCardMoveValue(cardValue);
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


    public CardSuit getCardSuit() {
        return cardSuit;
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
    public int changeForwardMoveToValue(String move){
        int moveInt = 0;
        if(move.equals("Forward 2")){
            moveInt = 2;
        } else if (move.equals("Forward 3")){
            moveInt = 3;
        } else if (move.equals("Forward 4")){
            moveInt = 4;
        } else if (move.equals("Forward 5")){
            moveInt = 5;
        } else if (move.equals("Forward 6")){
            moveInt = 6;
        }  else if (move.equals("Forward 8")){
            moveInt = 8;
        } else if (move.equals("Forward 9")){
            moveInt = 9;
        }else if (move.equals("Forward 10")){
            moveInt = 10;
        } else if (move.equals("Forward 11")){
            moveInt = 11;
        } else if (move.equals("Forward 12")){
            moveInt = 12;
        } else if (move.equals("Forward 13")) {
            moveInt = 13;
        }
        return moveInt;
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


