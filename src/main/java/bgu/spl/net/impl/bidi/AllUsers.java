package bgu.spl.net.impl.bidi;

import java.util.ArrayList;
import java.util.List;

public class AllUsers {

    private List<User> allUserList;
    private List<User> loggedUsers;

    private static class SingletonHolder {
        private static AllUsers instance = new AllUsers();
    }

    private AllUsers() {
        allUserList = new ArrayList<>();
        loggedUsers = new ArrayList<>();
    }

    public static AllUsers getInstance() {
        return SingletonHolder.instance;
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

}
