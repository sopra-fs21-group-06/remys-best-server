package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.AbstractTest;
import ch.uzh.ifi.hase.soprafs21.objects.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Boolean.FALSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameServiceTest extends AbstractTest {
    Logger log = LoggerFactory.getLogger(GameServiceTest.class);
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
    public void testChangePlayer_skipPlayer() {
        Game game = setupGame();
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        List<String> cardCodesPlayer1 = List.of("2D", "0H", "4H", "7C", "9S", "JH");
        addCardsToHand(currentPlayer, cardCodesPlayer1);
        Player playerNext = game.getPlayers().get(1);
        List<String> cardCodesPlayer2 = List.of("2D", "2H", "3H", "4C", "5S", "6H");
        addCardsToHand(playerNext, cardCodesPlayer2);
        playerNext.getHand().throwAwayHand();
        Player playerNextNext = game.getPlayers().get(2);
        List<String> cardCodesPlayer3 = List.of("2D", "0H", "4H", "7C", "9S", "JH");
        addCardsToHand(playerNextNext, cardCodesPlayer3);
        game.getCurrentRound().changeCurrentPlayer();
        log.info(String.valueOf(game.getCurrentRound().getCurrentPlayer().getColor()));
        assertEquals(game.getCurrentRound().getCurrentPlayer(), playerNextNext);
    }
    @Test
    public void testThrowAwayHand() {
        Game game = setupGame();
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        List<String> cardCodesPlayer = List.of("2D", "2H", "3H", "4C", "5S", "6H");
        addCardsToHand(currentPlayer, cardCodesPlayer);
        currentPlayer.getHand().throwAwayHand();
        assertTrue(currentPlayer.getHand().getHandDeck().isEmpty());
    }
    @Test
    public void testLayDownCard() {
        Game game = setupGame();
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        List<String> cardCodesPlayer = List.of("2D", "2H", "3H", "4C", "5S", "6H");
        addCardsToHand(currentPlayer, cardCodesPlayer);
        Card cardToDelete = new Card("2H");
        currentPlayer.layDownCard(cardToDelete);
        assertEquals(5, currentPlayer.getHand().getHandDeck().size());
    }


    @Test
    public void testEat_sameTargetField_marbleEaten() {
        // TODO Andrina


    }

    @Test
    public void testEat_jumpedOverAnotherMarble_marbleNotEaten() {
        // TODO Andrina
    }
    @Test
    public void test_updateRoundStats() {
        Game game = setupGame();
        game.getGameService().updateRoundStats(game);
        assertEquals(game.getCurrentCardAmountForRound(), 5);
        assertEquals(game.getStartPlayer(), game.getPlayers().get(1));
        assertEquals(game.getRoundNumber(), 2);

    }
    @Test
    public void testChangeColorMarbles_AfterEndTurn(){
        Game game = setupGame();
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        Player teamMate = currentPlayer.getTeamMate();
        List<Marble> currentPlayerMarble = currentPlayer.getMarbleList();
        for(Marble m: currentPlayerMarble){
            m.setFinish(Boolean.TRUE);
        }
        assertTrue(game.getGameService().checkPlayerIsFinished(currentPlayer));
        game.getGameService().endTurn(game);
        assertEquals(currentPlayer.getMarbleList(), teamMate.getMarbleList());
        assertTrue(currentPlayer.isFinished());
    }
    @Test
    public void testRoundIsFinished(){
        Game game = setupGame();
        for(Player p: game.getPlayers()){
            p.getHand().throwAwayHand();
        }
        assertTrue(game.getGameService().checkRoundIsFinished(game));
    }
    @Test
    public void testGetPlayableMarbles(){
        Game game = setupGame();
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        Card card = new Card("AH");
        Marble firstHomeMarble = game.getPlayingBoard().getFirstHomeMarble(currentPlayer.getColor(),FALSE);
        String moveName = "Go To Start";
        List<Marble> marbleList = game.getGameService().getPlayableMarbles(game, currentPlayer.getPlayerName(), card, moveName);
        assertEquals(marbleList.size(), 1);
        assertTrue(marbleList.contains(firstHomeMarble));

    }
    @Test
    public void testGetPossibleTargetFields(){
        Game game = setupGame();
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        Card card = new Card("AH");
        Marble firstHomeMarble = game.getPlayingBoard().getFirstHomeMarble(currentPlayer.getColor(),FALSE);
        String moveName = "Go To Start";
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = new ArrayList<>();
        List<String> targetFieldList = game.getGameService().getPossibleTargetFields(game, firstHomeMarble, moveName, card, marbleIdAndTargetFieldKeys);
        assertEquals(targetFieldList.size(), 1);
        assertTrue(targetFieldList.get(0).equals("16BLUE"));

    }
    @Test
    public void testMakeMove(){
        Game game = setupGame();
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();

        List<String> cardCodesPlayer = List.of("AH", "2H", "3H", "4C", "5S", "6H");
        addCardsToHand(currentPlayer, cardCodesPlayer);
        Marble firstHomeMarble = game.getPlayingBoard().getFirstHomeMarble(currentPlayer.getColor(),FALSE);
        String moveName = "Go To Start";
        ArrayList<MarbleIdAndTargetFieldKey> listToSendWith = new ArrayList<>();
        listToSendWith.add(new MarbleIdAndTargetFieldKey(firstHomeMarble.getMarbleId(), "16BLUE"));
        AtomicReference<ArrayList<MarbleIdAndTargetFieldKey>> marbleIdAndTargetFieldKeys = new AtomicReference<>(new ArrayList<>());
        Assertions.assertDoesNotThrow(() -> {
            marbleIdAndTargetFieldKeys.set(game.getGameService().makeMove(game, currentPlayer.getPlayerName(), cardCodesPlayer.get(0), moveName, listToSendWith));
        });
        assertEquals(marbleIdAndTargetFieldKeys.get().size(), 1);
        assertTrue(marbleIdAndTargetFieldKeys.get().get(0).getFieldKey().equals("16BLUE"));
        assertTrue(marbleIdAndTargetFieldKeys.get().get(0).getMarbleId() == firstHomeMarble.getMarbleId());
    }
    @Test
    public void test_checkHasCardThisMove(){
        Game game = setupGame();
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        Card card = new Card("AH");
        Marble firstHomeMarble = game.getPlayingBoard().getFirstHomeMarble(currentPlayer.getColor(),FALSE);
        String moveName = "Exchange";
        ArrayList<MarbleIdAndTargetFieldKey> listToSendWith = new ArrayList<>();
        listToSendWith.add(new MarbleIdAndTargetFieldKey(firstHomeMarble.getMarbleId(), "16BLUE"));
        Assertions.assertThrows(Exception.class, () -> {
            ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = game.getGameService().makeMove(game, currentPlayer.getPlayerName(), card.getCode(), moveName, listToSendWith);
        });
    }

    @Test
    public void test_checkIsYourTurn(){
        Game game = setupGame();
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        Card card = new Card("AH");
        Marble firstHomeMarble = game.getPlayingBoard().getFirstHomeMarble(currentPlayer.getColor(),FALSE);
        String moveName = "Go To Start";
        Player playerNext = game.getPlayers().get(1);
        ArrayList<MarbleIdAndTargetFieldKey> listToSendWith = new ArrayList<>();
        listToSendWith.add(new MarbleIdAndTargetFieldKey(firstHomeMarble.getMarbleId(), "16BLUE"));
        Assertions.assertThrows(Exception.class, () -> {
            ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = game.getGameService().makeMove(game, playerNext.getPlayerName(), card.getCode(), moveName, listToSendWith);
        });
    }

    @Test
    public void test_checkIsYourMarble(){
        Game game = setupGame();
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        Card card = new Card("AH");
        Player playerNext = game.getPlayers().get(1);
        Marble firstHomeMarble = game.getPlayingBoard().getFirstHomeMarble(playerNext.getColor(),FALSE);
        String moveName = "Go To Start";
        ArrayList<MarbleIdAndTargetFieldKey> listToSendWith = new ArrayList<>();
        listToSendWith.add(new MarbleIdAndTargetFieldKey(firstHomeMarble.getMarbleId(), "16BLUE"));
        Assertions.assertThrows(Exception.class, () -> {
            ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = game.getGameService().makeMove(game, currentPlayer.getPlayerName(), card.getCode(), moveName, listToSendWith);
        });
    }

    @Test
    public void test_checkIsCardInYourHand(){
        Game game = setupGame();
        Player currentPlayer = game.getCurrentRound().getCurrentPlayer();
        List<String> cardCodesPlayer = List.of("2H", "2H", "3H", "4C", "5S", "6H");
        addCardsToHand(currentPlayer, cardCodesPlayer);
        Card card = new Card("AH");
        Marble firstHomeMarble = game.getPlayingBoard().getFirstHomeMarble(currentPlayer.getColor(),FALSE);
        String moveName = "Go To Start";
        ArrayList<MarbleIdAndTargetFieldKey> listToSendWith = new ArrayList<>();
        listToSendWith.add(new MarbleIdAndTargetFieldKey(firstHomeMarble.getMarbleId(), "16BLUE"));
        Assertions.assertThrows(Exception.class, () -> {
            ArrayList<MarbleIdAndTargetFieldKey> marbleIdAndTargetFieldKeys = game.getGameService().makeMove(game, currentPlayer.getPlayerName(), card.getCode(), moveName, listToSendWith);
        });
    }

}
