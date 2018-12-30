package bgu.spl.net.api.bidi.messages.S2C;

import bgu.spl.net.api.bidi.messages.Message;

public class ErrorMessage  extends Message {
    int messageOpcode;
    public ErrorMessage(int messageOpcode) {
        super(11);
        this.messageOpcode=messageOpcode;
    }

    public int getMessageOpcode() {
        return messageOpcode;
    }
}
