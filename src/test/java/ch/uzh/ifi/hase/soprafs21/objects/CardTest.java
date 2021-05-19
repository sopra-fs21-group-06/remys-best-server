package ch.uzh.ifi.hase.soprafs21.objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {

    @Test
    public void testConvertCardCodeToCardName(){
        assertEquals("Hearts Two", Card.convertCardCodeToCardName("2H"));
        assertEquals("Diamonds Three", Card.convertCardCodeToCardName("3D"));
        assertEquals("Clubs Four", Card.convertCardCodeToCardName("4C"));
        assertEquals("Spades Five", Card.convertCardCodeToCardName("5S"));
        assertEquals("Hearts Six", Card.convertCardCodeToCardName("6H"));
        assertEquals("Hearts Seven", Card.convertCardCodeToCardName("7H"));
        assertEquals("Hearts Eight", Card.convertCardCodeToCardName("8H"));
        assertEquals("Hearts Nine", Card.convertCardCodeToCardName("9H"));
        assertEquals("Hearts Ten", Card.convertCardCodeToCardName("0H"));
        assertEquals("Hearts Jack", Card.convertCardCodeToCardName("JH"));
        assertEquals("Hearts Queen", Card.convertCardCodeToCardName("QH"));
        assertEquals("Hearts King", Card.convertCardCodeToCardName("KH"));
        assertEquals("Hearts Ace", Card.convertCardCodeToCardName("AH"));
    }
}
