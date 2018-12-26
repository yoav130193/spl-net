package bgu.spl.net.srv.bidi;

import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;

public class ServerRunner {
    public static void main(String[] args) {
        Reactor server = new Reactor(10,7777,()-> new EchoProtocol(),()->new LineMessageEncoderDecoder());
        //BaseServer server = new ThreadPerClientServer(7777,()-> new EchoProtocol(),()->new LineMessageEncoderDecoder());
        server.serve();
    }
}
