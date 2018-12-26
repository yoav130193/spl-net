package bgu.spl.net.api.bidi.messages.C2S;


import bgu.spl.net.api.bidi.messages.Message;

import java.util.List;

public class FollowMessage extends Message {

    private boolean follow;
    private int numOfUsers;
    private List<String> userNameList;

    public FollowMessage(boolean follow, int numOfUsers, List<String> userNameList) {
        super(4);
        this.follow = follow;
        this.numOfUsers = numOfUsers;
        this.userNameList = userNameList;
    }
}
