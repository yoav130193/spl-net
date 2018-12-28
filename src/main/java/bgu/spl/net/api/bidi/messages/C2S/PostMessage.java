package bgu.spl.net.api.bidi.messages.C2S;

import bgu.spl.net.api.bidi.messages.Message;

import java.util.List;

public class PostMessage extends Message {

    private String content;
    private List<String> extraUserNames;

    public PostMessage(String content, List<String> extraUserNames) {
        super(5);
        this.content = content;
        this.extraUserNames = extraUserNames;
    }

    public String getContent() {
        return content;
    }

    public List<String> getExtraUserNames() {
        return extraUserNames;
    }
}
