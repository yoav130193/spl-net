package bgu.spl.net.impl.bidi;

import java.util.List;

public class User {

    private int conectionId;
    private String username;
    private String password;
    private boolean logged;
    private List<String> followUserList; // who do I follow
    private List<String> areFollowedUserList; // who follow me
    private List<PostPmMessages> gotPostPmMessagesList; // post/pm that someone posted for you
    private List<PostPmMessages> sentPostPmMessagesList; // post/pm that you posted


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
}
