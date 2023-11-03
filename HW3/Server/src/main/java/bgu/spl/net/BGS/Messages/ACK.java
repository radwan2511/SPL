package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.api.bidi.Connections;

public class ACK extends Message{
    private int msgOpcode;
    private String optional;

    public ACK(int msgOpcode,String optional){
        super((short)10);
        this.msgOpcode=msgOpcode;
        this.optional=optional;
    }

    public int getMsgOpcode() {
        return msgOpcode;
    }

    public Object getOptional() {
        return optional;
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
       return optional;
    }
}
