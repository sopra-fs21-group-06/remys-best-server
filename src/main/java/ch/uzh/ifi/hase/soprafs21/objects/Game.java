package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.service.WebSocketService;
import ch.uzh.ifi.hase.soprafs21.websocket.dto.incoming.WaitingRoomEnterDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    /** should game have a Round ? such that we just instantiate a new variable of round that automatically is created with right number of cards etc?**/
    private List<Player> playerList = new ArrayList<Player>();
    private int nrCards=7;

    private String deckId;
    private Player startPlayer;
    private PlayingBoard playingBoard = new PlayingBoard();
    private final UUID gameID = UUID.randomUUID();
    private int roundCount = 0;
    private Round currentRound;
    private final WebSocketService webSocketService;

    /**can throw nullPointerException **/
    public Game(List<User> users, WebSocketService webSocketService){
        this.webSocketService = webSocketService;
        for(User user : users){
            playerList.add(userToPlayer(user));
        }
    }

    private void setDeckId(String deckid){
        this.deckId = deckid;
    }
    private String getDeckId(){
        return this.deckId;
    }
    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public int getRoundCount() {
        return roundCount;
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

    /** initializes Game, allows user to pick teamMates, Colors **/
    public void initializeGame(){

    }
    /*
    Thats a very informative comment. o.B.d.A. trivial.
     */
    public List<Player> updatePlayerColor(String playername, Color color){

        for(Player p: playerList){
            if(p.getPlayerName().equals(playername)){
                p.setColor(color);
                break;
            }
        }
        boolean allColorsAssigned = true;
        for(Player p: playerList){
            if (p.getColor() == null) {
                allColorsAssigned = false;
                break;
            }
        }

        if(allColorsAssigned) {
            WaitingRoomEnterDTO startGameObj = new WaitingRoomEnterDTO();
            startGameObj.setToken("Edouard ish de Geilst");
            String path = "/topic/game/%s/startGame";
                this.webSocketService.sendToTopic(String.format(path, gameID.toString()), startGameObj);
            }
        return playerList;
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
    public void addToRoundCount(){
        roundCount++;
    }
    public void changeNrCards(){
        if (nrCards == 2){
            nrCards = 7;
        } else {
            nrCards--;
        }
    }
}
