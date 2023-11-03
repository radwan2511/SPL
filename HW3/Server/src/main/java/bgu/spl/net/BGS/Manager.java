package main.java.bgu.spl.net.BGS;

import main.java.bgu.spl.net.BGS.Messages.*;
import main.java.bgu.spl.net.BGS.Messages.Error;
import main.java.bgu.spl.net.api.bidi.Connections;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import java.util.Vector;

public class Manager {

    private ConcurrentHashMap<String,User>users;
    private ConcurrentHashMap<String, List<String>>following;
    private ConcurrentHashMap<String, List<String>>followers;
    private ConcurrentHashMap<String,List<Post>>posts;
    private ConcurrentHashMap<User,List<PM>>privateMessages;
    private List<String>filteredWords;
    private ConcurrentHashMap<String,List<String>>blocked;
    
    public Manager(){
        following=new ConcurrentHashMap<>();
        users=new ConcurrentHashMap<>();
        posts=new ConcurrentHashMap<>();
        privateMessages=new ConcurrentHashMap<>();
        filteredWords=new LinkedList<>();
        blocked=new ConcurrentHashMap<>();
        followers=new ConcurrentHashMap<>();
    }



    public synchronized void addFilterdWord(String word){
        if(!filteredWords.contains(word))
            filteredWords.add(word);
    }
    public void filter(String s){
        for(String word:filteredWords){
            s.replaceAll(word,"<filtered>");
        }
    }
    public void sendPM(PM pm,Connections<Message>connections) throws IOException {
        filter(pm.getContent());
        if(privateMessages.containsKey(pm.getSender()))
            privateMessages.get(pm.getSender()).add(pm);
        if(users.containsKey(pm.getSender())) {
        	//connections.send(users.get(pm.getSender()).getConnectionId(),new Notification(false,pm.getSender(),pm.getContent()));
        	connections.send(users.get(pm.getUsername()).getConnectionId(),new Notification(false,pm.getSender(),pm.getContent() + " " + pm.getSendingDateAndTime()));
        }
            
    }

    public void post(Post p, Connections<Message> connections) throws IOException {
        posts.get(p.getSender()).add(p);
        p.findRecivers(this);
        boolean b=false;
        for(String name:p.getRecivers()){
           b=connections.send(users.get(name).getConnectionId(),new Notification(true,p.getSender(),p.getContent()));
        }
    }


    public String getLogstat(Message msg){
        LogStat ls=(LogStat)msg;
        String ans="";
        for(User u:ls.createLogStat(this)){
            ans+="ACK ";
            ans+=Integer.toString(7)+" ";
            ans+=Integer.toString(u.getAge())+" ";
            ans+=Integer.toString(u.getNumOfPosts())+" ";
            ans+=Integer.toString(u.getNumOfFollowers())+" ";
            ans+=Integer.toString(u.getNumOfFollowing())+"\0\n";
        }
        ans+=";";
        return ans;

    }

    public String getStat(Message msg){
        String ans="";
        for(String name:((Stat) msg).getUsernames()){
            User u=users.get(name);
            ans+="ACK ";
            ans+=Integer.toString(8)+" ";
            ans+=Integer.toString(u.getAge())+" ";
            ans+=Integer.toString(u.getNumOfPosts())+" ";
            ans+=Integer.toString(u.getNumOfFollowers())+" ";
            ans+=Integer.toString(u.getNumOfFollowing())+"\0\n";
        }

        ans+=";";
        return ans;
    }


    public String getFollowAck(Message msg){
        String ans="ACK ";
        ans+="4 ";
        ans+="FOLLOW " + ((Follow) msg).followOpCode() + " ";
        ans+=((Follow)msg).getUsername();
        ans+=";";
        return ans;
    }



    public Message getResponse(Message msg){
        if(msg.getOpcode()==1){
            if(msg.checkIfValid(this)){
                //msg.execute(this);
                return new ACK(msg.getOpcode(),"ACK "+ Short.toString(msg.getOpcode()));
            }
            else{
                return new Error(msg.getOpcode());
            }

        }

        if(msg.getOpcode()==2){
            if(msg.checkIfValid(this)){
                //msg.execute(this);
                return new ACK(msg.getOpcode(),"ACK "+ Short.toString(msg.getOpcode()));
            }
            else{
                return new Error(msg.getOpcode());
            }
        }

        if(msg.getOpcode()==3){
            if(msg.checkIfValid(this)){
                //msg.execute(this);
                return new ACK(msg.getOpcode(),"ACK "+Short.toString(msg.getOpcode()));
            }
            else{
                return new Error(msg.getOpcode());
            }
        }

        if(msg.getOpcode()==4){
            if(msg.checkIfValid(this)){
                //msg.execute(this);
                return new ACK(msg.getOpcode(),getFollowAck(msg));
            }
            else{
                return new Error(msg.getOpcode());
            }
        }


        if(msg.getOpcode()==6){
            if(msg.checkIfValid(this)){
                //msg.execute(this);
                 return new ACK(msg.getOpcode(),"ACK "+Short.toString(msg.getOpcode()));
            }
            else{
                return new Error(msg.getOpcode());
            }
        }
        if(msg.getOpcode()==12){
            if(msg.checkIfValid(this)){
                //msg.execute(this);
                return new ACK(msg.getOpcode(),"ACK "+Short.toString(msg.getOpcode()));
            }
            else{
                return new Error(msg.getOpcode());
            }
        }
        if(msg.getOpcode()==7){
            if(msg.checkIfValid(this)){
                return new ACK(msg.getOpcode(),getLogstat(msg));
            }
            else{
                return new Error(msg.getOpcode());
            }
        }

        if(msg.getOpcode()==8){
            if(msg.checkIfValid(this)){
                return new ACK(msg.getOpcode(),getStat(msg));
            }
            else{
                return new Error(msg.getOpcode());
            }
        }

        if(msg.getOpcode()==5){
            if(msg.checkIfValid(this)){
                //msg.execute(this);
                return new ACK(msg.getOpcode(),"ACK "+Short.toString(msg.getOpcode()));
            }
            else{
                return new Error(msg.getOpcode());
            }
        }

        return null;
    }



    public boolean isBlocked(String sender,String user) {
    	if(blocked.get(sender).contains(user))
    		return true;
    	else
    		return false;
    }
    public ConcurrentHashMap<String, User> getUsers() {
        return users;
    }

    public ConcurrentHashMap<User, List<PM>> getPrivateMessages() {
        return privateMessages;
    }


    public List<String> getFilteredWords() {
        return filteredWords;
    }

    public ConcurrentHashMap<String, List<Post>> getPosts() {
        return posts;
    }

    public ConcurrentHashMap<String, List<String>> getBlocked() {
        return blocked;
    }

    public ConcurrentHashMap<String, List<String>> getFollowers() {
        return followers;
    }

    public ConcurrentHashMap<String, List<String>> getFollowing() {
        return following;
    }

}
