package test.java;

import static org.junit.jupiter.api.Assertions.*;

import bgu.spl.mics.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class MessageBusTest<T> {
    MessageBus bus;

    @BeforeEach
    void setUp() throws Exception {
        bus=new MessageBusImpl();
    }

    @Test
    void subscribeEventTest(Class<? extends Event<T>> type, MicroService m){
        assertEquals(false, bus.isSubscribedEvent(type,m));
        bus.subscribeEvent(type,m);
        assertEquals(true,bus.isSubscribedEvent(type,m) );

    }
    @Test
    void subscribeBroadcastTest(Class<? extends Broadcast> type, MicroService m){
        assertEquals(false, bus.isSubscribedBroadcast(type,m));
        bus.subscribeBroadcast(type,m);
        assertEquals(true,bus.isSubscribedBroadcast(type,m));

    }
    @Test
    void completeTest(Event<T> e, T result){
        assertEquals(false,bus.isCompleted(e,result));
        bus.complete(e,result);
        assertEquals(true,bus.isCompleted(e,result));

    }

    @Test
    void sendBroadcastTest(Broadcast b){
        MicroService m=new ServiceExample("example");
        bus.subscribeBroadcast(b.getClass(),m);
        assertEquals(1,bus.size(m));

    }
    @Test
    void sendEventTest(Event<T> e){
        MicroService m=new ServiceExample("example");
        Class<? extends Event<T>> type= (Class<? extends Event<T>>) e.getClass();
        bus.subscribeEvent(type,m);
        assertEquals(1,bus.size(m));
    }

    @Test
    void registerTest(MicroService m){
        assertEquals(false,bus.isRegistered(m));
        bus.register(m);
        assertEquals(true,bus.isRegistered(m));


    }
    @Test
    void unregisterTest(MicroService m){
        bus.register(m);
        assertEquals(true,bus.isRegistered(m));
        bus.unregister(m);
        assertEquals(false,bus.isRegistered(m));
    }
    @Test
    void awaitMessageTest(MicroService m) throws InterruptedException{
        if(bus.size(m)==0){
            assertEquals(null,bus.awaitMessage(m));
        }
        else{
            assertEquals(bus.getNextMessage(m),bus.awaitMessage(m));
        }

    }


    class ServiceExample extends MicroService{

        /**
         * @param name the micro-service name (used mainly for debugging purposes -
         *             does not have to be unique)
         */
        public ServiceExample(String name) {
            super(name);
        }

        @Override
        protected void initialize() {

        }
    }



}
