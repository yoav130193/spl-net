package bgu.spl.net.api.bidi.messages.C2S;

import bgu.spl.net.api.bidi.messages.Message;

public class RegisterMessage extends Message {

    private String userName;
    private String password;

    public RegisterMessage(int opCode, String userName, String password) {
        super(opCode);
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
