package bgu.spl.net.api.bidi.messages.C2S;

import bgu.spl.net.api.bidi.messages.Message;

public class StatMessage  extends Message {
    private String userName;
    public StatMessage(String userName) {
        super(8);
        this.userName=userName;
    }

    public String getUserName() {
        return userName;
    }
}
