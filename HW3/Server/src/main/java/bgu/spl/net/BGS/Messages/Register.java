package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.BGS.User;
import main.java.bgu.spl.net.api.bidi.Connections;

import java.util.LinkedList;

public class Register extends Message{
    private String username;
    private String password;
    private String birthday;
    private Integer connectionId;

    public Register(String name,String pass,String birth,Integer conId){
        super((short)1);
        username=name;
        password=pass;
        birthday=birth;
        this.connectionId=conId;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean checkIfValid(Manager m) {
        if(m.getUsers().keySet().contains(username))
            return false;
        return true;
    }

    @Override
    public void execute(Manager m, Connections<Message> connections, BGSProtocol bgsProtocol) {
        User user=new User(connectionId,username,password,birthday);
        m.getUsers().putIfAbsent(username,user);
        m.getFollowers().putIfAbsent(username,new LinkedList<>());
        m.getBlocked().putIfAbsent(username,new LinkedList<>());
        m.getFollowing().putIfAbsent(username,new LinkedList<>());
        m.getPosts().putIfAbsent(username, new LinkedList<>());

    }
}
