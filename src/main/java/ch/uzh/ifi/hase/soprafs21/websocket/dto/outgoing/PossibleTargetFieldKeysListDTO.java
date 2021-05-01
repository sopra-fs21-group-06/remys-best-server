package ch.uzh.ifi.hase.soprafs21.websocket.dto.outgoing;

import java.util.List;

public class PossibleTargetFieldKeysListDTO {
    private List<String> targetFieldKeys;

    public List<String> getTargetFieldKeys() {
        return targetFieldKeys;
    }

    public void setTargetFieldKeys(List<String> targetFieldKeys) {
        this.targetFieldKeys = targetFieldKeys;
    }
}
