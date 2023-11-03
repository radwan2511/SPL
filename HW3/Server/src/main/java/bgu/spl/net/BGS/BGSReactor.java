package main.java.bgu.spl.net.BGS;

import main.java.bgu.spl.net.srv.Server;

public class BGSReactor {
	public static void main(String[] args) {
		Manager manager=new Manager();
		  Server.reactor(
               Runtime.getRuntime().availableProcessors(),
                7777, //port
                () -> new BGSProtocol(manager), //protocol factory
               ()->new BgsEncoderDecoder(manager) //message encoder decoder factory
        ).serve();
	}

}
