package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.CardSuit;

import java.util.ArrayList;
import java.util.HashMap;

public class CardJoker extends Card{
    private HashMap<Integer, ArrayList<Integer>> cardMoveValue = new HashMap<Integer, ArrayList<Integer>>();

    public CardJoker(CardSuit suit, String value, String image, String code) {
        super(suit, value, code);
    }

    @Override
    public void changeCardValueToCardMoveValue(String value) {
        ArrayList<Integer> combis = new ArrayList<Integer>();
        for (int i = 1; i<14; i++){
            if(i==4) {
                combis.add(4);
                combis.add(-4);
            } else {
                combis.add(i);
            }
        }
        cardMoveValue.put(1,combis);
        int h = 6;

        for (int i = 1; i < h; i++){

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

    }
}
