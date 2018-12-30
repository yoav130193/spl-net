package bgu.spl.net.impl.bidi;

import java.util.ArrayList;
import java.util.List;

public class AllPostPmMessages {

    List<PostPmMessages> postPmMessagesList;

    private static class SingletonHolder {
        private static AllPostPmMessages instance = new AllPostPmMessages();
    }

    private AllPostPmMessages() {
        postPmMessagesList = new ArrayList<>();
    }

    public static AllPostPmMessages getInstance() {
        return AllPostPmMessages.SingletonHolder.instance;
    }

    public List<PostPmMessages> getPostPmMessagesList() {
        return postPmMessagesList;
    }
}
