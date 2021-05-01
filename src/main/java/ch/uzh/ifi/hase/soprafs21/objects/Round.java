package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import java.util.ArrayList;
import java.util.List;

public class Round {
    private final CardAPIService cardAPIService;
    private Game game;
    private int nrCards;
    private Player currentPlayer;
    private List<Player> players;
    //private DeckService deckService;
    private Player winner = null;
    private String deckId;
    private final WebSocketService webSocketService;
    private final UserService userService;

    public Round(List<Player> players, Player startPlayer, int nrCards, Game game, CardAPIService cardAPIService, WebSocketService webSocketService, UserService userService){
        this.game = game;
        this.players = players;
        this.currentPlayer = startPlayer;
        this.nrCards = nrCards;
        this.cardAPIService = cardAPIService;
        deckId = cardAPIService.createDeck().getDeck_id();
        this.webSocketService = webSocketService;
        this.userService = userService;
        initializeRound();
    }
    /*
          public void setCurrentPlayer(Player currentPlayer){
            this.currentPlayer = currentPlayer;
        }

        public void setGame(Game game){
            this.game = game;
        }
    public void setDeckId (String deckId){
        this.deckId = deckId;
    }


        public void setNrCards ( int nrCards){
            this.nrCards = nrCards;
        }



        public int getNrCards () {
            return nrCards;
        }*/



    public void initializeRound () {

        for (Player p : players) {
            if (getGame().getCardCount() == 0) {
                cardAPIService.shuffle(deckId);
                getGame().setCardCount(53);
            }
            else if (getGame().getCardCount() < nrCards) {
                String firstDraw = String.valueOf(getGame().getCardCount());
                String secondDraw = String.valueOf(nrCards - getGame().getCardCount());

                //first draw
                Hand hand = new Hand(cardAPIService.drawCards(deckId, firstDraw));
                p.setHand(hand);

                cardAPIService.shuffle(deckId);

                //second draw
                p.getHand().addCardsToHand(cardAPIService.drawCards(deckId, secondDraw));
                sendOutCardToHandDTO(p);

                getGame().setCardCount(53 - (nrCards - getGame().getCardCount()));
            }

            else {
                String str = String.valueOf(nrCards);
                Hand hand = new Hand(cardAPIService.drawCards(deckId, str));
                p.setHand(hand);

                sendOutCardToHandDTO(p);

                getGame().setCardCount(getGame().getCardCount() - nrCards);

            }
        }
    }
    public String getNameNextPlayer () {
        String name = "";
        if(currentPlayer.getColor().equals(Color.BLUE)){
            for (Player p: players){
                if(p.getColor().equals(Color.GREEN)){
                    name = p.getPlayerName();
                }
            }
        } else if(currentPlayer.getColor().equals(Color.GREEN)){
            for (Player p: players){
                if(p.getColor().equals(Color.RED)){
                    name = p.getPlayerName();
                }
            }
        } else if(currentPlayer.getColor().equals(Color.RED)){
            for (Player p: players){
                if(p.getColor().equals(Color.YELLOW)){
                    name = p.getPlayerName();
                }
            }
        } else if(currentPlayer.getColor().equals(Color.YELLOW)){
            for (Player p: players){
                if(p.getColor().equals(Color.BLUE)){
                    name = p.getPlayerName();
                }
            }
        }
        return name;

    public void changeCurrentPlayer () {
       if(players.size() == 2){
            for(Player p: players){
                if(!currentPlayer.equals(p)){
                    currentPlayer = p;
                    break;
                }
            }
        }
        for (Player p: players){
            if(p.getPlayerName().equals(getNameNextPlayer())){
                currentPlayer = p;
                break;
            }
        }
    }
    public String getNameNextPlayer () {
        String name = "";
        if(currentPlayer.getColor().equals(Color.BLUE)){
            for (Player p: players){
                if(p.getColor().equals(Color.GREEN)){
                    name = p.getPlayerName();
                }
            }
        } else if(currentPlayer.getColor().equals(Color.GREEN)){
            for (Player p: players){
                if(p.getColor().equals(Color.RED)){
                    name = p.getPlayerName();
                }
            }
        } else if(currentPlayer.getColor().equals(Color.RED)){
            for (Player p: players){
                if(p.getColor().equals(Color.YELLOW)){
                    name = p.getPlayerName();
                }
            }
        } else if(currentPlayer.getColor().equals(Color.YELLOW)){
            for (Player p: players){
                if(p.getColor().equals(Color.BLUE)){
                    name = p.getPlayerName();
                }
            }
        }
        return name;

    }


        public Game getGame () {
            return game;
        }

        public Player getCurrentPlayer () {
            return currentPlayer;
        }



        public void sendOutCardToHandDTO(Player p) {
            webSocketService.sendCardsToPlayer(userService.getUserRepository().findByUsername(p.getPlayerName()).getSessionIdentity(), p.getHand().getHandDeck(), game.getGameId());
        }

        public void sendOutCardDifferenceHandDTO(Player p, Card c, int idx){
            List<Card> cardList = new ArrayList<>();
            cardList.add(c);
            webSocketService.sendCardsToPlayer(userService.getUserRepository().findByUsername(p.getPlayerName()).getSessionIdentity(), cardList, game.getGameId());
        }

        public void sendOutCurrentTurnDTO(){
            webSocketService.sendCurrentTurnMessage(currentPlayer.getPlayerName(), game.getGameId());
        }
    }

