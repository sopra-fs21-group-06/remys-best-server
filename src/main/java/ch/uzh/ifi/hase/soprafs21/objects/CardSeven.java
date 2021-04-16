package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.CardSuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class CardSeven extends Card{
    private HashMap<Integer, ArrayList<Integer>> cardMoveValue = new HashMap<Integer, ArrayList<Integer>>();
    private boolean canEatWhileSkip = TRUE;
    private List<String> movesToDisplay;
    private boolean canStart;
    public CardSeven(CardSuit suit, String value, String image, String code) {
        super(suit, value, image, code);
    }

    @Override
    public void changeCardValueToCardMoveValue(String value) {
        canStart = FALSE;
        ArrayList<Integer> sevenWithOneMarble = new ArrayList<Integer>();
        sevenWithOneMarble.add(7);
        cardMoveValue.put(1,sevenWithOneMarble);
        int h = 6;
        ArrayList<Integer> combis = new ArrayList<Integer>();
        for (int i = 1; i < h; i++) {
            combis.add(i);
            combis.add(h);
            cardMoveValue.put(2, combis);
            combis.clear();
            h--;
        }
        combis.add(2);
        combis.add(2);
        combis.add(3);
        cardMoveValue.put(3,combis);
        combis.clear();
        int b = 1;
        h = 5;
        for (int i = 1; i == h; i++){
            combis.add(i);
            combis.add(h);
            combis.add(b);
            cardMoveValue.put(3, combis);
            combis.clear();
            h--;
        }
        combis.add(1);
        combis.add(2);
        combis.add(2);
        combis.add(2);
        cardMoveValue.put(4,combis);
        combis.clear();
        combis.add(1);
        combis.add(1);
        combis.add(1);
        combis.add(4);
        cardMoveValue.put(4,combis);
        combis.clear();
        combis.add(1);
        combis.add(1);
        combis.add(2);
        combis.add(3);
        cardMoveValue.put(4,combis);
        combis.clear();
        movesToDisplay.add("Split");



    }
}
