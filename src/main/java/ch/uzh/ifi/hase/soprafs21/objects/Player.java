package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.FieldStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.controller.WSGameController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private String cardCodeToExchange = null;
    private boolean isFinished;
    Logger log = LoggerFactory.getLogger(Player.class);
    public Player(String playerName) {
        this.playerName = playerName;
        this.setFinished(FALSE);
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void layDownCard(Card c){
        hand.deleteCardFromHand(c);
    }

    public String getCardCodeToExchange() {
        return cardCodeToExchange;
    }

    public void setCardCodeToExchange(String cardToChangeCode) {
        this.cardCodeToExchange = cardToChangeCode;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
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

    //returns the marbles on the field (Not home and not in finish sector and not on Start)
    public List<Marble> getMarblesOnFieldNotHomeNotOnStart(){
        List<Marble> marblesOnField = new ArrayList<>();
        for(Marble m: this.getMarbleList()){
            if(!m.getHome() && !(m.getCurrentField() instanceof FinishField) && !m.getCurrentField().getFieldStatus().equals(FieldStatus.BLOCKED)){
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
    public int getNrMarbleAtHome(){
        List<Marble> marblesOnFieldAndFinished = new ArrayList<>();
        for(Marble m: this.getMarbleList()){
            if(m.getHome()){
                marblesOnFieldAndFinished.add(m);
            }
        }
        return marblesOnFieldAndFinished.size();
    }
    public int getNrMarblesFinished(){
        List<Marble> marblesOnFieldAndFinished = new ArrayList<>();
        for(Marble m: this.getMarbleList()){
            if(m.getFinish()){
                marblesOnFieldAndFinished.add(m);
            }
        }
        return marblesOnFieldAndFinished.size();
    }
    public boolean canFinishWithSeven(Game game){
        if(getNrMarbleAtHome() != 0){
            return FALSE;
        }
        int distance = 0;
        for(Marble m: this.getMarblesOnFieldAndNotFinished()){
            if(!(m.getCurrentField().getColor().equals(m.getColor()))) {
                return FALSE;
            } else {
                if (!(m.getCurrentField() instanceof FinishField)) {
                    distance += 16 - m.getCurrentField().getFieldValue();
                }
                distance += game.getPlayingBoard().nrStepsToNextFreeFinishSpot(m.getCurrentField());

                distance = distance - getMarblesOnFieldAndNotFinished().indexOf(m);
                log.info("canFInishiwithsven marbleNr: " + String.valueOf(m.getMarbleId()) + "distanceAll :" + String.valueOf(distance));
            }
        }
        if(distance <= 7){
            log.info("canFInishiwithsven TRUE ");
            return TRUE;
        }
        return FALSE;
    }


}
