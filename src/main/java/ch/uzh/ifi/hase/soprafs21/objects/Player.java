package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import java.util.List;

import static java.lang.Boolean.TRUE;

public class Player {
    private Color color;
    private Hand hand;
    private String playerName;
    private UserStatus status;
    private List<Marble> marbleList;
    private Player teamMate;
    private Boolean canPlay;

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
        this.canPlay = TRUE;
    }

    public Boolean canPlay() {

        return canPlay;
    }

    public void setMarbleList(List<Marble> marbleList) {
        this.marbleList = marbleList;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setTeamMate(Player teamMate) {
        this.teamMate = teamMate;
    }


}
