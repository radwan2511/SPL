package main.java.bgu.spl.net.srv;

import main.java.bgu.spl.net.api.bidi.Connections;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    private ConcurrentHashMap<Integer,ConnectionHandler<T>> activeClients;
    //private Integer id;

    public ConnectionsImpl(){
        activeClients=new ConcurrentHashMap<>();
        //id=0;
    }
    @Override
    public boolean send(int connectionId, T msg) throws IOException {
        if(!activeClients.containsKey(connectionId)){
            return false;
        }
        else{
            activeClients.get(connectionId).send(msg);
            return true;
        }
    }

    @Override
    public void broadcast(T msg) throws IOException {
        for(Integer id: activeClients.keySet()){
            activeClients.get(id).send(msg);
        }

    }

    @Override
    public void disconnect(int connectionId) {
        if(activeClients.containsKey(connectionId))
            activeClients.remove(connectionId);

    }

    public void connect(int id,ConnectionHandler handler){
        activeClients.putIfAbsent(id,handler);
    }
}
