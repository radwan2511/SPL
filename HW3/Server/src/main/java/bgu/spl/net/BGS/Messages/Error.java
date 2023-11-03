package main.java.bgu.spl.net.BGS.Messages;

import main.java.bgu.spl.net.BGS.BGSProtocol;
import main.java.bgu.spl.net.BGS.Manager;
import main.java.bgu.spl.net.api.bidi.Connections;

public class Error extends Message {
    private int messageOpcode;

    public Error(int messageOpcode){
        super((short)11);
        this.messageOpcode=messageOpcode;
    }

    public int getMessageOpcode() {
        return messageOpcode;
    }

    @Override
    public boolean checkIfValid(Manager m) {
        return false;
    }
    @Override
    public void execute(Manager m, Connections<Message> connections, BGSProtocol bgsProtocol) {

    }

    @Override
    public String toString() {
        return "Error "+Integer.toString(messageOpcode)+";";
    }
}
