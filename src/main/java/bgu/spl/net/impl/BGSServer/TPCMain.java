package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.MessageEncoderDecoderImpl;
import bgu.spl.net.impl.bidi.AllPostPmMessages;
import bgu.spl.net.impl.bidi.AllUsers;
import bgu.spl.net.impl.bidi.CoolProtocol;

public class TPCMain {
    public static void main(String[] args) {
        AllUsers allUsers = new AllUsers();
        AllPostPmMessages allPostPmMessages = new AllPostPmMessages();
        ThreadPerClientServer server = new ThreadPerClientServer(Integer.parseInt(args[0]),()-> new CoolProtocol(allUsers,allPostPmMessages),()->new MessageEncoderDecoderImpl());
        server.serve();
    }
}
