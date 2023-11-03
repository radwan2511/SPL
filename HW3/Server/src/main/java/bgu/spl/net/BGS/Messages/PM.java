package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.api.bidi.Connections;

import java.io.IOException;

public class PM extends Message{
    private String username;
    private String content;
    private String sendingDateAndTime;
    private String sender;

    public PM(String user,String con,String DateAndTime,String sender){
        super((short)6);
        this.username=user;
        this.content=con;
        this.sendingDateAndTime=DateAndTime;
        this.sender=sender;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    public String getSendingDateAndTime() {
        return sendingDateAndTime;
    }

    @Override
    public boolean checkIfValid(Manager m) {
        if(!m.getUsers().containsKey(sender))
            return false;
        else if(!m.getUsers().get(sender).isLoggedin())
            return false;
        else if(!m.getUsers().containsKey(username))
            return false;
        else if(!m.getFollowers().get(username).contains(sender))
            return false;
        return true;
    }

    @Override
    public void execute(Manager m, Connections<Message> connections, BGSProtocol bgsProtocol) throws IOException {
        m.sendPM(this,connections);
    }
}
