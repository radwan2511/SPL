package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.api.bidi.Connections;

public class Block extends Message {
    private String username;
    private String sender;

    public Block(String sender,String user){
        super((short)12);
        this.username=user;
        this.sender=sender;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean checkIfValid(Manager m) {
        if(!m.getUsers().containsKey(username)){
            return false;
        }
        if(!m.getUsers().containsKey(sender)){
            return false;
        }
        if(!m.getUsers().get(sender).isLoggedin())
            return false;
        return true;
    }
    @Override
    public void execute(Manager m, Connections<Message> connections, BGSProtocol bgsProtocol) {
         m.getBlocked().get(sender).add(username);
         if(m.getFollowers().get(username).contains(sender)){
             m.getFollowers().get(username).remove(sender);
             m.getUsers().get(username).unfollowedByOther();
         }

        if(m.getFollowers().get(sender).contains(username)){
            m.getFollowers().get(sender).remove(username);
            m.getUsers().get(sender).unfollowedByOther();
        }

        if(m.getFollowing().get(sender).contains(username))
        {
            m.getFollowing().get(sender).remove(username);
            m.getUsers().get(sender).unfollow();
        }

        if(m.getFollowing().get(username).contains(sender)){
            m.getFollowing().get(username).remove(sender);
            m.getUsers().get(username).unfollow();
        }

    }
}
