package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import ch.uzh.ifi.hase.soprafs21.objects.Player;

public class GameEndDTO {
    String won[];
    String lost[];
    String aborted;

    public String[] getWon() {
        return won;
    }

   public String getAborted() {
        return this.aborted;
   }

    public void setAborted(String aborted) {
        this.aborted = aborted;
    }
}
