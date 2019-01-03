package bgu.spl.net.impl.bidi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AllUsers {

    private Map<String, User> allUserList;
    private Map<String, User> loggedUsersMap;

    public AllUsers() {
        allUserList = new ConcurrentHashMap<>();
        loggedUsersMap = new ConcurrentHashMap<>();
    }


    public void addToList(User user) {
        allUserList.put(user.getUsername(), user);
    }

    public User getUser(String username) {
        return allUserList.get(username);
        /*
        for (int i = 0; i < allUserList.size(); i++) {
            if (allUserList.get(username).getUsername().equals(username)) return allUserList.get(i);
        }
        return null;
        */
    }

    public Map<String, User> getAllUserList() {
        return allUserList;
    }

    public List<String> getAllUserStringList() {
        List<String> stringList = new ArrayList<>();
        for (Map.Entry entry : allUserList.entrySet()) {
            stringList.add((String) entry.getKey());
        }
        return stringList;
    }

    public Map<String, User> getLoggedUsersMap() {
        return loggedUsersMap;
    }

    public int getAmountOfLoggedUsers() {
        return loggedUsersMap.size();
    }

    public boolean addUser(User user) {
        allUserList.put(user.getUsername(), user);
        return true;
    }

    /*
    public User getUser(int connectionId) {

        for (int i = 0; i < allUserList.size(); i++) {
            if (allUserList.get().getConectionId() == connectionId) return allUserList.get(i);
        }
        return null;
    }
    */
    public boolean logUser(User user) {
        loggedUsersMap.put(user.getUsername(), user);
        return true;
    }
}
