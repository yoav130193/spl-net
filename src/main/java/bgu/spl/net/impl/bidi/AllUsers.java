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
        if(allUserList.putIfAbsent(user.getUsername(),user)==null)
            return true;
//        synchronized (allUserList){
//          cd  if
//        }
//        allUserList.put(user.getUsername(), user);
        return false;
    }

    public boolean logUser(User user) {
        if(loggedUsersMap.putIfAbsent(user.getUsername(),user)==null)
            return true;

//       synchronized (loggedUsersMap){
//           if(loggedUsersMap.containsValue(user))
//               return false;
//           loggedUsersMap.put(user.getUsername(),user);
//           return true;
       return false;
    }
}
