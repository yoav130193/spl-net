package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.messages.C2S.*;
import bgu.spl.net.api.bidi.messages.Message;
import bgu.spl.net.api.bidi.messages.S2C.AckMessage;
import bgu.spl.net.api.bidi.messages.S2C.ErrorMessage;
import bgu.spl.net.api.bidi.messages.S2C.NotificationMessage;


import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    /**
     *
     * @param nextByte the next byte to consider for the currently decoded
     * message
     * @return client to server message
     */
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

    /**
     *
     * @param message the message to encode
     * @return server to client message
     */
    @Override
    public byte[] encode(Message message) {
        byte[] messageB;
        byte[][] arguments = new byte[][]{};

        int opcode = message.getOpCode();
        byte[] opcodeB = new byte[2];
        opcodeB[0]=0;

        switch (opcode){
            case 9:{
                opcodeB[1]=9;
                NotificationMessage notificationMessage = (NotificationMessage) message;
                byte[] notificationType = new byte[]{0};
                if (notificationMessage.getNotificationType())
                    notificationType[0] = 1;
                byte[] postingUser = notificationMessage.getPostingUser().getBytes();
                byte[] content = notificationMessage.getContent().getBytes();
                arguments = new byte[][]{opcodeB,notificationType,postingUser,{0},content,{0}};
                break;
            }
            case 10:{
                opcodeB[1]=10;
                AckMessage ackMessage = (AckMessage) message;
                int messageOpcode = ackMessage.getMessageOpcode();
                byte[] messageOpcodeB = new byte[2];

                ByteBuffer.wrap(messageOpcodeB).putInt(messageOpcode);
                if(messageOpcode==4 || messageOpcode==7){
                        byte[] numOfUsers = new byte[2];
                        ByteBuffer.wrap(numOfUsers).putInt(ackMessage.getNumOfUsers());
                        List<String> userNameList = ackMessage.getUserNameListSring();
                        byte[][] userNameListB = new byte[userNameList.size()*2][];
                        for (int i = 0; i < userNameListB.length; i++) {
                            userNameListB[i] = userNameList.remove(i).getBytes();
                            userNameListB[++i] = new byte[]{0};
                        }
                         arguments= new byte[][]{opcodeB,messageOpcodeB,numOfUsers,merge(userNameListB)};
                }
                else if (messageOpcode==8){
                    byte[] numOfPosts = new byte[2];
                    ByteBuffer.wrap(numOfPosts).putInt(ackMessage.getNumOfPosts());
                    byte[] numOfFollowers = new byte[2];
                    ByteBuffer.wrap(numOfFollowers).putInt(ackMessage.getNumOfFollowers());
                    byte[] numOfFollowing = new byte[2];
                    ByteBuffer.wrap(numOfFollowing).putInt(ackMessage.getNumOfFollowing());
                    arguments = new byte[][]{opcodeB,messageOpcodeB,numOfPosts,numOfFollowers,numOfFollowing};
                }
                else{
                    arguments = new byte[][]{opcodeB,messageOpcodeB};
                }
                break;
            }
            case 11:{
                opcodeB[1]=11;
                ErrorMessage errorMessage = (ErrorMessage) message;
                int messageOpcode = errorMessage.getMessageOpcode();
                byte[] messageOpcodeB = new byte[2];
                ByteBuffer.wrap(messageOpcodeB).putInt(messageOpcode);
                arguments = new byte[][]{opcodeB,messageOpcodeB};
                break;
            }
            default:{
                return null;
            }
        }
        messageB = merge(arguments);
        return messageB;
    }

    private byte[] updateArray(byte[] result, byte[] toAdd) {
        int len = result.length;
        result = Arrays.copyOf(result,len+toAdd.length);
        System.arraycopy(toAdd,0,result,len,toAdd.length);
        return result;
    }

    private byte[] merge(byte[][] arguments){
        byte[] result = arguments[0];
        for (int i = 1; i < arguments.length ; i++) {
            result = updateArray(result,arguments[i]);
        }
        return result;
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
        byte opCode = opCodeB[1];
        switch (opCode) {
            case 1: {
                LinkedList<byte[]> arguments = findArguments(bytes, 2);
                String userName = getString(arguments.removeFirst());
                String password = getString(arguments.removeFirst());
                message = new RegisterMessage(userName, password);
                break;
            }
            case 2: {
                LinkedList<byte[]> arguments = findArguments(bytes, 2);
                String userName = getString(arguments.removeFirst());
                String password = getString(arguments.removeFirst());
                message = new LoginMessage(userName, password);
                break;
            }
            case 3: {
                message = new LogoutMessage();
                break;
            }
            case 4: {
                byte followB = bytes[2];
                byte[] numOfUsersB = Arrays.copyOfRange(bytes, 3, 5);
                LinkedList<byte[]> arguments = findArguments(bytes, 5);
                boolean follow = false;
                if (followB == 0)
                    follow = true;
                int numOfUsers = ByteBuffer.wrap(numOfUsersB).getInt();
                List<String> userNameList = new LinkedList<>();
                while (!arguments.isEmpty()) {
                    ((LinkedList<String>) userNameList).addLast(getString(arguments.removeFirst()));
                }
                message = new FollowMessage(follow, numOfUsers, userNameList);
                break;
            }

            case 5: {
                LinkedList<byte[]> arguments = findArguments(bytes, 2);
                String content = getString(arguments.removeFirst());
                List<String> extraUserNames = new LinkedList<>();
                for (int i = 0; i < content.length(); i++) {
                    if (content.charAt(i)=='@'){
                        i++;
                        String userName = "";
                        while(content.charAt(i)!=' ')
                            userName = userName + content.charAt(i);
                        ((LinkedList<String>) extraUserNames).addLast(userName);
                    }
                }
                message = new PostMessage(content,extraUserNames);
                break;
            }

            case 6: {
                LinkedList<byte[]> arguments = findArguments(bytes, 2);
                String userName = getString(arguments.removeFirst());
                String content = getString(arguments.removeFirst());
                message = new PmMessage(userName,content);
                break;
            }

            case 7: {
                message = new UserListMessage();
                break;
            }
            case 8: {
                LinkedList<byte[]> arguments = findArguments(bytes, 2);
                String userName = getString(arguments.removeFirst());
                message = new StatMessage(userName);
                break;
            }
        }
        return message;
    }

    private int findZero(byte[] array, int start) {
        for (int i = start; i < array.length; i++) {
            if (array[i] == 0)
                return i;
        }
        return array.length;
    }

    private LinkedList<byte[]> findArguments(byte[] array, int st) {
        LinkedList<byte[]> arguments = new LinkedList<>();
        int start;
        int stop = st - 1;
        while (stop != array.length) {
            start = stop + 1;
            stop = findZero(array, start);
            arguments.addLast(Arrays.copyOfRange(array, start, stop));
        }
        return arguments;
    }

    private String getString(byte[] array){
        return new String(array,0,array.length,StandardCharsets.UTF_8);
    }
}

