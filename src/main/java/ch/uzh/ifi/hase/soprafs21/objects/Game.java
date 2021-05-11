package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.MarbleExecuteCardDTO;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.ExecutePlayCardDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Game {
    /** should game have a Round ? such that we just instantiate a new variable of round that automatically is created with right number of cards etc?**/
    private List<Player> playerList = new ArrayList<Player>();
    private int nrCards=7;
    private boolean GameIsFinished = false;
    private String deckId;
    private Player startPlayer;
    private PlayingBoard playingBoard = new PlayingBoard();
    private final UUID gameId = UUID.randomUUID();
    private int roundCount = 0;
    private Round currentRound;
    private int cardCount = 54;
    private GameService gameService = GameService.getInstance();
    private final WebSocketService webSocketService;

    /**can throw nullPointerException **/
    public Game(List<User> users, WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
        for(User user : users){
            playerList.add(userToPlayer(user));
        }
        this.startPlayer = playerList.get(0);
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


    public int getNextCardAmount(){
        if (nrCards == 2){
           return 7;
        } else {
            return nrCards - 1;
        }
    }


    public Round getCurrentRound() {
        return currentRound;
    }

    /** used for conversion of a user into a player entity **/
    public Player userToPlayer(User user){
        return new Player(user.getUsername());
    }

    /*
    Thats a very informative comment. o.B.d.A. trivial.
     */
    public List<Player> updatePlayerColor(String playerName, Color color){
        for(Player p: playerList){
            if(p.getPlayerName().equals(playerName)){
                p.setColor(color);
                break;
            }
        }
        return playerList;
    }

    public void setCardExchange(String playerName, String cardToChangeCode){
        // TODO check if card is in player's name
        for(Player p: playerList){
            if(p.getPlayerName().equals(playerName)){
                p.setCardToChangeCode(cardToChangeCode);
                break;
            }
        }
        boolean allPlayerPerformedCardexchange = true;
        for(Player p: playerList){
            if (p.getCardToChangeCode() == null) {
                allPlayerPerformedCardexchange = false;
                break;
            }
        }
        if(allPlayerPerformedCardexchange) {

            for(Player p: playerList){
                if(p.getCardToChangeCode()!= null){
                    performCardExchange(p, p.getTeamMate());
                }
            }
            currentRound.sendOutCurrentTurnDTO();
            sendOutCurrentTurnFactsDTO();
        }
    }

    private void computeAndSetTeamMates() {
        for(Player p : playerList) {
            for(Player possibleTeamMate : playerList) {
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

        for(int i = 0; i < nrCards; i++){
            if(player1.getHand().getHandDeck().get(i).getCode().equals(player1CardCode)){
                player1Card = player1.getHand().getHandDeck().get(i);
                player1CardIdx = i;
                player1.getHand().getHandDeck().remove(i);
                break;
            }
        }

        for(int i = 0; i < nrCards; i++){
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
        currentRound.sendOutCardDifferenceHandDTO(player1, player2Card, player1CardIdx);
        assert player1Card != null;
        currentRound.sendOutCardDifferenceHandDTO(player2, player1Card, player2CardIdx);
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public void setPlayerToReady(String playername) {
        for(Player p: playerList){
            if(p.getPlayerName().equals(playername)){
                p.setReady(true);
                break;
            }
        }


        boolean areAllPlayersReady = true;
        for(Player p: playerList){
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

    public UUID getGameId() {
        return gameId;
    }

   /* public void setNrCards(int nrCards) {
        this.nrCards = nrCards;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void setPlayingBoard(PlayingBoard playingBoard) {
        this.playingBoard = playingBoard;
    }*/

    public void setStartPlayer(Player startPlayer) {
        this.startPlayer = startPlayer;
    }
    public void addToRoundCount(){
        roundCount++;
    }
    public void changeNrCards(){
        if (this.nrCards == 2){
            this.nrCards = 7;
        } else {
            this.nrCards--;
        }
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public void sendOutCurrentTurnFactsDTO(){
        webSocketService.sendCurrentTurnFactsMessage(roundCount, currentRound.getCurrentPlayer().getPlayerName(), getNextCardAmount(), currentRound.getNameNextPlayer(), gameId);
    }

    public void sendExecutedMove(ExecutePlayCardDTO executePlayCardDTO){
        String playerName = gameService.getUserService().convertTokenToUsername(executePlayCardDTO.getToken());
        String cardCode = executePlayCardDTO.getCode();
        String moveName = executePlayCardDTO.getMoveName();
        List<MarbleExecuteCardDTO> tupleList = executePlayCardDTO.getMarbles();
        ArrayList<MarbleIdAndTargetFieldKey> marbleIdsAndTargetFieldKeys = new ArrayList<>();

        try {
            for(MarbleExecuteCardDTO m: tupleList) {
                marbleIdsAndTargetFieldKeys.addAll(gameService.makeMove(playerName, cardCode, m.getTargetFieldKey(), m.getMarbleId(), moveName, this));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO send error via websocket and abort, websocketService.sendPrivateError() to be implemented
            // e.getMessage();
        }

        webSocketService.sendGameExecutedCard(playerName, cardCode, marbleIdsAndTargetFieldKeys, gameId);
    }

    public GameService getGameService() {
        return gameService;
    }

    public WebSocketService getWebSocketService() {
        return webSocketService;
    }
    public void changeCurrentPlayer () {
        if(playerList.size() == 2){
            for(Player p: playerList){
                if(!startPlayer.equals(p)){
                    startPlayer = p;
                    break;
                }
            }
        }
        for (Player p: playerList){
            if(p.getPlayerName().equals(getNameNextPlayer())){
                startPlayer = p;
                break;
            }
        }
    }
    public String getNameNextPlayer () {
        String name = "";
        if(startPlayer.getColor().equals(Color.BLUE)){
            for (Player p: playerList){
                if(p.getColor().equals(Color.GREEN)){
                    name = p.getPlayerName();
                }
            }
        } else if(startPlayer.getColor().equals(Color.GREEN)){
            for (Player p: playerList){
                if(p.getColor().equals(Color.RED)){
                    name = p.getPlayerName();
                }
            }
        } else if(startPlayer.getColor().equals(Color.RED)){
            for (Player p: playerList){
                if(p.getColor().equals(Color.YELLOW)){
                    name = p.getPlayerName();
                }
            }
        } else if(startPlayer.getColor().equals(Color.YELLOW)){
            for (Player p: playerList){
                if(p.getColor().equals(Color.BLUE)){
                    name = p.getPlayerName();
                }
            }
        }
        return name;

    }
}
