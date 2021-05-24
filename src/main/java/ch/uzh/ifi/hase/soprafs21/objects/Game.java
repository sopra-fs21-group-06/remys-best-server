package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.service.CardAPIService;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.utils.DogUtils;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleExecuteCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.ExecutePlayCardDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    /** should game have a Round ? such that we just instantiate a new variable of round that automatically is created with right number of cards etc?**/
    private final List<Player> players = new ArrayList<>();
    private int currentCardAmountForRound = MAX_NUMBER_OF_CARDS;
    private static int MIN_NUMBER_OF_CARDS = 2;
    private static int MAX_NUMBER_OF_CARDS = 6;
    private Player startPlayer;
    private final PlayingBoard playingBoard = new PlayingBoard();
    private final UUID gameId = UUID.randomUUID();
    private int roundNumber = 0;
    private Round currentRound;
    private GameService gameService = GameService.getInstance();
    private final WebSocketService webSocketService;
    private final CardAPIService cardAPIService;

    public Game(List<User> users, WebSocketService webSocketService, CardAPIService cardAPIService) {
        this.webSocketService = webSocketService;
        this.cardAPIService = cardAPIService;
        for(User user : users){
            players.add(userToPlayer(user));
        }
        this.startPlayer = players.get(0);
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public int getRoundNumber(){
        return this.roundNumber;
    }

    /** used for conversion of a user into a player entity **/
    public Player userToPlayer(User user){
        return new Player(user.getUsername());
    }

    public List<Player> updatePlayerColor(String playerName, Color color){
        for(Player p: players){
            if(p.getPlayerName().equals(playerName)){
                p.setColor(color);
                break;
            }
        }
        return players;
    }

    public void setCardCodeToExchange(String playerName, String cardCodeToExchange){
        for(Player p: players) {
            if(p.getPlayerName().equals(playerName)){
                p.setCardCodeToExchange(cardCodeToExchange);
                break;
            }
        }

        boolean haveAllPlayersPerformedTheCardExchange = true;
        for(Player p: players){
            if (p.getCardCodeToExchange() == null) {
                haveAllPlayersPerformedTheCardExchange = false;
                break;
            }
        }

        if(haveAllPlayersPerformedTheCardExchange) {
            List<Player> playersWithTeamMate = new ArrayList<>();
            for(Player p: players) {
                if(!playersWithTeamMate.contains(p) && !playersWithTeamMate.contains(p.getTeamMate())) {
                    playersWithTeamMate.add(p);
                }
            }
            for(Player player: playersWithTeamMate) {
                performCardExchange(player, player.getTeamMate());
            }
            broadcastCurrentTurnAndUpdatedFacts();
        }
    }

    public void broadcastCurrentTurnAndUpdatedFacts() {
        currentRound.broadcastCurrentTurnMessage();
        broadcastFactsMessage();
    }

    private void computeAndSetTeamMates() {
        for(Player p : players) {
            for(Player possibleTeamMate : players) {
                if(p.getColor() == Color.BLUE && possibleTeamMate.getColor() == Color.RED) {
                    p.setTeamMate(possibleTeamMate);
                    possibleTeamMate.setTeamMate(p);
                    p.setMarbleList(playingBoard.getBlueMarbles());
                    possibleTeamMate.setMarbleList(playingBoard.getRedMarbles());
                }
                if(p.getColor() == Color.GREEN && possibleTeamMate.getColor() == Color.YELLOW) {
                    p.setTeamMate(possibleTeamMate);
                    possibleTeamMate.setTeamMate(p);
                    p.setMarbleList(playingBoard.getGreenMarbles());
                    possibleTeamMate.setMarbleList(playingBoard.getYellowMarbles());
                }
            }
        }
    }

    private void performCardExchange(Player player1, Player player2){
        String player1CardCode = player1.getCardCodeToExchange();
        String player2CardCode = player2.getCardCodeToExchange();
        Card player1Card = null;
        Card player2Card = null;
        int player1CardIdx = 0;
        int player2CardIdx= 0;

        for(int i = 0; i < currentCardAmountForRound; i++){
            if(player1.getHand().getHandDeck().get(i).getCode().equals(player1CardCode)){
                player1Card = player1.getHand().getHandDeck().get(i);
                player1CardIdx = i;
                break;
            }
        }

        for(int i = 0; i < currentCardAmountForRound; i++){
            if(player2.getHand().getHandDeck().get(i).getCode().equals(player2CardCode)){
                player2Card = player2.getHand().getHandDeck().get(i);
                player2CardIdx = i;
                break;
            }
        }

        player1.getHand().getHandDeck().remove(player1CardIdx);
        player2.getHand().getHandDeck().remove(player2CardIdx);

        player1.getHand().getHandDeck().add(player1CardIdx, player2Card);
        player2.getHand().getHandDeck().add(player2CardIdx, player1Card);

        player1.setCardCodeToExchange(null);
        player2.setCardCodeToExchange(null);

        currentRound.sendCardsToPlayer(player1, player2Card, player1CardIdx);
        currentRound.sendCardsToPlayer(player2, player1Card, player2CardIdx);
    }

    public void setPlayerToReady(String playerName) {
        for(Player p: players){
            if(p.getPlayerName().equals(playerName)){
                p.setReady(true);
                break;
            }
        }

        boolean areAllPlayersReady = true;
        for(Player p: players){
            if (!p.isReady()) {
                areAllPlayersReady = false;
                break;
            }
        }

        if(areAllPlayersReady) {
            computeAndSetTeamMates();
            gameService.initiateRound(this);
            webSocketService.broadcastExchangeFactsMessage(getCurrentRound().getCurrentPlayer().getPlayerName(), gameId);
        }
    }

    public int getCurrentCardAmountForRound() {
        return currentCardAmountForRound;
    }

    public Player getStartPlayer() {
        return startPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public PlayingBoard getPlayingBoard() {
        return playingBoard;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void incrementRoundNumber(){
        roundNumber++;
    }

    public void incrementCardAmountForRound() {
        this.currentCardAmountForRound = getNextCardAmount();
    }

    private int getNextCardAmount() {
        if (this.currentCardAmountForRound == MIN_NUMBER_OF_CARDS){
            return MAX_NUMBER_OF_CARDS;
        } else {
            return this.currentCardAmountForRound - 1;
        }
    }

    public void broadcastFactsMessage(){
        webSocketService.broadcastFactsMessage(roundNumber, currentRound.getCurrentPlayer().getPlayerName(), getNextCardAmount(), currentRound.getNextPlayerName(), gameId);
    }

    public void executeMove(ExecutePlayCardDTO executePlayCardDTO){
        String playerName = gameService.getUserService().convertTokenToUsername(executePlayCardDTO.getToken());
        String cardCode = executePlayCardDTO.getCode();
        String moveName = executePlayCardDTO.getMoveName();
        List<MarbleExecuteCardDTO> marbleExecuteCardDTO = executePlayCardDTO.getMarbles();

        try {
            ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKeysToExecute = DogUtils.generateMarbleIdsAndTargetFieldKeys(marbleExecuteCardDTO);
            gameService.makeMove(this, playerName, cardCode, moveName, marbleIdsAndTargetFieldKeysToExecute);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO send error via websocket and abort, websocketService.sendPrivateError() to be implemented
            // e.getMessage();
        }
    }

    public GameService getGameService() {
        return gameService;
    }

    public CardAPIService getCardAPIService() {
        return cardAPIService;
    }

    public WebSocketService getWebSocketService() {
        return webSocketService;
    }

    public void changeStartingPlayer() {
        for (Player p : players) {
            if (p.getPlayerName().equals(getNextPlayerName())) {
                startPlayer = p;
                break;
            }
        }
    }

    public String getNextPlayerName() {
        return DogUtils.getNextPlayer(this.startPlayer, this.players).getPlayerName();
    }
}
