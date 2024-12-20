package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.*;


/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

	private boolean workingTrainModelEvent;
	
    public GPUService(String name) {
        super("Change_This_Name");
        // TODO Implement this
        
    }
    
    @Override
    protected void initialize() {
        // TODO Implement this
    	
    }
    
    public DataBatch trainModelEvent(GPU gpu) {
        // TODO Implement this
    	return null;
    }
    
    public DataBatch testModelEvent(GPU gpu) {
        // TODO Implement this
    	return null;
    }
    
    public boolean getWorkingTrainModelEvent() {
    	return this.workingTrainModelEvent;
    }
}
