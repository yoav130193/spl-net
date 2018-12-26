package bgu.spl.net.api.bidi.messages.S2C;

import bgu.spl.net.api.bidi.messages.Message;

public class ErrorMessage  extends Message {
    public ErrorMessage(int opCode) {
        super(opCode);
    }
}
