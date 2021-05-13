package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.AbstractTestUtility;
import ch.uzh.ifi.hase.soprafs21.objects.Game;
import ch.uzh.ifi.hase.soprafs21.objects.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameServiceTest extends AbstractTestUtility {

    @Test
    public void testCanPlay_goToStart_UserCanPlayOneCard() {
        Game game = setupGame();

        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        List<String> cardCodes = List.of("KD", "2D", "4H", "7C", "9S", "JH");
        addCardsToHand(currentPlayer, cardCodes);

        List<String> playableCardCodes = game.getGameService().canPlay(currentPlayer, game);
        assertEquals(1, playableCardCodes.size());
        assertEquals("KD", playableCardCodes.get(0));
    }

    @Test
    public void testCanPlay_goToStart_UserCanPlayMultipleCards() {
        Game game = setupGame();

        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        List<String> cardCodes = List.of("KD", "AH", "X1", "X2", "9S", "JH");
        addCardsToHand(currentPlayer, cardCodes);

        List<String> playableCardCodes = game.getGameService().canPlay(currentPlayer, game);
        assertEquals(4, playableCardCodes.size());
        assertTrue(playableCardCodes.contains("KD"));
        assertTrue(playableCardCodes.contains("AH"));
        assertTrue(playableCardCodes.contains("X1"));
        assertTrue(playableCardCodes.contains("X2"));
    }

    @Test
    public void testCanPlay_goToStart_UserCanNotPlay() {
        Game game = setupGame();

        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        List<String> cardCodes = List.of("2D", "0H", "4H", "7C", "9S", "JH");
        addCardsToHand(currentPlayer, cardCodes);

        List<String> playableCardCodes = game.getGameService().canPlay(currentPlayer, game);
        assertEquals(0, playableCardCodes.size());
    }

    @Test
    public void testEat_sameTargetField_marbleEaten() {
        // TODO Andrina
    }

    @Test
    public void testEat_jumpedOverAnotherMarble_marbleNotEaten() {
        // TODO Andrina
    }
}