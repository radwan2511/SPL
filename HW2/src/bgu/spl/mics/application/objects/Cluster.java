package bgu.spl.mics.application.objects;

import java.util.*;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	private Collection<GPU> GPUS;
	private Collection<CPU> CPUS;
	
	public Cluster() {
		
	}
	
	/**
     * Retrieves the single instance of this class.
     */
	public static Cluster getInstance() {
		//TODO: Implement this
		return null;
	}

}
