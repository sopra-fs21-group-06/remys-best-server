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
    private List<Player> players = new ArrayList<>();
    private int currentCardAmountForRound = 7;
    private boolean GameIsFinished = false;
    private String deckId;
    private Player startPlayer;
    private PlayingBoard playingBoard = new PlayingBoard();
    private final UUID gameId = UUID.randomUUID();
    private int roundNumber = 0;
    private Round currentRound;
    private GameService gameService = GameService.getInstance();
    private final WebSocketService webSocketService;
    private final CardAPIService cardAPIService = new CardAPIService();

    public Game(List<User> users, WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
        for(User user : users){
            players.add(userToPlayer(user));
        }
        this.startPlayer = players.get(0);
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    /*
    public boolean getGameIsFinished(){return GameIsFinished;}
    public void setGameIsFinished(boolean gameIsFinished) {
        GameIsFinished = gameIsFinished;
        if(gameIsFinished){
            GameEndDTO dto = new GameEndDTO();
            //TO-DO defining DTO will be done once Game, Player, PlayingBoard are finished
            webSocketService.sentGameEndMessage(this.gameId.toString(),dto);
        }
    }

    private void setDeckId(String deckid){
        this.deckId = deckid;
    }
    private String getDeckId(){
        return this.deckId;
    }
    public int getRoundCount() {
        return roundCount;
    }*/
    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public Round getCurrentRound() {
        return currentRound;
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

    public void setCardExchange(String playerName, String cardToChangeCode){
        // TODO ONLY one exchange per team!!
        for(Player p: players){
            if(p.getPlayerName().equals(playerName)){
                p.setCardToChangeCode(cardToChangeCode);
                break;
            }
        }
        boolean allPlayerPerformedCardexchange = true;
        for(Player p: players){
            if (p.getCardToChangeCode() == null) {
                allPlayerPerformedCardexchange = false;
                break;
            }
        }
        if(allPlayerPerformedCardexchange) {
            Player player1Team1 = null;
            Player player2Team1 = null;
            Player player1Team2 = null;
            Player player2Team2 = null;
            for(Player p: players){
                if(p.getColor().equals(Color.BLUE)) {
                    player1Team1 = p;
                } else if (p.getColor().equals((Color.RED))){
                    player2Team1 = p;
                } else if(p.getColor().equals(Color.GREEN)){
                    player1Team2 = p;
                } else {
                    player2Team2 = p;
                }
            }
            performCardExchange(player1Team1, player2Team1);
            performCardExchange(player1Team2, player2Team2);
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
        String player1CardCode = player1.getCardToChangeCode();
        String player2CardCode = player2.getCardToChangeCode();
        Card player1Card = null;
        Card player2Card = null;
        int player1CardIdx = 0;
        int player2CardIdx= 0;

        for(int i = 0; i < currentCardAmountForRound; i++){
            if(player1.getHand().getHandDeck().get(i).getCode().equals(player1CardCode)){
                player1Card = player1.getHand().getHandDeck().get(i);
                player1CardIdx = i;
                player1.getHand().getHandDeck().remove(i);
                break;
            }
        }

        for(int i = 0; i < currentCardAmountForRound; i++){
            if(player2.getHand().getHandDeck().get(i).getCode().equals(player2CardCode)){
                player2Card = player2.getHand().getHandDeck().get(i);
                player2CardIdx = i;
                player2.getHand().getHandDeck().remove(i);
                break;
            }
        }

        player1.getHand().getHandDeck().add(player1CardIdx, player2Card);
        player2.getHand().getHandDeck().add(player2CardIdx, player1Card);

        player1.setCardToChangeCode(null);
        player2.setCardToChangeCode(null);

        assert player2Card != null;
        currentRound.sendCardsToPlayer(player1, player2Card, player1CardIdx);
        assert player1Card != null;
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
            webSocketService.sendExchangeFactsMessage(getCurrentRound().getCurrentPlayer().getPlayerName(), gameId);
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
        if (this.currentCardAmountForRound == 2){
            return 7;
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
        return DogUtils.getNextPlayerName(this.startPlayer, this.players);
    }
}
