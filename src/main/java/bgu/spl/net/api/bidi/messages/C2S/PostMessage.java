package bgu.spl.net.api.bidi.messages.C2S;

import bgu.spl.net.api.bidi.messages.Message;

public class PostMessage extends Message {

    private String content;

    public PostMessage(String content) {
        super(5);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
