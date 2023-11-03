package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.api.bidi.Connections;

public class Login extends Message{
    private String username;
    private String password;
    private boolean captcha;
    
    
    public Login(String user,String pass,boolean cap){
        super((short) 2);
        username=user;
        password=pass;
        this.captcha=cap;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean checkIfValid(Manager m) {
        if(!m.getUsers().keySet().contains(username))
            return false;
        else if(!password.equals(m.getUsers().get(username).getPassword()))
            return false;
        else if(!captcha)
            return false;
        else if(m.getUsers().get(username).isLoggedin())
        	return false;
        
        m.getUsers().get(username).login();
        return true;
    }

    @Override
    public void execute(Manager m, Connections<Message> connections, BGSProtocol bgsProtocol) {
        m.getUsers().get(username).login();
    }
}
