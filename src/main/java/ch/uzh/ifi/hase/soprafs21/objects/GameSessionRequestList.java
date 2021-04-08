package ch.uzh.ifi.hase.soprafs21.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
/** needs a constructor**/
public class GameSessionRequestList {
    private List<GameSessionRequest> gameSessionRequestList;

    /**can throw nullPointerException **/
    public void addRequest(GameSessionRequest request){
        gameSessionRequestList.add(request);
    };
    /**can throw nullPointerException **/
    public void remove(GameSessionRequest request){
        gameSessionRequestList.remove(request);
    };

    /**can throw nullPointerException **/
    public void clearByHostID(UUID id) {
        for (GameSessionRequest request : gameSessionRequestList) {
            if (request.getHostID().equals(id)) {
                gameSessionRequestList.remove(request);
                clearByHostID(id);
                break;
            }
        }
    }

    /**can throw nullPointerException **/
    public void clearByUserAndGameSessionID(UUID gameSessionID, UUID userID){
        for(GameSessionRequest request: gameSessionRequestList){
            if(request.getGameSessionID().equals(gameSessionID)&&request.getReceiverID().equals(userID)){
                gameSessionRequestList.remove(request);
                clearByUserAndGameSessionID(gameSessionID,userID);
                break;
            }
        }
    };

    /**can throw nullPointerException **/
    public List<GameSessionRequest> getRequestsByUserID(UUID id){
<<<<<<< Updated upstream
        List<GameSessionRequest> list = new ArrayList<GameSessionRequest>();
        for (GameSessionRequest request : gameSessionRequestList) {
            if (request.getReceiverID().equals(id)) {
                list.add(request);
            }
        }
        return list;
    };
};

=======
            List<GameSessionRequest> list = new ArrayList<GameSessionRequest>();
            for (GameSessionRequest request : gameSessionRequestList) {
                if (request.getReceiverID().equals(id)) {
                    list.add(request);
                }
            }
            return list;
    };
};
>>>>>>> Stashed changes
