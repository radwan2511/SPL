package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.BGS.User;
import main.java.bgu.spl.net.api.bidi.Connections;

import java.util.LinkedList;
import java.util.List;

public class LogStat extends Message{

    private String sender;
    //private List<User>users;
    public LogStat(String sender){
        super((short)7);
        this.sender=sender;
       // users=new LinkedList<>();
    }

    public List<User> createLogStat(Manager m){
        List<User> ans=new LinkedList<>();
        for(User u:m.getUsers().values()){
            if(u.isLoggedin()){
                if(!m.getBlocked().get(sender).contains(u.getUsername()) &&
                        !m.getBlocked().get(u.getUsername()).contains(sender))
                    ans.add(u);
            }
        }
        return ans;
    }

    @Override
    public boolean checkIfValid(Manager m) {
        if(!m.getUsers().containsKey(sender))
            return false;
        else if(!m.getUsers().get(sender).isLoggedin())
            return false;
        return true;
    }

    @Override
    public void execute(Manager m, Connections<Message> connections, BGSProtocol bgsProtocol) {

    }

    public String getSender() {
        return sender;
    }
}
