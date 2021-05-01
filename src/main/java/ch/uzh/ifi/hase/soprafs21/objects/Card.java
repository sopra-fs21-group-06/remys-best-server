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


