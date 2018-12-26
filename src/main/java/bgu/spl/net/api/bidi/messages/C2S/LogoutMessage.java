package bgu.spl.net.api.bidi.messages.C2S;

import bgu.spl.net.api.bidi.messages.Message;

public class LogoutMessage  extends Message {

    private String userName;
    private String password;

    public LogoutMessage(String userName, String password) {
        super(3);
        this.userName = userName;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }
}
