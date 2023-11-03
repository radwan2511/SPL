package main.java.bgu.spl.net.BGS;

//import main.java.bgu.spl.net.impl.rci.ObjectEncoderDecoder;
//import main.java.bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import main.java.bgu.spl.net.srv.Server;

public class BGSServerMain {
    public static void main(String[] args){
        Manager manager=new Manager();
        // you can use any server...
        Server.threadPerClient(
                7777, //port
                () -> new BGSProtocol(manager), //protocol factory
               ()->new BgsEncoderDecoder(manager)//message encoder decoder factory
        ).serve();

//        Server.reactor(
//                Runtime.getRuntime().availableProcessors(),
//                7777, //port
//                () -> new BGSProtocol(manager), //protocol factory
//                ()->new BgsEncoderDecoder(manager) //message encoder decoder factory
//        ).serve();

    }
    }

