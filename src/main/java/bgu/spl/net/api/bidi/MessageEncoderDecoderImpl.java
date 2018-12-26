package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class MessageEncoderDecoderImpl<Message> implements MessageEncoderDecoder<Message> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    @Override
    public Message decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == '\n') {
            return popMessage();
        }
        pushByte(nextByte);
        return null; //not a line yet
    }
    @Override
    public byte[] encode(Message message) {
        return (message + "\n").getBytes(); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private Message popMessage() {
        Message message = null;
        byte[] opCodeB = {bytes[0], bytes[1]};
        int opCode = ByteBuffer.wrap(opCodeB).getInt();
        switch (opCode) {
            case 0:
                break;
            case 1:
                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                break;

            case 6:
                break;

            case 7:
                break;

            case 8:
                break;

            case 9:
                break;

            case 10:
                break;
            case 11:
                break;
        }
    return message;
    }
}

