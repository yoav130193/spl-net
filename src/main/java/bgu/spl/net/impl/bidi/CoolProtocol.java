package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.messages.C2S.*;
import bgu.spl.net.api.bidi.messages.Message;
import bgu.spl.net.api.bidi.messages.S2C.*;

import java.util.ArrayList;
import java.util.List;

public class CoolProtocol implements BidiMessagingProtocol<Message> {

    private boolean shouldTerminate = false;
    private int connectionId;
    private Connections<Message> connections;
    private AllUsers allUsers;
    private AllPostPmMessages allPostPmMessages;

    public CoolProtocol(AllUsers allUsers,AllPostPmMessages allPostPmMessages) {
        this.allUsers = allUsers;
        this.allPostPmMessages = allPostPmMessages;

    }

    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(Message message) {
        //TODO- raz how the fuck to find out who am I
        // just a temporary user that we need to change
        User me = allUsers.getUser(connectionId);

        switch (message.opCode) {

            case 1:
                RegisterMessage registerMessage = (RegisterMessage) message;
                User user = allUsers.getUser(registerMessage.getUserName());
                if (user != null) { // a user is already registered with this message
                    connections.send(connectionId, new ErrorMessage(1));
                } else { // new user !!! :)
                    if(allUsers.addUser(new User(connectionId,registerMessage.getUserName(),registerMessage.getPassword())));
                     connections.send(connectionId, new AckMessage(1));
                }
                break;
            case 2:
                LoginMessage loginMessage = (LoginMessage) message;
                user = allUsers.getUser(loginMessage.getUserName());
                if (user == null || user.isLogged()) {
                    // user doesn't exist or logged!
                    connections.send(connectionId, new ErrorMessage(2));
                }
                else if (user.getPassword().equals(loginMessage.getPassword())){
                    user.setConnectionId(connectionId);
                    // log in the fucking user
                    if(allUsers.logUser(user))
                        connections.send(connectionId, new AckMessage(2));
                }
                break;
            case 3:
                //LogoutMessage logoutMessage = (LogoutMessage) message;
                if (!allUsers.getLoggedUsers().contains(me)) {
                    //I am not logged in
                    connections.send(connectionId, new ErrorMessage(3));
                } else {
                    // I am logged
                    allUsers.getLoggedUsers().remove(me);
                    connections.send(connectionId, new AckMessage(3));
                    shouldTerminate=true;
                }
                break;
            case 4:
                FollowMessage followMessage = (FollowMessage) message;
                List<String> usersForAck = new ArrayList<>();
                boolean toFollow = followMessage.isFollow();

                if (!allUsers.getLoggedUsers().contains(me)) {
                    //I am not logged in
                    connections.send(connectionId, new ErrorMessage(4));
                } else {
                    // I am logged

                    for (int i = 0; i < followMessage.getUserNameList().size(); i++) {
                        String followUser = followMessage.getUserNameList().get(i);
                        if (followMessage.isFollow()) {
                            //try to follow people
                            if (!me.getFollowUserList().contains
                                    (followUser)) {
                                //not following already
                                usersForAck.add(followUser);
                                // add to my followList
                                me.getFollowUserList().add(followUser);
                                // add to his followedList
                                allUsers.getUser(followUser).getAreFollowedUserList().
                                        add(me.getUsername());
                            }
                        } else {
                            // try to not follow people
                            if (me.getFollowUserList().contains(followUser)) {
                                // he is my followed list
                                usersForAck.add(followMessage.getUserNameList().get(i));
                                // remove from my follow list
                                me.getAreFollowedUserList().remove(followUser);
                                // remove from his followedList
                                allUsers.getUser(followUser).
                                        getAreFollowedUserList().remove(me.getUsername());


                            }
                        }
                    }
                }
                if (usersForAck.size() == 0) {
                    //nobody had changed
                    connections.send(connectionId, new ErrorMessage(4));
                } else {
                    connections.send(connectionId, new AckMessage(4, usersForAck, usersForAck.size()));
                }

                break;
            case 5:
                PostMessage postMessage = (PostMessage) message;
                if (!allUsers.getLoggedUsers().contains(me)) {
                    //I am not logged in
                    connections.send(connectionId, new ErrorMessage(5));
                } else {
                    //I am logged in
                    PostPmMessages newPost = new PostPmMessages(
                            me.getUsername(),
                            postMessage.getContent(),
                            PostPmMessages.MessageType.POSTMESSAGE);

                    //add post to the list of PostPMmessages
                    allPostPmMessages.getPostPmMessagesList()
                            .add(newPost);

                    // add post to all my followers
                    List<String> myFollowers = me.getAreFollowedUserList();
                    for (int i = 0; i < myFollowers.size(); i++) {
                        if (allUsers.getUser(myFollowers.get(i)) != null) {
                            User sendUser = allUsers.getUser(myFollowers.get(i));
                            sendUser.getGotPostPmMessagesList().add(newPost);
                            connections.send(sendUser.getConectionId(), new NotificationMessage(
                                    true,
                                    sendUser.getUsername(),
                                    newPost.getMessage()));
                        }
                    }

                    // add post to all specific users
                    List<String> specificUsers = postMessage.getExtraUserNames();
                    for (int i = 0; i < specificUsers.size(); i++) {
                        if (allUsers.getUser(specificUsers.get(i)) != null) {
                            User specificUser = allUsers.getUser(specificUsers.get(i));
                            specificUser.getGotPostPmMessagesList().add(newPost);
                            connections.send(specificUser.getConectionId(), new NotificationMessage(
                                    true,
                                    specificUser.getUsername(),
                                    newPost.getMessage()));
                        }
                    }

                    // add post to my sent message
                    me.getSentPostPmMessagesList().add(newPost);

                }
                break;
            case 6:
                PmMessage pmMessage = (PmMessage) message;
                if (!allUsers.getLoggedUsers().contains(me)) {
                    //I am not logged in
                    connections.send(connectionId, new ErrorMessage(6));
                } else {
                    // I am logged in
                    PostPmMessages newPM = new PostPmMessages(
                            me.getUsername(),
                            pmMessage.getContent(),
                            PostPmMessages.MessageType.PMMESSAGE);
                    if (allUsers.getUser(pmMessage.getUserName()) == null) {
                        //recipient user is not registered
                        connections.send(connectionId, new ErrorMessage(6));
                    } else {
                        //recipient user registered
                        // add post to the recipient user
                        User recipientUser = allUsers.getUser(pmMessage.getUserName());
                        recipientUser.getGotPostPmMessagesList().add(newPM);

                        //add post to the list of PostPMmessages
                        allPostPmMessages.getPostPmMessagesList()
                                .add(newPM);
                        connections.send(recipientUser.getConectionId(),
                                new NotificationMessage(false,
                                        recipientUser.getUsername(),
                                        newPM.getMessage()));
                        // add post to my sent message
                        me.getSentPostPmMessagesList().add(newPM);

                    }
                }
                break;
            case 7:
                UserListMessage userListMessage = (UserListMessage) message;
                if (!allUsers.getLoggedUsers().contains(me)) {
                    //I am not logged in
                    connections.send(connectionId, new ErrorMessage(7));
                } else {
                    //I am logged in
                    connections.send(connectionId, new AckMessage(7,
                            allUsers.getAllUserList().size(), allUsers.getAllUserList()));

                }
                break;
            case 8:
                StatMessage statMessage = (StatMessage) message;

                if (!allUsers.getLoggedUsers().contains(me)) {
                    //I am not logged in
                    connections.send(connectionId, new ErrorMessage(8));
                } else {
                    //I am logged in
                    if (allUsers.getUser(statMessage.getUserName()) == null) {
                        // userName not registered
                        connections.send(connectionId, new ErrorMessage(8));
                    } else {
                        // userName is registered
                        User statUser = allUsers.getUser(statMessage.getUserName());
                        connections.send(connectionId, new AckMessage(8,
                                statUser.getSentPostPmMessagesList().size()
                                , statUser.getAreFollowedUserList().size()
                                , statUser.getFollowUserList().size()));
                    }
                }

                break;
                /*
            case 9:
                NotificationMessage notificationMessage = (NotificationMessage) message;

                break;
            case 10:
                AckMessage ackMessage = (AckMessage) message;

                break;
            case 11:
                ErrorMessage errorMessage = (ErrorMessage) message;

                break;
                */
        }


    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
