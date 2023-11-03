package main.java.bgu.spl.net.srv;

import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.BGS.Messages.LogStat;
import main.java.bgu.spl.net.BGS.Messages.Message;
import main.java.bgu.spl.net.BGS.Messages.Post;
import main.java.bgu.spl.net.BGS.Messages.Stat;
import main.java.bgu.spl.net.BGS.User;
import main.java.bgu.spl.net.api.MessageEncoderDecoder;
import main.java.bgu.spl.net.api.bidi.BidiMessagingProtocol;
import main.java.bgu.spl.net.api.bidi.Connections;

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
    private Integer connectionId;
    private Connections<T>connections;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, BidiMessagingProtocol<T> protocol,Connections<T>con,Integer id) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connections=con;
        this.connectionId=id;
        this.encdec.connectionId(id);
        protocol.start(connectionId, this.connections);
        connections.connect(connectionId,this);

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
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    @Override
    public void send(T msg) throws IOException {
        if(msg!=null){
            out.write(encdec.encode(msg));
            out.flush();
        }



    }
}
