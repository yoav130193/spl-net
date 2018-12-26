package bgu.spl.net.api.bidi.messages.S2C;

import bgu.spl.net.api.bidi.messages.Message;

public class AckMessage  extends Message {


    public AckMessage(int opCode) {
        super(opCode);
    }
}
