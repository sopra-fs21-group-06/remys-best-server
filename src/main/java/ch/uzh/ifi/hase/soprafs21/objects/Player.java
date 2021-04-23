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
    private List<Marble> marbleList;
    private Player teamMate;
    private Boolean canPlay;

    public Player(String playerName) {
        this.playerName = playerName;
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

    //FIRST CHECK IN GAMEPLAY IF ANY BLOCKS 
    public Boolean canPlay() {
        List<Marble> marblesOnBoard = null;
        int count = 0;
        for (Marble m : this.marbleList) {
            if (m.getHome() == TRUE) {
                count++;
            } else if(m.getFinish() == FALSE){
                marblesOnBoard.add(m);
            }
        }
        if (count == 4) {
            for (Card c : this.hand.getHandDeck()) {
                if (c.getCanStart() == TRUE) {
                    return TRUE;
                }
            }
            return FALSE;
        }
        if (count != 0){
            for (Card c : this.hand.getHandDeck()) {
                if (c.getCanStart() == TRUE) {
                    return TRUE;
                }
            }
        }
        for(Marble m : marblesOnBoard){
            int distance =  m.distanceToNextStartField();
            for (Card c : this.hand.getHandDeck()){
                for(Integer i: c.getCardMoveValue()) {
                    if (i < distance){
                        return  TRUE;
                    }
                }
            }
        }

        return FALSE;
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


}
