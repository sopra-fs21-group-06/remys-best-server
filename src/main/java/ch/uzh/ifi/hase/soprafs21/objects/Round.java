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
    private int currentCardAmount;
    private Player currentPlayer;
    private List<Player> players;
    private Player winner = null;
    private final WebSocketService webSocketService;
    private final UserService userService;

    public Round(Game game, CardAPIService cardAPIService, WebSocketService webSocketService, UserService userService){
        this.game = game;
        this.players = game.getPlayers();
        this.currentPlayer = game.getStartPlayer();
        this.currentCardAmount = game.getCurrentCardAmountForRound();
        this.cardAPIService = cardAPIService;
        this.webSocketService = webSocketService;
        this.userService = userService;
        initializeRound();
    }

    public void initializeRound () {
        for (Player p : players) {
            Hand hand = new Hand(cardAPIService.drawCards(this.currentCardAmount));
            p.setHand(hand);
            sendCardsToPlayer(p);
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
        return DogUtils.getNextPlayerName(this.currentPlayer, this.players);
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

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}

