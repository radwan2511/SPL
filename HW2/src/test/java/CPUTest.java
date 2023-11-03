package test.java;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.CPUService;
import bgu.spl.mics.application.services.GPUService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;


public class CPUTest {
    CPU cpu;

    @BeforeEach
    void setUp() throws Exception {
        cpu = new CPU(6, null , new Cluster(), new CPUService("name") );
    }
    
    @Test
    void processBatchTest(DataBatch data) {
        assertEquals(new DataBatch() ,cpu.processBatch(data));
    }
    
    
    @Test
    void getCoresTest (){
        assertEquals(6 ,cpu.getCores() );
    }
    
    @Test
    void getDataTest (){
        assertEquals(null ,cpu.getData());
    }
    
    @Test
    void GetClusterTest (){
        assertEquals(new Cluster() ,cpu.getCluster());
    }
    
    @Test
    void GetCPUServiceTest (){
        assertEquals(new CPUService("name") ,cpu.getCPUService());
    }
    

}