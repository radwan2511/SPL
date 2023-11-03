package main.java.bgu.spl.net.BGS;


import main.java.bgu.spl.net.BGS.Messages.Message;
import main.java.bgu.spl.net.api.bidi.BidiMessagingProtocol;
import main.java.bgu.spl.net.api.bidi.Connections;
import main.java.bgu.spl.net.srv.ConnectionsImpl;

import java.io.IOException;

public class BGSProtocol implements BidiMessagingProtocol<Message> {
    private Connections<Message> connections;
    private Integer id;
    private Manager mangager;
    private boolean terminate;

    public BGSProtocol(Manager m){
        this.mangager=m;
    }
    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connections=(ConnectionsImpl)connections;
        //this.connections.connect(connectionId,);
        id=connectionId;
        terminate=false;
    }

    @Override
    public void process(Message message) throws IOException {
        connections.send(id,mangager.getResponse(message));
    	if(message.checkIfValid(mangager)) {
            message.execute(mangager,connections,this);
        }
        //return null;
    }

    //public void send(Message msg,)

    public void terminate(){
        terminate=true;
    }

    @Override
    public boolean shouldTerminate() {
        return terminate;
    }


}
