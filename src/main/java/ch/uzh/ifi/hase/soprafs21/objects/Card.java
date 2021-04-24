package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.CardSuit;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Card {
    private CardSuit cardSuit;
    private ArrayList<Integer> cardMoveValue;
    private String cardValue;
    private String card_id;
    private boolean canEatWhileSkip = FALSE;
    private List<String> movesToDisplay;
    private Boolean canStart;

    public Card(CardSuit suit, String value, String code){
        this.cardSuit = suit;
        this.cardValue = value;
        this.card_id = code;
        this.changeCardValueToCardMoveValue(value);

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

        if (value.matches("[2-3][5-6][8-9]]")) {
            int number = Integer.valueOf(value);
            cardMoveValue.add(number);
            movesToDisplay.add("Forward"+number);
            canStart = FALSE;
        } else if (value.matches("4")) {
            cardMoveValue.add(-4);
            cardMoveValue.add(4);
        } else if (value.matches("0")){
            cardMoveValue.add(10);
        } else if (value.matches("J")) {
            cardMoveValue.add(15);
        } else if   (value.matches("A")){
            cardMoveValue.add(14);
            canStart = FALSE;
        } else if (value.matches("JACK")) {
            movesToDisplay.add("Exchange");
            canStart = FALSE;
        } else if   (value.matches("ACE")){
            cardMoveValue.add(1);
            cardMoveValue.add(11);
        } else if (value.matches("Q")){
            cardMoveValue.add(12);
        }else if (value.matches("K")){
            cardMoveValue.add(14);
            movesToDisplay.add("Forward 12");
            canStart = FALSE;
        }else if (value.matches("KING")){
            cardMoveValue.add(13);
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

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }
}


