package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.service.DeckService;
import ch.uzh.ifi.hase.soprafs21.service.HandService;
import ch.uzh.ifi.hase.soprafs21.service.PlayerService;
import ch.uzh.ifi.hase.soprafs21.service.PlayingBoardService;

import java.util.List;

import static java.util.Objects.isNull;

public class Round {
    private Game game;
    private int nrCards;
    private PlayingBoardService playingBoardService;
    private Player currentPlayer;
    private List<Player> players;
    private DeckService deckService;
    private Player winner = null;

    public Round(Deck deck, PlayingBoardService playingBoardService, List<Player> players, Player startPlayer, int nrCards){
        this.playingBoardService = playingBoardService;
        this.players = players;
        this.currentPlayer = startPlayer;
        this.nrCards = nrCards;
        DeckService deckService = new DeckService(deck);
        this.deckService = deckService;
        this.startRound();
    }

    public Player startRound() {
        for (Player p : players) {
            Hand hand = new Hand(deckService.draw(nrCards));
            p.setHand(hand);
        }

        while (isNull(winner) && nrCards != 0) {

            for (int i = 0; i < 5; i++) {
                // checker class first if any of the cards can be played otherwise lay down card

                if (currentPlayer.canPlay()) {
                    PlayerService playerService = new PlayerService(currentPlayer);
                    Marble m = playerService.chooseMarble();
                    Card c = playerService.chooseCard();
                    playingBoardService.moveMarble(m, c);
                    Hand h = currentPlayer.getHand();
                    HandService handService = new HandService(h);
                    handService.deleteCardFromHand(c);
                }
                if (currentPlayer.isWinning()) {
                    return winner = currentPlayer;
                }
                currentPlayer = players.get(XY);

            }
        }// change currentPlayer clockwise -> blue, green, red, yellow
                nrCards--;
        return winner;
    }

    public void setPlayingBoard(PlayingBoard playingBoard) {
        this.playingBoard = playingBoard;
    }

    public void setNrCards(int nrCards) {
        this.nrCards = nrCards;
    }

    public PlayingBoard getPlayingBoard() {
        return playingBoard;
    }

    public int getNrCards() {
        return nrCards;
    }

    public Game getGame() {
        return game;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
