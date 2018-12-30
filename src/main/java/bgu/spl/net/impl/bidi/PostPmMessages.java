package bgu.spl.net.impl.bidi;

public class PostPmMessages {

    private String username;
    private String message;
    private MessageType messageType;

    enum MessageType {
        POSTMESSAGE, PMMESSAGE;
    }

    public PostPmMessages(String username, String message, MessageType messageType) {
        this.message = message;
        this.username = username;
        this.messageType = messageType;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
