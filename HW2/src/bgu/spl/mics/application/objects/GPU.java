package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.application.objects.*;


/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    public static enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;
    
    private Model model;
    private Cluster cluster;
    
    private GPUService gpuService;
    
    public GPU(int type, Model model, Cluster cluster , GPUService gpuSerice) {
    	this.type = Type.values()[type];
    	this.model = model;
    	this.cluster = cluster;
    	this.gpuService = gpuService;
    }
    
    public DataBatch processEvent(DataBatch data) {
        // TODO Implement this
    	
    	return data;
    }
    
    public Type getType()
    {
    	return this.type;
    }
    
    public Model getModel()
    {
    	return this.model;
    }
    
    public Cluster getCluster()
    {
    	return this.cluster;
    }
    
    public GPUService getGPUService() {
    	return this.gpuService;
    }
    
}

