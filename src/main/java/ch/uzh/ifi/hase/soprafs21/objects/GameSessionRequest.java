package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.RequestStatus;

import java.util.UUID;

public class GameSessionRequest{
    private UUID hostID;
    private UUID receiverID;
    private UUID gameSessionID;
    private RequestStatus requestStatus;

    public GameSessionRequest(UUID receiverID){
        requestStatus=RequestStatus.PENDING;
    }

    public UUID getGameSessionID() {return gameSessionID;
    }

    public UUID getHostID() {
        return hostID;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setGameSessionID(UUID gameSessionID) {
        this.gameSessionID = gameSessionID;
    }

    public UUID getReceiverID() {
        return receiverID;
    }

    public void setHostID(UUID hostID) {
        this.hostID = hostID;
    }

    public void setReceiverID(UUID receiverID) {
        this.receiverID = receiverID;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
