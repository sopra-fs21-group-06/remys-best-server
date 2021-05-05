package ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.outgoing;

public class FriendRequestCreatePostDTO {
    public String token;
    public String receiverName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
