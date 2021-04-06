package ch.uzh.ifi.hase.soprafs21.objects;

import java.util.List;
import java.util.UUID;

public class Game {
    private List<Player> playerList;
    private int nrCards;
    private Deck deck;
    private Player startPlayer;
    private PlayingBoard playingBoard;
    private UUID gameID;

    public Deck getDeck() {
        return deck;
    }

    public int getNrCards() {
        return nrCards;
    }

    public Player getStartPlayer() {
        return startPlayer;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public PlayingBoard getPlayingBoard() {
        return playingBoard;
    }

    public UUID getGameID() {
        return gameID;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }

    public void setNrCards(int nrCards) {
        this.nrCards = nrCards;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void setPlayingBoard(PlayingBoard playingBoard) {
        this.playingBoard = playingBoard;
    }

    public void setStartPlayer(Player startPlayer) {
        this.startPlayer = startPlayer;
    }

}
