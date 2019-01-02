package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.messages.Message;
import bgu.spl.net.api.bidi.messages.S2C.NotificationMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class User {

    private int conectionId;
    private String username;
    private String password;
    private boolean logged;
    private List<String> followUserList; // who do I follow
    private List<String> areFollowedUserList; // who follow me
    private List<PostPmMessages> gotPostPmMessagesList; // post/pm that someone posted for you
    private List<PostPmMessages> sentPostPmMessagesList; // post/pm that you posted
    private BlockingQueue<NotificationMessage> notifications;

    public User(int conectionId, String username, String password) {
        this.conectionId = conectionId;
        this.username = username;
        this.password = password;
        this.logged = false;
        this.followUserList = new LinkedList<>();
        this.areFollowedUserList = new LinkedList<>();
        this.gotPostPmMessagesList = new LinkedList<>();
        this.sentPostPmMessagesList = new LinkedList<>();
        this.notifications = new LinkedBlockingQueue<>();
    }

    public java.lang.String getPassword() {
        return password;
    }

    public List<String> getFollowUserList() {
        return followUserList;
    }

    public java.lang.String getUsername() {
        return username;
    }

    public boolean checkPassword(java.lang.String password) {
        if (this.password.equals(password)) return true;
        return false;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isLogged() {
        return logged;
    }

    public List<String> getAreFollowedUserList() {
        return areFollowedUserList;
    }

    public List<PostPmMessages> getGotPostPmMessagesList() {
        return gotPostPmMessagesList;
    }

    public List<PostPmMessages> getSentPostPmMessagesList() {
        return sentPostPmMessagesList;
    }

    public int getConectionId() {
        return conectionId;
    }

    public void setConnectionId(int connectionId) {
        this.conectionId = connectionId;
    }

    public Message getNotification() {
       return notifications.poll();
    }

    public boolean hasNotifications() {
        return !notifications.isEmpty();
    }

    public void addAwaitingMessage(NotificationMessage notification) {
        try {
            this.notifications.put(notification);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
