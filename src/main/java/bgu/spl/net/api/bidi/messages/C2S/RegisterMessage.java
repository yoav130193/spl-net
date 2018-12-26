package bgu.spl.net.api.bidi.messages.C2S;

import bgu.spl.net.api.bidi.messages.Message;

public class RegisterMessage extends Message {

    private String userName;
    private String password;

    public RegisterMessage(String userName, String password) {
        super(1);
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
