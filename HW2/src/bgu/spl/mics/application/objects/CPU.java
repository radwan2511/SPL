package bgu.spl.mics.application.objects;

import java.util.*;
import bgu.spl.mics.application.services.CPUService;
import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.objects.GPU.Type;



/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
	private int cores;
	private Collection<DataBatch> data;
	private Cluster cluster;
	
	private CPUService cpuService;
	
	public CPU(int cores, Collection<DataBatch> data, Cluster cluster, CPUService cpuService) {
		this.cores = cores;
		this.data = data;
		this.cluster = cluster;
		this.cpuService = cpuService;
	}
	
	public DataBatch processBatch(DataBatch data) {
        // TODO Implement this
    	return null;
    }
	
	public int getCores()
    {
    	return this.cores;
    }
    
    public Collection<DataBatch> getData()
    {
    	return this.data;
    }
    
    public Cluster getCluster()
    {
    	return this.cluster;
    }
    
    public CPUService getCPUService() {
    	return this.cpuService;
    }
}
