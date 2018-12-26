package bgu.spl.net.api.bidi.messages.S2C;

import bgu.spl.net.api.bidi.messages.Message;

public class ErrorMessage  extends Message {
    char messageOpcode;
    public ErrorMessage(char messageOpcode) {
        super(11);
        this.messageOpcode=messageOpcode;
    }

    public char getMessageOpcode() {
        return messageOpcode;
    }
}
