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
    private List<String> movesToDisplay;
    private Boolean canStart;
    private Boolean normalPlayingCard;

    public Card(CardSuit suit, String value, String image, String code){
        this.cardSuit = suit;
        this.cardValue = value;
        this.image = image;
        this.card_id = code;
        this.changeCardValueToCardMoveValue(value);

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
            movesToDisplay.add("Forward"+"number");
            canStart = FALSE;
        } else if (value.matches("4")) {
            cardMoveValue.add(-4);
            cardMoveValue.add(4);
            movesToDisplay.add("Forward 4");
            movesToDisplay.add("Backwards 4");
            canStart = FALSE;
        } else if (value.matches("TEN")){
            movesToDisplay.add("Forward 10");
            cardMoveValue.add(10);
            canStart = FALSE;
        } else if (value.matches("JACK")) {
            cardMoveValue.add(15);
            movesToDisplay.add("Exchange");
            canStart = FALSE;
        } else if   (value.matches("ACE")){
            cardMoveValue.add(14);
            cardMoveValue.add(1);
            cardMoveValue.add(11);
            movesToDisplay.add("Forward 1");
            movesToDisplay.add("Forward 11");
            movesToDisplay.add("Go To Start");
            canStart = TRUE;
        } else if (value.matches("QUEEN")){
            cardMoveValue.add(12);
            movesToDisplay.add("Forward 12");
            canStart = FALSE;
        }else if (value.matches("KING")){
            cardMoveValue.add(14);
            cardMoveValue.add(13);
            movesToDisplay.add("Forward 13");
            movesToDisplay.add("Go To Start");
            canStart = TRUE;
        }
    }

    public Boolean getCanStart() {
        return canStart;
    }
}
