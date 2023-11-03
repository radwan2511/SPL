package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.api.bidi.Connections;

public class Notification extends Message{
    private boolean type;
    private String postingUser;
    private String content;

    public Notification(boolean t,String user,String con){
        super((short)9);
        this.type=t;
        this.postingUser=user;
        this.content=con;
    }

    public String getContent() {
        return content;
    }


    public String getPostingUser() {
        return postingUser;
    }

    public boolean isType() {
        return type;
    }

    @Override
    public boolean checkIfValid(Manager m) {
        return true;
    }
    @Override
    public void execute(Manager m, Connections<Message> connections, BGSProtocol bgsProtocol) {
    	
    }

    @Override
    public String toString() {
        String ans="";
        if(type){
            ans+="Notification ";
            ans+="Public ";
            ans+=postingUser+" ";
            ans+=content+";";
        }
        else {
            ans+="Notification ";
            ans+="Private ";
            ans+=postingUser+" ";
            ans+=content+";";
        }
        return ans;
    }
}
