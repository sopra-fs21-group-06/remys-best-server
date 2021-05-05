package ch.uzh.ifi.hase.soprafs21.rest.dto.UserManagment;

public class UserLoginPostDTO {

    private String password;

    private String usernameOrEmail;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
}
