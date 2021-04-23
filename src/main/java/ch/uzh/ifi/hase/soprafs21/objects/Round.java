package ch.uzh.ifi.hase.soprafs21.objects;


import ch.uzh.ifi.hase.soprafs21.service.PlayingBoardService;

import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;


import java.util.List;

public class Round {
    private final CardAPIService cardAPIService;
    private Game game;
    private int nrCards;
    private PlayingBoardService playingBoardService;
    private Player currentPlayer;
    private List<Player> players;
    //private DeckService deckService;
    private Player winner = null;
    private String deckId;

    public Round(List < Player > players, Player startPlayer, int nrCards, Game game, CardAPIService cardAPIService){
                this.game = game;
                this.players = players;
                this.currentPlayer = startPlayer;
                this.nrCards = nrCards;
                this.cardAPIService = cardAPIService;
    }

    public void setDeckId (String deckId){
        this.deckId = deckId;
    }
    public void initializeRound () {
        if (cardAPIService.drawCards(deckId, "1") == null) {
            //cardAPIService.shuffle();
        }
        for (Player p : players) {
            String str = "" + nrCards;

            Hand hand = new Hand(cardAPIService.drawCards(deckId, str));

            p.setHand(hand);
            // send cards to
        }

    }

    public void changeCurrentPlayer () {
        int i = players.indexOf(currentPlayer);
        int inext = (i + 1) % 4;
        currentPlayer = players.get(inext);
    }
    public String getNameNextPlayer () {
        int i = players.indexOf(currentPlayer);
        int inext = (i + 1) % 4;
        return players.get(inext).getPlayerName();
    }


        public void setNrCards ( int nrCards){
            this.nrCards = nrCards;
        }



        public int getNrCards () {
            return nrCards;
        }

        public Game getGame () {
            return game;
        }

        public Player getCurrentPlayer () {
            return currentPlayer;
        }

        public void setCurrentPlayer (Player currentPlayer){
            this.currentPlayer = currentPlayer;
        }

        public void setGame (Game game){
            this.game = game;
        }
    }

