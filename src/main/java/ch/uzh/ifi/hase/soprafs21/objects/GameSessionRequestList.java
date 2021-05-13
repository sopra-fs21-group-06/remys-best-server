package ch.uzh.ifi.hase.soprafs21.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameSessionRequestList {
    private final List<GameSessionRequest> gameSessionRequestList;

    public GameSessionRequestList(){
        this.gameSessionRequestList = new ArrayList<GameSessionRequest>();
    }

    public void addRequest(GameSessionRequest request){
        if(request!=null){gameSessionRequestList.add(request);};
    };

    public void removeRequest(GameSessionRequest request){
        if(request!=null){gameSessionRequestList.remove(request);}
    };

    public void clearByHostID(Long id) {
        try {
            for (GameSessionRequest request : gameSessionRequestList) {
                if (request.getHostID().equals(id)) {
                    gameSessionRequestList.remove(request);
                    clearByHostID(id);
                    break;
                }
            }
        }catch(NullPointerException e){
            System.out.println("Something went wrong in clearByHostID()");
        }
    }

    public void clearByUserAndGameSessionID(UUID gameSessionID, Long userID){
        try {
            for (GameSessionRequest request : gameSessionRequestList) {
                if (request.getGameSessionID().equals(gameSessionID) && request.getReceiverID().equals(userID)) {
                    gameSessionRequestList.remove(request);
                    clearByUserAndGameSessionID(gameSessionID, userID);
                    break;
                }
            }
        }catch(NullPointerException e){
            System.out.println("Something went wrong in clearByUserAndGameSessionID()");
        }
    };

    public List<GameSessionRequest> getRequestsByUserID(Long id){
        try {
            List<GameSessionRequest> list = new ArrayList<GameSessionRequest>();
            for (GameSessionRequest request : gameSessionRequestList) {
                if (request.getReceiverID().equals(id)) {
                    list.add(request);
                }
            }
            return list;
        }catch(NullPointerException e){
            System.out.println("Something went wrong in getRequestsByUSerID");
            return null;
        }
    }
};

