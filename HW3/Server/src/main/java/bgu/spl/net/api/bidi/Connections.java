package main.java.bgu.spl.net.api.bidi;

import main.java.bgu.spl.net.srv.ConnectionHandler;

import java.io.IOException;

public interface Connections<T> {

    boolean send(int connectionId, T msg) throws IOException;

    void broadcast(T msg) throws IOException;

    void disconnect(int connectionId);

    void connect(int id, ConnectionHandler handler);
}
