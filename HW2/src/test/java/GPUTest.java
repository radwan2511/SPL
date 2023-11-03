package test.java;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.objects.GPU.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bgu.spl.mics.application.services.*;


public class GPUTest<T> {
    GPU gpu;
    
    @BeforeEach
    void setUp() throws Exception {
        gpu = new GPU(0, new Model(), new Cluster(), new GPUService("name"));
    }
    
    @Test
    void processEventTest() throws InterruptedException {
       assertEquals(new DataBatch(),gpu.processEvent(new DataBatch()));
   }
    
    @Test
    void GetTypeTest (){
        assertEquals(GPU.Type.RTX3090 ,gpu.getType() );
    }
    
    @Test
    void GetModelTest (){
        assertEquals(new Model() ,gpu.getModel());
    }
    
    @Test
    void GetClusterTest (){
        assertEquals(new Cluster() ,gpu.getCluster());
    }
    
    @Test
    void GetGPUServiceTest (){
        assertEquals(new GPUService("name") ,gpu.getGPUService());
    }
    
}
