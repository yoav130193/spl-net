package bgu.spl.net.api.bidi.messages.S2C;

import bgu.spl.net.api.bidi.messages.Message;

public class NotificationMessage extends Message {
    private boolean notificationType;
    private String postingUser;
    private String content;
    public NotificationMessage(boolean notificationType,String postingUser,String content) {
        super(9);
        this.notificationType = notificationType;
        this.postingUser = postingUser;
        this.content = content;
    }

    public boolean getNotificationType() {
        return notificationType;
    }

    public String getContent() {
        return content;
    }

    public String getPostingUser() {
        return postingUser;
    }
}
