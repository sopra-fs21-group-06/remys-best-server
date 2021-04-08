package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.CardSuit;

import java.awt.*;

public class Card {
    private CardSuit cardSuit;
    private int value;

    public CardSuit getCardSuit() {
        return cardSuit;
    }

    public int getValue() {
        return value;
    }

    public void setCardSuit(CardSuit cardSuit) {
        this.cardSuit = cardSuit;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
