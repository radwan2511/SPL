package main.java.bgu.spl.net.srv;


import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.api.MessageEncoderDecoder;
import main.java.bgu.spl.net.api.bidi.BidiMessagingProtocol;
import main.java.bgu.spl.net.api.bidi.Connections;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

public abstract class BaseServer<T> implements Server<T> {

    private final int port;
    private final Supplier<BidiMessagingProtocol<T>> protocolFactory;
    private final Supplier<MessageEncoderDecoder<T>> encdecFactory;
    private ServerSocket sock;
    private Connections<T>connections;
    private int connectionIds;


    public BaseServer(
            int port,
            Supplier<BidiMessagingProtocol<T>> protocolFactory,
            Supplier<MessageEncoderDecoder<T>> encdecFactory
            ) {

        this.port = port;
        this.protocolFactory = protocolFactory;
        this.encdecFactory = encdecFactory;
		this.sock = null;
        connectionIds=0;
        connections=new ConnectionsImpl<>();

    }

    @Override
    public void serve() {
    	
        //try (ServerSocket serverSock = new ServerSocket(port)) { Inet4Address.getLocalHost().getHostAddress().toString()
    	try (ServerSocket serverSock = new ServerSocket(port, 0, InetAddress.getLocalHost())) {
			System.out.println("Server started");

            this.sock = serverSock; //just to be able to close

            while (!Thread.currentThread().isInterrupted()) {

                Socket clientSock = serverSock.accept();

                BlockingConnectionHandler<T> handler = new BlockingConnectionHandler(
                        clientSock,
                        encdecFactory.get(),
                        protocolFactory.get(),
                        connections,
                        connectionIds);
                connectionIds++;

                execute(handler);
            }
        } catch (IOException ex) {
        }

        System.out.println("server closed!!!");
    }

    @Override
    public void close() throws IOException {
		if (sock != null)
			sock.close();
    }

    protected abstract void execute(BlockingConnectionHandler<T>  handler);

}
