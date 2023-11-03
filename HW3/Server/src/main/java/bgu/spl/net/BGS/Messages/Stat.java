package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.api.bidi.Connections;

import java.util.List;

public class Stat extends Message{

    //private String usernames;
    private String sender;
    private List<String>usernames;
    public Stat(String sender,List<String>names){
        super((short)8);
        usernames=names;
        this.sender=sender;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    @Override
    public boolean checkIfValid(Manager m) {
        if(!m.getUsers().containsKey(sender))
            return false;
        else if(!m.getUsers().get(sender).isLoggedin())
            return false;
        for(String name:usernames){
            if(!m.getUsers().containsKey(name))
                return false;
            if(m.getBlocked().get(sender).contains(name))
                return false;
            if(m.getBlocked().get(name).contains(sender))
                return false;
        }
        return true;
    }


    @Override
    public void execute(Manager m, Connections<Message> connections, BGSProtocol bgsProtocol) {

    }
}
