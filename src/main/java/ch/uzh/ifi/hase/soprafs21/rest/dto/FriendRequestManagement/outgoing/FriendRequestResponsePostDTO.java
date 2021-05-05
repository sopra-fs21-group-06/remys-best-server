package ch.uzh.ifi.hase.soprafs21.rest.dto.FriendRequestManagement.outgoing;

public class FriendRequestResponsePostDTO {
    private String token;
    private String senderName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
