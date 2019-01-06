package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.MessageEncoderDecoderImpl;
import bgu.spl.net.impl.bidi.AllPostPmMessages;
import bgu.spl.net.impl.bidi.AllUsers;
import bgu.spl.net.impl.bidi.CoolProtocol;

public class ReactorMain {
    public static void main(String[] args) {
        AllUsers allUsers = new AllUsers();
        AllPostPmMessages allPostPmMessages = new AllPostPmMessages();
        Reactor server = new Reactor(Integer.parseInt(args[1]),Integer.parseInt(args[0]),()-> new CoolProtocol(allUsers,allPostPmMessages),()->new MessageEncoderDecoderImpl());
        server.serve();
    }
}
