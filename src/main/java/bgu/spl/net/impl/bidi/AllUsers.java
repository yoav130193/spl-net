package bgu.spl.net.impl.bidi;

import java.util.ArrayList;
import java.util.List;

public class AllUsers {

    private List<User> allUserList;
    private List<User> loggedUsers;

    public AllUsers() {
        allUserList = new ArrayList<>();
        loggedUsers = new ArrayList<>();
    }


    public void addToList(User user) {
        allUserList.add(user);
    }

    public User getUser(String username) {
        for (int i = 0; i < allUserList.size(); i++) {
            if (allUserList.get(i).getUsername().equals(username)) return allUserList.get(i);
        }
        return null;
    }

    public List<User> getAllUserList() {
        return allUserList;
    }

    public List<User> getLoggedUsers() {
        return loggedUsers;
    }

    public int getAmountOfLoggedUsers() {
        return loggedUsers.size();
    }

    public boolean addUser(User user){
        allUserList.add(user);
        return true;
    }

    public User getUser(int connectionId) {

        for (int i = 0; i < allUserList.size(); i++) {
            if (allUserList.get(i).getConectionId() == connectionId) return allUserList.get(i);
        }
        return null;
    }

    public boolean logUser(User user) {
        loggedUsers.add(user);
        return true;
    }
}
