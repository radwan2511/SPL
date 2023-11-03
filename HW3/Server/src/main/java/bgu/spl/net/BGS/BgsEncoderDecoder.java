package main.java.bgu.spl.net.BGS;

import main.java.bgu.spl.net.BGS.Messages.*;
import main.java.bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BgsEncoderDecoder implements MessageEncoderDecoder<Message> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private Integer connectionId;
    private Manager manager;

    public BgsEncoderDecoder(Manager m){
        this.manager=m;
    }
    @Override
    public Message decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == ';') {
            return popMessage();
        }

        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(Message message) {
        return (message.toString() + ";").getBytes(); //uses utf8 by default
    }

    @Override
    public void connectionId(Integer id) {
        this.connectionId = id;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }


    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }
    private String getSender(){
        for(User u:manager.getUsers().values()){
            if(u.getConnectionId()==this.connectionId)
                return u.getUsername();
        }
        return null;
    }
    
    private void updateConnectionId(String username, Integer connectionId) {
    	if(manager.getUsers().containsKey(username))
    		manager.getUsers().get(username).setConnectionId(connectionId);
    }

    private Message popMessage(){
        //byte[] opcodeAr={bytes[0],bytes[1]};
        
        String str = new String(bytes, StandardCharsets.UTF_8).trim();

        String[] splited = str.split("\\s+");
        //short opcode=bytesToShort(opcodeAr);
        
        short opcode = (short) Integer.parseInt(splited[0]);
        Arrays.fill(bytes, (byte)0);
        
        if(opcode==1){
            /*int lastZero=2;
            int i=2;
            while(i<bytes.length && bytes[i]!=0){
                i++;
            }
            String username=new String(bytes, lastZero, i, StandardCharsets.UTF_8);
            lastZero=i;
            i++;
            while(i<bytes.length && bytes[i]!=0){
                i++;
            }
            String password=new String(bytes, lastZero, i, StandardCharsets.UTF_8);
            lastZero=i;
            i++;
            while(i<bytes.length && bytes[i]!=0){
                i++;
            }
            String birthday=new String(bytes, lastZero, i, StandardCharsets.UTF_8);
            return new Register(username,password,birthday,this.connectionId);*/
        	
        	Arrays.fill(bytes, (byte)0); // reset bytes array
        	return new Register(splited[1], splited[2], splited[3],this.connectionId);
        }
        if(opcode==2){
            /*int lastZero=2;
            int i=2;
            while(i<bytes.length && bytes[i]!=0){
                i++;
            }
            String username=new String(bytes, lastZero, i, StandardCharsets.UTF_8);
            lastZero=i;
            i++;
            while(i<bytes.length && bytes[i]!=0){
                i++;
            }
            String password=new String(bytes, lastZero, i, StandardCharsets.UTF_8);
            lastZero=i;
            i++;*/
            
        	//boolean captcha=bytes[bytes.length-1]==1;
        	boolean captcha = splited[splited.length-1].compareTo("1") == 0;
            
            //return new Login(username,password,captcha);
            Arrays.fill(bytes, (byte)0); // reset bytes array
            
            //update connection id
            updateConnectionId(splited[1], connectionId);
            return new Login(splited[1], splited[2],captcha);

        }
        if(opcode==3){
        	Arrays.fill(bytes, (byte)0); // reset bytes array
            return new Logout(getSender());
        }
        if(opcode==4){
            /*byte[]isfollowar={bytes[2],bytes[3]};
            short isfollowshort=bytesToShort(isfollowar);
            boolean follow=!(isfollowshort!=0);
            int i=4;
            while(i<bytes.length && bytes[i]!=0){
                i++;
            }
            String username=new String(bytes, 4, i, StandardCharsets.UTF_8);
            */
            Arrays.fill(bytes, (byte)0); // reset bytes array
            //return new Follow(follow,getSender(),username);
            return new Follow(splited[1].compareTo("00") == 0,getSender(), splited[2]);
            
        }
        if(opcode==5){
            /*int i=2;
            while(i<bytes.length && bytes[i]!=0){
                i++;
            }
            String content=new String(bytes, 2, i, StandardCharsets.UTF_8);
            */
            Arrays.fill(bytes, (byte)0); // reset bytes array
            //return new Post(content,getSender());
            String post = "";
            for(int i = 1; i< splited.length; i++)
            	post += splited[i] + " ";
            post = post.substring(0, post.length()-1);
            
            return new Post(post ,getSender());
        }
        if(opcode==6){
            /*int lastZero=2;
            int i=2;
            while(i<bytes.length && bytes[i]!=0){
                i++;
            }
            String username=new String(bytes, lastZero, i, StandardCharsets.UTF_8);
            lastZero=i;
            i++;
            while(i<bytes.length && bytes[i]!=0){
                i++;
            }
            String content=new String(bytes, lastZero, i, StandardCharsets.UTF_8);
            lastZero=i;
            i++;
            while(i<bytes.length && bytes[i]!=0){
                i++;
            }
            String sendingdate=new String(bytes, lastZero, i, StandardCharsets.UTF_8);
            */
            Arrays.fill(bytes, (byte)0); // reset bytes array
            //return new PM(username,content,sendingdate,getSender());
            
            String post = "";
            for(int i = 2; i< splited.length-2; i++)
            	post += splited[i] + " ";
            post = post.substring(0, post.length()-1);
            
            
            return new PM(splited[1],post ,splited[splited.length-2] + " " + splited[splited.length-1],getSender());
        }
        if(opcode==7){
            Arrays.fill(bytes, (byte)0); // reset bytes array
            return new LogStat(getSender());
        }
        if(opcode==8){
            /*int i=2;
            while(i<bytes.length && bytes[i]!=0){
                i++;
            }
            String names=new String(bytes, 2, i, StandardCharsets.UTF_8);*/
            //String[]usernames=names.split("|");
        	
        	        	
        	String[] usernames = splited[1].split("\\|");
            List<String> users=new LinkedList<>();
            for(int j=0;j<usernames.length;j++){
                users.add(usernames[j]);
            }
            
            Arrays.fill(bytes, (byte)0); // reset bytes array
            return new Stat(getSender(),users);
        }
        if(opcode==12){
            /*int i=2;
            while(i<bytes.length && bytes[i]!=0){
                i++;
            }
            String username=new String(bytes, 2, i, StandardCharsets.UTF_8);
            */
            Arrays.fill(bytes, (byte)0); // reset bytes array
            return new Block(getSender(), splited[1]);
            //return new Block(getSender(),username);
        }
                
        return null;
    }

    public short bytesToShort(byte[] byteArr)

    {
        short result = (short)((byteArr[0] & 0xff) << 8);

        result += (short)(byteArr[1] & 0xff);

        return result;

    }


}
