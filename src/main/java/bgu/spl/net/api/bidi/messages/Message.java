package bgu.spl.net.api.bidi.messages;

public abstract class Message {

    protected  int opCode;
    public Message(int opCode) {
        this.opCode = opCode;
    }

    public int getOpCode() {
        return opCode;
    }
}
