package no.stelvio.batch.controller.support;

import java.util.Map;

import no.stelvio.batch.BatchBi;
import no.stelvio.batch.BatchRegistry;
import no.stelvio.batch.controller.BatchControllerServiceBi;
import no.stelvio.common.config.InvalidPropertyException;
import no.stelvio.common.config.MissingPropertyException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Work controller service for batches. Use the server side batch context configuration which is described in a xml file, for
 * ex. btc-opptjeningbatch-context.xml Guideline - Batch - Initializing RequestContext for Batch: - describes how to set up the
 * interceptor component used to initialize the RequestContext for batch jobs. - describes also how to sett up the configuration
 * for each batch. - batchNameMap is used by DefaultBatchControllerService to get id's for protoype batch-object beans to run.
 * Map key is batchName
 * 
 * @author persond9e847e67144, NAV
 * @author persondfa1fa919e87, Accenture
 * 
 * @version $Id$
 */
public class DefaultBatchControllerService implements BatchControllerServiceBi, ApplicationContextAware {

	private final Log log = LogFactory.getLog(DefaultBatchControllerService.class);

	private ApplicationContext applicationContext;

	// The name of the Map used by BatchController to get id's for protoype
	// batch-object beans to run.
	// Map key is batchName
	private Map batchNameMap;

	
	private static final String SLICE_STRING = " slice ";
	
	/** {@inheritDoc} */
	public int executeBatch(String batchName, int slice) {
		String batchBeanId = (String) batchNameMap.get(batchName);

		// batchBeanMap needs to reference map of batch ids
		if (batchBeanId == null) {
			throw new MissingPropertyException("Batch controller configuration missing property in batch name map for batch: "
					+ batchName, new String[] { "batchBeanId" });

		}

		BatchBi batch = null;
		try {
			batch = (BatchBi) getApplicationContext().getBean(batchBeanId);
			batch.setBatchName(batchName);
			batch.setSlice(slice);
			
			// Register batch
			registerBatch(batchName, slice, batch);
			int result = batch.executeBatch(slice);
			return result;

		} catch (BeansException be) {
			throw new InvalidPropertyException("Could not create batch instance, due to bad configuration",
					new Object[] { batchBeanId }, be);
		} finally {
			unregisterBatch(batchName, slice, batch);
		}
	}

	/** {@inheritDoc} */
	public boolean stopBatch(String batchName, int slice) {
		boolean batchWasLocatedAndStopped = false;
		if (isRegistryConfiguredCorrectly()) {
			BatchRegistry runningBatches = getBatchRegistry();
			if (runningBatches.isBatchRegistered(batchName, slice)) {
				BatchBi runningBatch = getBatchRegistry().getBatch(batchName, slice);
				runningBatch.stopBatch();
				if (log.isInfoEnabled()) {
					log.info("Running batch " + batchName + SLICE_STRING + slice + " was located and stop was requested");
				}
				batchWasLocatedAndStopped = true;

			} else {
				if (log.isInfoEnabled()) {
					log.info("No running batch with name " + batchName + SLICE_STRING + slice + " found");
				}
			}
		}
		return batchWasLocatedAndStopped;
	}

	/**
	 * Registers a batch execution with the registry configured in the applicatioContext.
	 * 
	 * @param batchName batch name
	 * @param slice slice
	 * @param batch batch
	 */
	@SuppressWarnings("unchecked")
	private void registerBatch(String batchName, int slice, BatchBi batch) {
		Map<String, BatchRegistry> registries = getApplicationContext().getBeansOfType(BatchRegistry.class);
		if (registries.isEmpty()) {
			if (log.isInfoEnabled()) {
				log.debug("No BatchRegistry defined in ApplicationContext, batch " + batchName + SLICE_STRING + slice
						+ " will still" + " be executed, but can not be stopped");
			}
		} else if (registries.values().size() > 1) {
			if (log.isInfoEnabled()) {
				log.info("More than 1 BatchRegistry defined in ApplicationContext, batch " + batchName + SLICE_STRING + slice
						+ " will still" + " be executed, but can not be stopped.");
				log.debug("The following BatchRegistries was found: " + registries.keySet().toString());
			}
		} else {
			if (log.isInfoEnabled()) {
				log.info("Batch registry was succesfully retrieved");
				log.info("Registering batch " + batchName + SLICE_STRING + slice);
			}
			BatchRegistry runningBatches = registries.values().iterator().next();
			runningBatches.registerBatch(batchName, slice, batch);
		}
	}

	/**
	 * Unregister a batch.
	 * 
	 * @param batchName batch name
	 * @param slice slice
	 * @param batch batch
	 */
	private void unregisterBatch(String batchName, int slice, BatchBi batch) {
		if (isRegistryConfiguredCorrectly()) {
			if (log.isDebugEnabled()) {
				log.debug("Batch registry was succesfully retrieved");
				log.debug("Unregistering batch " + batchName + SLICE_STRING + slice);
			}
			BatchRegistry runningBatches = getBatchRegistry();
			boolean unregistered = runningBatches.unregisterBatch(batchName, slice);
			if (log.isInfoEnabled()) {
				if (unregistered) {
					log.info("Batch " + batchName + SLICE_STRING + slice + " was unregistered");
				} else {
					log.info("No batch matching batchName=" + batchName + SLICE_STRING + slice
							+ " was found, and could thus not be unregistered");
				}
			}

		} else {
			if (log.isDebugEnabled()) {
				log.debug("Only 1 BatchRegistry may be configured in an applicationContext. Unable to unregistrer Batch");
			}
		}
	}

	/**
	 * Checks if registry is correctly configured.
	 * 
	 * @return <code>true</code> if 1 and only 1 is configured in applicationContext, otherwise <code>false</code>
	 */
	private boolean isRegistryConfiguredCorrectly() {
		return (getApplicationContext().getBeansOfType(BatchRegistry.class).values().size() == 1);
	}

	/**
	 * Get batch registry.
	 * 
	 * @return batch registry
	 */
	private BatchRegistry getBatchRegistry() {
		return (BatchRegistry) getApplicationContext().getBeansOfType(BatchRegistry.class).values().iterator().next();
	}

	/**
	 * Called by Spring to validate that the required dependency injection is properly configured. The batch controller needs to
	 * have configured a batch name map containing the application batches that the controller can execute.
	 * 
	 * @throws MissingPropertyException
	 *             If a dependency injection property is missing from the batch controller configuration.
	 */
	public void performSanityCheck() throws MissingPropertyException {
		if (batchNameMap == null) {
			throw new MissingPropertyException(
					"The batchNameMap was not set, this may have been caused by a insufficient configuration",
					new String[] { "batchNameMap" });
		}
	}

	/**
	 * Get the batchNameMap.
	 * 
	 * @return batchNameMap - String
	 */
	public Map getBatchNameMap() {
		return batchNameMap;
	}

	/**
	 * Set the batchNameMap.
	 * 
	 * @param batchNameMap
	 *            - String
	 */
	public void setBatchNameMap(Map batchNameMap) {
		this.batchNameMap = batchNameMap;
	}

	/** {@inheritDoc} */
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.applicationContext = ctx;
	}

	/**
	 * Gets the applicationContext.
	 * 
	 * @return applicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

}
