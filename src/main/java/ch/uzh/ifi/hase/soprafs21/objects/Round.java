package ch.uzh.ifi.hase.soprafs21.objects;

import java.util.List;

public class Round {
    private Game game;
    private int nrCards;
    private Player currentPlayer;
    private List<Player> players;
    private Player winner = null;
    private Deck deck;

    public Round(List<Player> players, Player startPlayer, int nrCards, Game game){
        this.game = game;
        this.players = players;
        this.currentPlayer = startPlayer;
        this.nrCards = nrCards;
        Deck deck = new Deck();
        this.deck = deck;

    }


    public void initializeRound(){
        if(deck.draw(1) == null){
            deck.shuffle();
        }
        for (Player p : players) {
            Hand hand = new Hand(deck.draw(nrCards));
            p.setHand(hand);
        }
    }






    public void setNrCards(int nrCards) {
        this.nrCards = nrCards;
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
