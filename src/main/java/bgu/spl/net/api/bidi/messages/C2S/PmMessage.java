package bgu.spl.net.api.bidi.messages.C2S;

import bgu.spl.net.api.bidi.messages.Message;

public class PmMessage extends Message {

    private String userName;
    private String content;

    public PmMessage(String userName,String content) {
        super(6);
        this.userName = userName;
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }
}
