package bgu.spl.net.srv.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {


    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private final Connections<T> connections;
    private final int connectionId;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, BidiMessagingProtocol<T> protocol,
                                     Connections<T> connections, int connectionId) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connections = connections;
        this.connectionId = connectionId;
    }

    @Override
    public void send(T msg) {
        try {
            if (connected) {
                out.write(encdec.encode(msg));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    protocol.process(nextMessage);

                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
        in.close();
        out.close();
        connections.disconnect(connectionId);
    }

    public BidiMessagingProtocol<T> getProtocol() {
        return protocol;
    }
}
