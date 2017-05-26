package no.stelvio.batch;

import no.stelvio.batch.controller.BatchControllerServiceBi;
import no.stelvio.batch.support.StaticBatchRegistry;

/**
 * A BatchRegistry is used to have a reference to running batches which are defined as Spring Prototypes. As they are defined as
 * prototypes getting a them from the ApplicationContext will always return a new instance.
 * 
 * The registry is used by the {@link BatchControllerServiceBi} to hold a reference to running batches. These references are
 * used when the {@link BatchControllerServiceBi} is told to stop a running batch.
 * 
 * The simple implementation of this interface is the {@link StaticBatchRegistry} which uses a map variable to store running
 * batches in memory. This implementation is however not appropriate if batches are run in a clustered envionment. For a
 * clustered environment a registry using MBeans might be more appropriate, if the application server supports using MBeans
 * across nodes in a cluster.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public interface BatchRegistry {

	/**
	 * Register a batch with specified batchName and slice.
	 * 
	 * @param batchName
	 *            the name of the batch to register
	 * @param slice
	 *            the slice number of the batch to register
	 * @param batch
	 *            the BatchBi instance
	 */
	void registerBatch(String batchName, int slice, BatchBi batch);

	/**
	 * Removes the batch with the given batchName and slice from the register.
	 * 
	 * @param batchName
	 *            the name of the batch to unregister
	 * @param slice
	 *            the slice number of the batch to unregister
	 * @return boolean true if a batch was unregistered
	 */
	boolean unregisterBatch(String batchName, int slice);

	/**
	 * Returns true if a batch with specified batchName and slice is registerd.
	 * 
	 * @param batchName
	 *            the name of the batch
	 * @param slice
	 *            the slice number of the batch
	 * @return <code>true</code> if a batch with specified id is in the registry
	 */
	boolean isBatchRegistered(String batchName, int slice);

	/**
	 * Returns the BatchBi registered with the batchName and slice.
	 * 
	 * @param batchName
	 *            the name of the batch
	 * @param slice
	 *            the slice number of the batch
	 * @return BatchBi with matching name and slice, <code>null</code> if no such batch is registered
	 */
	BatchBi getBatch(String batchName, int slice);

}
