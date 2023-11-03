package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.api.bidi.Connections;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Post extends Message{
    private String content;
    private List<String> taggedUsers;
    private List<String> recivers;
    private String sender;

    public Post(String content,String s){
        super((short)5 );
        this.content=content;
        this.sender=s;
        taggedUsers=new LinkedList<>();
        recivers=new LinkedList<>();
    }

    public String getContent() {
        return content;
    }

    public List<String> getRecivers() {
        return recivers;
    }

    public List<String> getTaggedUsers() {
        return taggedUsers;
    }

    public void findRecivers(Manager m){
        for(String name:m.getUsers().keySet()){
            String s="@"+name;
            if(content.contains(s)){
                taggedUsers.add(name);
                recivers.add(name);
            }
            else if(m.getFollowers().get(sender).contains(name)){
                recivers.add(name);
            }
        }
    }

    public String getSender() {
        return sender;
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
    public void execute(Manager m, Connections<Message> connections, BGSProtocol bgsProtocol) throws IOException {
        m.post(this,connections);
    }
}
