package bgu.spl.net.api.bidi.messages.C2S;

import bgu.spl.net.api.bidi.messages.Message;

public class PmMessage  extends Message {

    private String content;

    public PmMessage(String content) {
        super(6);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
