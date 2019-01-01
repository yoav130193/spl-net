package bgu.spl.net.impl.bidi;

import java.util.ArrayList;
import java.util.List;

public class AllPostPmMessages {

    List<PostPmMessages> postPmMessagesList;

    public AllPostPmMessages() {
        postPmMessagesList = new ArrayList<>();
    }

    public List<PostPmMessages> getPostPmMessagesList() {
        return postPmMessagesList;
    }
}
