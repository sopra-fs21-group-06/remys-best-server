package ch.uzh.ifi.hase.soprafs21.objects;

public class Round {
    private Game game;
    private int nrCards;
    private PlayingBoard playingBoard;
    private Player currentPlayer;

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
