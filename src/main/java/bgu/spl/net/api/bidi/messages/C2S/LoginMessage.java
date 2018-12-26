package bgu.spl.net.api.bidi.messages.C2S;

import bgu.spl.net.api.bidi.messages.Message;

public class LoginMessage  extends Message {

    private int opCode;


    public LoginMessage(int opCode) {
        super(opCode);
    }
}
