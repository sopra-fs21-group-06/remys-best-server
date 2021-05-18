package ch.uzh.ifi.hase.soprafs21.objects;

import ch.uzh.ifi.hase.soprafs21.constant.RequestStatus;

import java.util.UUID;

public class GameSessionRequest{
    private Long hostID;
    private Long receiverID;
    private UUID gameSessionID;
    private RequestStatus requestStatus;

    public GameSessionRequest(UUID receiverID){
        requestStatus=RequestStatus.PENDING;
    }

    public UUID getGameSessionID() {return gameSessionID;
    }

    public Long getHostID() {
        return hostID;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setGameSessionID(UUID gameSessionID) {
        this.gameSessionID = gameSessionID;
    }

    public Long getReceiverID() {
        return receiverID;
    }

    public void setHostID(Long hostID) {
        this.hostID = hostID;
    }

    public void setReceiverID(Long receiverID) {
        this.receiverID = receiverID;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
