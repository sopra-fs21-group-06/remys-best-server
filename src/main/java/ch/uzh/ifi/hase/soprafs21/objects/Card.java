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
    private String image;
    private String card_id;
    private boolean canEatWhileSkip = FALSE;

    public Card(CardSuit suit, String value, String image, String code){
        this.cardSuit = suit;
        this.cardValue = value;
        this.image = image;
        this.card_id = code;
        this.changeCardValueToCardMoveValue(value);
    }

    public CardSuit getCardSuit() {
        return cardSuit;
    }
    // 15 for changing marbles, 14 for getting out homefields
    // make methods setAce(),setKing() etc.
    public void changeCardValueToCardMoveValue(String value) {

        if (value.matches("[2-3][5-6][8-9]]")) {
            int number = Integer.valueOf(value);
            cardMoveValue.add(number);
        }
        else if (value.matches("4")) {
            cardMoveValue.add(-4);
            cardMoveValue.add(4);
        }
        else if (value.matches("0")) {
            cardMoveValue.add(10);
        }
        else if (value.matches("J")) {
            cardMoveValue.add(15);
        }
        else if (value.matches("A")) {
            cardMoveValue.add(14);
            cardMoveValue.add(1);
            cardMoveValue.add(11);
        }
        else if (value.matches("Q")) {
            cardMoveValue.add(12);
        }
        else if (value.matches("K")) {
            cardMoveValue.add(14);
            cardMoveValue.add(13);
        }
    }



}
