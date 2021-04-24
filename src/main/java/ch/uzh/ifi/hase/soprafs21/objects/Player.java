package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

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
    private boolean isReady;
    private String cardToChangeCode = null;

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
        this.canPlay = TRUE;
    }


    // TO DO CHECK 7 generally and if only on marble of me still there and JACK
    public Boolean canPlay (List<Integer> valuesBlocking) {
        List<Marble> marblesOnBoard = null;
        int count = 0;
        // get Marbles on board
        for (Marble m : this.marbleList) {
            if (m.getHome() == TRUE) {
                count++;
            }
            else if (m.getFinish() == FALSE) {
                marblesOnBoard.add(m);
            }
        }
        // check if all are at home, player needs CanStart Card
        if (count == 4) {
            for (Card c : this.hand.getHandDeck()) {
                if (c.getCanStart() == TRUE) {
                    return TRUE;
                }
            }
            return FALSE;
        }
        // if not all are on field, if player has start card, can play
        if (count != 0) {
            for (Card c : this.hand.getHandDeck()) {
                if (c.getCanStart() == TRUE) {
                    return TRUE;
                }
            }
        }
        // for the rest marbles on board, check first if nextSTartfield is blocking if yes (distance vs Cardvalue, if not -> all except jake
        for (Marble m : marblesOnBoard) {
            int nextStartFieldValue = m.nextStartFieldValue();
            int distance = 20 - (m.getCurrentField().getFieldValue() % 16);
            if (!(valuesBlocking.contains(nextStartFieldValue))) {
                for (Card c : this.hand.getHandDeck()) {
                    if (!(c.getCardMoveValue() == null)) {
                        return TRUE;
                    }
                }

            }
            else {
                for (Card c : this.hand.getHandDeck()) {
                    if (c instanceof CardJoker) {
                        return TRUE;
                    }
                    else if (!(c instanceof CardSeven)) {
                        for (Integer i : c.getCardMoveValue()) {
                            if (i < distance) {
                                return TRUE;
                            }
                        }
                    }

                }
            }
        }

    return  FALSE;

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
    public List<Marble> getMarblesAtHome(){
        List<Marble> marblesOnField = null;
        for(Marble m: this.getMarbleList()){
            if (m.getHome()){
                marblesOnField.add(m);
            }
        }
        return marblesOnField;
    }
    //returns the marbles on the field (Not home and not in finish sector)
    public List<Marble> getMarblesOnField(){
        List<Marble> marblesOnField = null;
        for(Marble m: this.getMarbleList()){
            if(!(m.getHome()) && !(m.getCurrentField() instanceof FinishField)){
                marblesOnField.add(m);
            }
        }
        return marblesOnField;
    }
    //return marbles on Field and in finish sector but not finished. All Marbles who can still move
    public List<Marble> getMarblesOnFieldAndNotFinished(){
        List<Marble> marblesOnFieldAndFinished = null;
        for(Marble m: this.getMarbleList()){
            if(!(m.getHome()) && !(m.getFinish())){
                marblesOnFieldAndFinished.add(m);
            }
        }
        return marblesOnFieldAndFinished;
    }
    //Marbles who are in Finish sector who cant move anymore
    public List<Marble> getMarblesInFinishFieldAndFinished(){
        List<Marble> MarblesInFinishFieldAndFinished = null;
        for(Marble m: this.getMarbleList()){
            if (m.getFinish()){
                MarblesInFinishFieldAndFinished.add(m);
            }
        }
        return MarblesInFinishFieldAndFinished;
    }




}
