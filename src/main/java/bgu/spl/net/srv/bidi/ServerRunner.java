package bgu.spl.net.srv.bidi;

import bgu.spl.net.api.bidi.MessageEncoderDecoderImpl;
import bgu.spl.net.impl.bidi.AllPostPmMessages;
import bgu.spl.net.impl.bidi.AllUsers;
import bgu.spl.net.impl.bidi.CoolProtocol;

public class ServerRunner {
    public static void main(String[] args) {
        AllUsers allUsers = new AllUsers();
        AllPostPmMessages allPostPmMessages = new AllPostPmMessages();
        Reactor server = new Reactor(10,7777,()-> new CoolProtocol(allUsers,allPostPmMessages),()->new MessageEncoderDecoderImpl());
        //BaseServer server = new ThreadPerClientServer(7777,()-> new EchoProtocol(),()->new LineMessageEncoderDecoder());
        server.serve();
    }
}
