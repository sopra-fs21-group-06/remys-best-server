package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.websocket.dto.ChooseColorPlayerDTO;

import java.util.List;
import java.util.UUID;

public class WaitingRoomChooseColorDTO {
    private UUID gameId;
    private List<ChooseColorPlayerDTO> players;
    private boolean startGame;

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public List<ChooseColorPlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<ChooseColorPlayerDTO> players) {
        this.players = players;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }
}
