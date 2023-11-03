package main.java.bgu.spl.net.srv;

import java.io.Closeable;
import java.io.IOException;

public interface ConnectionHandler<T> extends Closeable{

    void send(T msg) throws IOException;

}
