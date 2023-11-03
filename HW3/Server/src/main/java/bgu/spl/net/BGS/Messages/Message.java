package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.api.bidi.Connections;

import java.io.IOException;
import java.io.Serializable;

public abstract class Message{
    private short opcode;

    public Message(short opcode){
        this.opcode=opcode;
    }
    public short getOpcode() {
        return opcode;
    }

    public abstract boolean checkIfValid(Manager m);
    public abstract void execute(Manager m, Connections<Message> connections, BGSProtocol bgsProtocol) throws IOException;


}
