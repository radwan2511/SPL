package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.api.bidi.Connections;

public class Logout extends Message {

	private String username;
	
    public Logout(String username){
        super((short)3);
        this.username = username;
    }

    @Override
    public boolean checkIfValid(Manager m) {

        if(m.getUsers().keySet().isEmpty())
            return false;
        else if(username!=null && !m.getUsers().containsKey(username))
        	return false;
        else if(username!=null && !m.getUsers().get(username).isLoggedin())
        	return false;
        //m.getUsers().get(username).logout();
        return true;
    }

    @Override
    public void execute(Manager m, Connections<Message> connections, BGSProtocol bgsProtocol) {
    	m.getUsers().get(username).logout();
    	bgsProtocol.terminate();
    }
}
