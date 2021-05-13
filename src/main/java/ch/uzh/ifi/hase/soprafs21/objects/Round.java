package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private final CardAPIService cardAPIService;
    private Game game;
    private int nrCards;
    private Player currentPlayer;
    private List<Player> players;
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
        this.deckId = cardAPIService.createDeck().getDeck_id();
        this.webSocketService = webSocketService;
        this.userService = userService;
        initializeRound();
    }

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
                sendCardsToPlayer(p);

                getGame().setCardCount(53 - (nrCards - getGame().getCardCount()));
            }

            else {
                String str = String.valueOf(nrCards);
                Hand hand = new Hand(cardAPIService.drawCards(deckId, str));
                p.setHand(hand);

                sendCardsToPlayer(p);

                getGame().setCardCount(getGame().getCardCount() - nrCards);

            }
        }
    }

    public void changeCurrentPlayer() {

        // TODO remove if 4 players
       if(players.size() == 2){
            for(Player p: players){
                if(!currentPlayer.equals(p)){
                    currentPlayer = p;
                    break;
                }
            }
        }
        for (Player p: players){
            if(p.getPlayerName().equals(getNextPlayerName())){
                currentPlayer = p;
                break;
            }
        }

        game.broadcastCurrentTurnAndUpdatedFacts();
    }

    public String getNextPlayerName() {
        String nextPlayerName = DogUtils.getNextPlayerName(this.currentPlayer, this.players);
        return nextPlayerName;
    }

    public Game getGame () {
        return game;
    }

    public Player getCurrentPlayer () {
        return currentPlayer;
    }

    public void sendCardsToPlayer(Player p) {
        webSocketService.sendCardsToPlayer(userService.getUserRepository().findByUsername(p.getPlayerName()).getSessionIdentity(), p.getHand().getHandDeck(), game.getGameId());
    }

    public void sendCardsToPlayer(Player p, Card c, int idx){
        List<Card> cardList = new ArrayList<>();
        cardList.add(c);
        webSocketService.sendCardsToPlayer(userService.getUserRepository().findByUsername(p.getPlayerName()).getSessionIdentity(), cardList, game.getGameId());
    }

    public void broadcastCurrentTurnMessage(){
        webSocketService.broadcastCurrentTurnMessage(currentPlayer.getPlayerName(), game.getGameId());
    }
}

