package bgu.spl.net.api.bidi.messages.S2C;

import bgu.spl.net.api.bidi.messages.Message;

import java.util.List;

public class AckMessage extends Message {

    private int messageOpcode;
    private String userName;
    private List<String> userNameList;
    private int numOfPosts;
    private int numOfFollowers;
    private int numOfFollowing;


    public AckMessage(int messageOpcode, String userName, List<String> userNameList) {
        super(10);
        this.messageOpcode = messageOpcode;
        this.userName = userName;
        this.userNameList = userNameList;

    }


    public AckMessage(int messageOpcode, int numOfPosts, int numOfFollowers, int numOfFollowing) {
        super(10);
        this.messageOpcode = messageOpcode;
        this.numOfPosts = numOfPosts;
        this.numOfFollowing = numOfFollowing;
        this.numOfFollowers = numOfFollowers;
    }

    public AckMessage(int messageOpcode) {
        super(10);
        this.messageOpcode = messageOpcode;
    }

    private void createAck(int messageOpcode) {
        switch (messageOpcode) {
            case 5:

                break;
        }
    }

    public int getMessageOpcode() {
        return messageOpcode;
    }

    public String getUserName() {
        return userName;
    }

    public int getNumOfFollowers() {
        return numOfFollowers;
    }

    public int getNumOfFollowing() {
        return numOfFollowing;
    }

    public int getNumOfPosts() {
        return numOfPosts;
    }

    public List<String> getUserNameList() {
        return userNameList;
    }

}
