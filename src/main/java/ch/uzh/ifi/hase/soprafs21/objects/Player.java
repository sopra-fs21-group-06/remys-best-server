package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Player {

    private Color color;
    private Hand hand;
    private final String playerName;
    private UserStatus status;
    private List<Marble> marbleList = new ArrayList<>();
    private Player teamMate;
    private boolean isReady;
    private String cardToChangeCode = null;

    public void layDownCard(Card c){
        hand.deleteCardFromHand(c);
    }
    public String getCardToChangeCode() {
        return cardToChangeCode;
    }

    public void setCardToChangeCode(String cardToChangeCode) {
        this.cardToChangeCode = cardToChangeCode;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public Player(String playerName) {
        this.playerName=playerName;
    }

    public Color getColor() {
        return color;
    }

    public Hand getHand() {
        return hand;
    }

    public List<Marble> getMarbleList() {
        return marbleList;
    }

    public Player getTeamMate() {
        return teamMate;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }


    public void setMarbleList(List<Marble> marbleList) {
        this.marbleList = marbleList;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setTeamMate(Player teamMate) {
        this.teamMate = teamMate;
    }
    public ArrayList<Marble> getMarblesAtHome(){
        ArrayList<Marble> marblesOnField = new ArrayList<>();
        for(Marble m: this.getMarbleList()){
            if (m.getHome()){
                marblesOnField.add(m);
            }
        }
        return marblesOnField;
    }
    //returns the marbles on the field (Not home and not in finish sector)
    public List<Marble> getMarblesOnField(){
        List<Marble> marblesOnField = new ArrayList<>();
        for(Marble m: this.getMarbleList()){
            if(!(m.getHome()) && !(m.getCurrentField() instanceof FinishField)){
                marblesOnField.add(m);
            }
        }
        return marblesOnField;
    }
    //returns the marbles on the field (Not home and not in finish sector and not on Start)
    public List<Marble> getmarblesOnFieldNotHomeNotOnStart(){
        List<Marble> marblesOnField = new ArrayList<>();
        for(Marble m: this.getMarbleList()){
            if(!(m.getHome()) && !(m.getCurrentField() instanceof FinishField) && !(m.getCurrentField() instanceof StartField)){
                marblesOnField.add(m);
            }
        }
        return marblesOnField;
    }

    //return marbles on Field and in finish sector but not finished. All Marbles who can still move
    public List<Marble> getMarblesOnFieldAndNotFinished(){
        List<Marble> marblesOnFieldAndFinished = new ArrayList<>();
        for(Marble m: this.getMarbleList()){
            if(!(m.getHome()) && !(m.getFinish())){
                marblesOnFieldAndFinished.add(m);
            }
        }
        return marblesOnFieldAndFinished;
    }
    //Marbles who are in Finish sector who cant move anymore
    public List<Marble> getMarblesInFinishFieldAndFinished(){
        List<Marble> MarblesInFinishFieldAndFinished = new ArrayList<>();
        for(Marble m: this.getMarbleList()){
            if (m.getFinish()){
                MarblesInFinishFieldAndFinished.add(m);
            }
        }
        return MarblesInFinishFieldAndFinished;
    }
}
