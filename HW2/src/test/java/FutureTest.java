package test.java;


import bgu.spl.mics.Future;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;


public class FutureTest<T> {
    Future future;
    T res;

    @BeforeEach
    void setUp() throws Exception {
        future=new Future();
        res= (T) new Object();
    }
    @Test
     void getTest() throws InterruptedException {
        if(future.isDone())
            future.resolve(res);
        else
            wait();
        assertEquals(res,future.get());
    }
    @Test
    void resolveTest (T result){
        resolveTest(result);
        assertEquals(result,future.get());
    }
    @Test
     void isDoneTest(){
        assertEquals(false, future.isDone());
        future.resolve(res);
        assertEquals(true, future.isDone());

    }
    @Test
    void getTest(long timeout, TimeUnit unit) throws InterruptedException {
        if(future.isDone())
            future.resolve(res);
        else
            unit.sleep(timeout);
        assertEquals(res,future.get());

    }



}
