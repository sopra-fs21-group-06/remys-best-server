package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import java.util.List;

public class PossibleTargetFieldKeysListDTO {
    private List<String> possibleTargetFieldKeyList;

    public List<String> getPossibleTargetFieldKeyList() {
        return possibleTargetFieldKeyList;
    }

    public void setPossibleTargetFieldKeyList(List<String> possibleTargetFieldKeyList) {
        this.possibleTargetFieldKeyList = possibleTargetFieldKeyList;
    }
}
