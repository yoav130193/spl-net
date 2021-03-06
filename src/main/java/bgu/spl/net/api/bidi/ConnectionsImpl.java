package bgu.spl.net.api.bidi;

import bgu.spl.net.impl.BGSServer.ConnectionHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    Map<Integer, ConnectionHandler<T>> connectionHandlerMap;


    public ConnectionsImpl() {
        connectionHandlerMap = new ConcurrentHashMap<>();

    }


    @Override
    public boolean send(int connectionId, Object msg) {
        ConnectionHandler<T> connectionHandler = connectionHandlerMap.get(connectionId);
        if (connectionHandler != null) {
            connectionHandler.send((T) msg);
        }

        return false;
    }

    @Override
    public void broadcast(Object msg) {
        for (Map.Entry<Integer, ConnectionHandler<T>> entry : connectionHandlerMap.entrySet()) {
            if (entry.getValue() != null)
                entry.getValue().send((T) msg);
        }

    }

    @Override
    public void disconnect(int connectionId) {
      connectionHandlerMap.remove(connectionId);
    }

    public void connect(int connectionId, ConnectionHandler connectionHandler) {
        connectionHandlerMap.put(connectionId, connectionHandler);

    }

}
