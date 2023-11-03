package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.api.bidi.Connections;

public class Follow extends Message{
    private boolean follow;
    private String username;
    private String sender;

    public Follow(boolean follow,String sender,String user){
        super((short)4);
        this.follow=follow;
        this.username=user;
        this.sender=sender;
    }

    public String getUsername() {
        return username;
    }

    public boolean isFollow() {
        return follow;
    }
    
    public String followOpCode() {
    	if(follow)
    		return "0";
    	else
    		return "1";
    }
    
    @Override
    public boolean checkIfValid(Manager m) {
        if(!m.getUsers().get(sender).isLoggedin())
            return false;
        else if(follow){
            if(m.getFollowers().get(username).contains(sender))
                return false;
            else if(m.getBlocked().get(sender).contains(username))
                return false;
            else if(m.getBlocked().get(username).contains(sender))
                return false;
           
        }
        else{
            if(!m.getFollowers().get(username).contains(sender))
                return false;
        }
        return true;
    }

    @Override
    public void execute(Manager m, Connections<Message> connections, BGSProtocol bgsProtocol) {
        if(follow){
        	
        	 m.getFollowers().get(username).add(sender);
             m.getUsers().get(username).followedByOther();
             m.getFollowing().get(sender).add(username);
             m.getUsers().get(sender).follow();
          
        }
        else{
            m.getFollowers().get(username).remove(sender);
            m.getUsers().get(username).unfollowedByOther();
            m.getFollowing().get(sender).remove(username);
            m.getUsers().get(sender).unfollow();
        }
    }

}
