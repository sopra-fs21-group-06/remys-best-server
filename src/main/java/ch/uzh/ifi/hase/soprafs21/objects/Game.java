package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.entity.User;
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
<<<<<<< Updated upstream
    /** should game have a Round ? such that we just instantiate a new variable of round that automatically is created with right number of cards etc?**/
    private List<Player> playerList = new ArrayList<Player>();
    private int nrCards=7;
=======
    private List<Player> playerList = new ArrayList<Player>();
    private int nrCards=7;//don't know exactly what this variable is
>>>>>>> Stashed changes
    private Deck deck= new Deck();
    private Player startPlayer;
    private PlayingBoard playingBoard = new PlayingBoard();
    private final UUID gameID = UUID.randomUUID();

<<<<<<< Updated upstream
=======
    /**can throw nullPointerException **/
>>>>>>> Stashed changes
    public Game(List<User> users){
        for(User user : users){
            playerList.add(userToPlayer(user));
        }
    }
<<<<<<< Updated upstream
    public Player userToPlayer(User user){
        return new Player(user.getName());
    }
    public void initializeGame(){}
=======
    /** used for conversion of a user into a player entity **/
    public Player userToPlayer(User user){
        return new Player(user.getName());
    }

    /** initializes Game, allows user to pick teamMates, Colors **/
    public void initializeGame(){
>>>>>>> Stashed changes

    }
    public Deck getDeck() {
        return deck;
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

    public void setDeck(Deck deck) {
        this.deck = deck;
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
}
