package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.messages.C2S.*;
import bgu.spl.net.api.bidi.messages.Message;
import bgu.spl.net.api.bidi.messages.S2C.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CoolProtocol implements BidiMessagingProtocol<Message> {

    private boolean shouldTerminate = false;
    private int connectionId;
    private Connections<Message> connections;
    private AllUsers allUsers;
    private AllPostPmMessages allPostPmMessages;
    User me;

    public CoolProtocol(AllUsers allUsers, AllPostPmMessages allPostPmMessages) {
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

        switch (message.opCode) {

            case 1:
                RegisterMessage registerMessage = (RegisterMessage) message;
                User user = allUsers.getUser(registerMessage.getUserName());
                if (user != null) { // a user is already registered with this message
                    connections.send(connectionId, new ErrorMessage(1));
                } else { // new user !!! :)
                    if (allUsers.addUser(new User(connectionId, registerMessage.getUserName(), registerMessage.getPassword())))
                    {
                        System.out.println(registerMessage.getUserName() + " registered!");
                        connections.send(connectionId, new AckMessage(1));
                    }
                    else{
                        connections.send(connectionId, new ErrorMessage(1));
                    }
                }
                break;
            case 2:
                LoginMessage loginMessage = (LoginMessage) message;
                user = allUsers.getUser(loginMessage.getUserName());
                if (me != null || user == null || user.isLogged()) {
                    // user doesn't exist or logged!
                    connections.send(connectionId, new ErrorMessage(2));
                } else if (user.getPassword().equals(loginMessage.getPassword())) {
                    // log in the fucking user
                    if (allUsers.logUser(user)) {
                        user.setConnectionId(connectionId);
                        user.setLogged(true);
                        System.out.println(user.getUsername() + " logged in!");
                        connections.send(connectionId, new AckMessage(2));
                        me = user;
                        while (me.hasNotifications())
                            connections.send(connectionId, me.getNotification());
                    } else {
                        connections.send(connectionId, new ErrorMessage(2));
                    }
                } else {
                    connections.send(connectionId, new ErrorMessage(2));
                }
                break;
            case 3:
                //LogoutMessage logoutMessage = (LogoutMessage) message;
                if (me == null || !allUsers.getLoggedUsersMap().containsKey(me.getUsername())) {
                    //I am not logged in
                    connections.send(connectionId, new ErrorMessage(3));
                } else {
                    // I am logged
                    allUsers.getLoggedUsersMap().remove(me.getUsername());
                    me.setLogged(false);
                    System.out.println(me.getUsername() + " logged out!");
                    connections.send(connectionId, new AckMessage(3));
                    //shouldTerminate = true;
                }
                break;
            case 4:
                FollowMessage followMessage = (FollowMessage) message;
                List<String> usersForAck = new ArrayList<>();
                boolean toFollow = followMessage.isFollow();

                if (me == null || !allUsers.getLoggedUsersMap().containsKey(me.getUsername())) {
                    //I am not logged in
                    connections.send(connectionId, new ErrorMessage(4));
                } else {
                    // I am logged

                    for (int i = 0; i < followMessage.getUserNameList().size(); i++) {
                        String followUser = followMessage.getUserNameList().get(i);
                        if (followMessage.isFollow()) {
                            //try to follow people
                            if (!me.getFollowUserList().contains
                                    (followUser) && allUsers.getUser(followUser) != null) {
                                //not following already
                                usersForAck.add(followUser);
                                // add to my followList
                                me.getFollowUserList().add(followUser);
                                // add to his followedList
                                allUsers.getUser(followUser).getAreFollowedUserList().
                                        add(me.getUsername());
                                System.out.println(me.getUsername() + " now follows " + followUser);
                            }
                        } else {
                            // try to not follow people
                            if (me.getFollowUserList().contains(followUser)) {
                                // he is my followed list
                                usersForAck.add(followMessage.getUserNameList().get(i));
                                // remove from my follow list
                                me.getFollowUserList().remove(followUser);
                                // remove from his followedList
                                allUsers.getUser(followUser).
                                        getAreFollowedUserList().remove(me.getUsername());
                                System.out.println(me.getUsername() + " now unfollows " + followUser);

                            }
                        }
                    }
                    if (usersForAck.size() == 0) {
                        //nobody had changed
                        connections.send(connectionId, new ErrorMessage(4));
                    } else {
                        connections.send(connectionId, new AckMessage(4, usersForAck, usersForAck.size()));
                    }
                }


                break;
            case 5:
                PostMessage postMessage = (PostMessage) message;
                if (me == null || !allUsers.getLoggedUsersMap().containsKey(me.getUsername())) {
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
                    //check duplicate messages
                    List<String> recipientList = new ArrayList<>();

                    // add post to all my followers
                    List<String> myFollowers = me.getAreFollowedUserList();
                    for (int i = 0; i < myFollowers.size(); i++) {
                        if (allUsers.getUser(myFollowers.get(i)) != null) {
                            User sendUser = allUsers.getUser(myFollowers.get(i));

                            sendUser.getGotPostPmMessagesList().add(newPost);
                            NotificationMessage notification = new NotificationMessage(
                                    true,
                                    me.getUsername(),
                                    newPost.getMessage());
                            connections.send(sendUser.getConectionId(), notification);
                            recipientList.add(sendUser.getUsername());
                            if (!sendUser.isLogged())
                                sendUser.addAwaitingMessage(notification);
                            // if user has logged in after the islogged check reassure that he will recieve the notification
                            if (sendUser.isLogged()) {
                                while (sendUser.hasNotifications())
                                    connections.send(connectionId, sendUser.getNotification());
                            }
                        }
                    }

                    // add post to all specific users
                    LinkedList<String> specificUsers = (LinkedList<String>) postMessage.getExtraUserNames();
                    for (int i = 0; i < specificUsers.size(); i++) {
                        if (allUsers.getUser(specificUsers.get(i)) != null
                                && !recipientList.contains(specificUsers.get(i))) {
                            User specificUser = allUsers.getUser(specificUsers.get(i));
                            specificUser.getGotPostPmMessagesList().add(newPost);
                            NotificationMessage notification = new NotificationMessage(
                                    true,
                                    me.getUsername(),
                                    newPost.getMessage());
                            connections.send(specificUser.getConectionId(), notification);
                            if (!specificUser.isLogged())
                                specificUser.addAwaitingMessage(notification);
                            // if user has logged in after the islogged check reassure that he will recieve the notification
                            if (specificUser.isLogged()) {
                                while (specificUser.hasNotifications())
                                    connections.send(connectionId, specificUser.getNotification());
                            }
                        }
                    }

                    // add post to my sent message
                    me.getSentPostPmMessagesList().add(newPost);
                    System.out.println(me.getUsername() + " now posted: " + newPost.getMessage());
                    connections.send(connectionId, new AckMessage(5));

                }
                break;
            case 6:
                PmMessage pmMessage = (PmMessage) message;
                if (me == null || !allUsers.getLoggedUsersMap().containsKey(me.getUsername())) {
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
                        NotificationMessage notification = new NotificationMessage(false,
                                me.getUsername(),
                                newPM.getMessage());
                        connections.send(recipientUser.getConectionId(), notification
                        );
                        if (!recipientUser.isLogged())
                            recipientUser.addAwaitingMessage(notification);
                        // if user has logged in after the islogged check reassure that he will recieve the notification
                        if (recipientUser.isLogged()) {
                            while (recipientUser.hasNotifications())
                                connections.send(connectionId, recipientUser.getNotification());
                        }
                        // add post to my sent message
                        me.getSentPostPmMessagesList().add(newPM);
                        connections.send(connectionId, new AckMessage((6)));
                        System.out.println(me.getUsername() + " now PMed: " + newPM.getMessage());

                    }
                }
                break;
            case 7:
                UserListMessage userListMessage = (UserListMessage) message;
                if (me == null || !allUsers.getLoggedUsersMap().containsKey(me.getUsername())) {
                    //I am not logged in
                    connections.send(connectionId, new ErrorMessage(7));
                } else {
                    //I am logged in
                    connections.send(connectionId, new AckMessage(7,
                            allUsers.getAllUserStringList().size(), allUsers.getAllUserStringList()));
                    System.out.println(me.getUsername() + " asked for userlist!");

                }
                break;
            case 8:
                StatMessage statMessage = (StatMessage) message;

                if (me == null || !allUsers.getLoggedUsersMap().containsKey(me.getUsername())) {
                    //I am not logged in
                    connections.send(connectionId, new ErrorMessage(8));
                } else {
                    //I am logged in
                    if (allUsers.getUser(statMessage.getUserName()) == null) {
                        // userName not registered
                        connections.send(connectionId, new ErrorMessage(8));
                    }
                    else {
                            // userName is registered
                            User statUser = allUsers.getUser(statMessage.getUserName());
                            int countPost = 0;
                            for (int i = 0; i < statUser.getSentPostPmMessagesList().size(); i++) {
                                if (statUser.getSentPostPmMessagesList().get(i).getMessageType()
                                        == PostPmMessages.MessageType.POSTMESSAGE)
                                    countPost++;
                            }
                            connections.send(connectionId, new AckMessage(8,
                                    countPost
                                    , statUser.getAreFollowedUserList().size()
                                    , statUser.getFollowUserList().size()));
                            System.out.println(me.getUsername() + " asked for stats of " + statUser.getUsername());
                        }
                }

                break;
        }


    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
