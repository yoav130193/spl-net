package bgu.spl.net.srv.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;

import java.util.function.Supplier;

public class ThreadPerClientServer extends BaseServer {

    public ThreadPerClientServer(
            int port,
            Supplier<BidiMessagingProtocol> protocolFactory,
            Supplier<MessageEncoderDecoder> encoderDecoderFactory) {

        super(port, protocolFactory, encoderDecoderFactory);
    }


    @Override
    protected void execute(BlockingConnectionHandler handler) {
        new Thread(handler).start();
    }

}