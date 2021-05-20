package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

public class Round {
    private final CardAPIService cardAPIService;
    private Game game;
    private int currentCardAmount;
    private Player currentPlayer;
    private List<Player> players;
    private Player winner = null;
    private final WebSocketService webSocketService;
    private final UserService userService;


    Logger log = LoggerFactory.getLogger(Round.class);
    public Round(Game game, WebSocketService webSocketService, UserService userService, CardAPIService cardAPIService){
        this.game = game;
        this.players = game.getPlayers();
        this.currentPlayer = game.getStartPlayer();
        this.currentCardAmount = game.getCurrentCardAmountForRound();
        this.cardAPIService = cardAPIService;
        this.webSocketService = webSocketService;
        this.userService = userService;
        initializeRound();
    }

    public void initializeRound() {
        for (Player p : players) {
            Hand hand = new Hand(cardAPIService.drawCards(this.currentCardAmount));
            p.setHand(hand);
            sendCardsToPlayer(p);
        }
    }

    public void changeCurrentPlayer() {
        for (Player p: players){
            if(p.getColor().equals(DogUtils.getNextColor(this.currentPlayer.getColor()))) {
                currentPlayer = p;
                if (!p.getHand().getHandDeck().isEmpty()) {
                    break;
                }
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

