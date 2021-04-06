package ch.uzh.ifi.hase.soprafs21.objects;

import java.util.List;

public class GameEngine {
    private List<GameSession> gameSessionList;
    private List<Game> runningGamesList;
    private List<GameSessionRequest> gameSessionRequestList;
    private WaitingRoom waitingRoom;

    public List<Game> getRunningGamesList() {
        return runningGamesList;
    }

    public List<GameSession> getGameSessionList() {
        return gameSessionList;
    }

    public List<GameSessionRequest> getGameSessionRequestList() {
        return gameSessionRequestList;
    }

    public WaitingRoom getWaitingRoom() {
        return waitingRoom;
    }

    public void setGameSessionList(List<GameSession> gameSessionList) {
        this.gameSessionList = gameSessionList;
    }

    public void setGameSessionRequestList(List<GameSessionRequest> gameSessionRequestList) {
        this.gameSessionRequestList = gameSessionRequestList;
    }

    public void setRunningGamesList(List<Game> runningGamesList) {
        this.runningGamesList = runningGamesList;
    }

    public void setWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
    }
}
