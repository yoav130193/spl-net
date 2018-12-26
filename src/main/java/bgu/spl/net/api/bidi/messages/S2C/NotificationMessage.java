package bgu.spl.net.api.bidi.messages.S2C;

import bgu.spl.net.api.bidi.messages.Message;

public class NotificationMessage extends Message {
    private char notificationType;
    private String postingUser;
    private String content;
    public NotificationMessage(char notificationType,String postingUser,String content) {
        super(9);
        this.notificationType = notificationType;
        this.postingUser = postingUser;
        this.content = content;
    }

    public char getNotificationType() {
        return notificationType;
    }

    public String getContent() {
        return content;
    }

    public String getPostingUser() {
        return postingUser;
    }
}
