package no.stelvio.batch.controller.support;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import no.stelvio.batch.BatchBi;
import no.stelvio.batch.BatchRegistry;
import no.stelvio.batch.controller.BatchControllerServiceBi;
import no.stelvio.batch.support.ControllerServiceHistorySupport;
import no.stelvio.common.config.InvalidPropertyException;
import no.stelvio.common.config.MissingPropertyException;

/**
 * Work controller service for batches. Use the server side batch context configuration which is described in a xml file, for
 * ex. btc-opptjeningbatch-context.xml Guideline - Batch - Initializing RequestContext for Batch: - describes how to set up the
 * interceptor component used to initialize the RequestContext for batch jobs. - describes also how to set up the configuration
 * for each batch. - batchNameMap is used by DefaultBatchControllerService to get id's for prottoype batch-object beans to run.
 * Map key is batchName
 * 
 * @author persond9e847e67144 (NAV)
 * @author persondfa1fa919e87 (Accenture)
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class DefaultBatchControllerService implements BatchControllerServiceBi, ApplicationContextAware {

	protected static final String JOB_NAME = "jobName";

	private static final Log LOGGER = LogFactory.getLog(DefaultBatchControllerService.class);

	private ApplicationContext applicationContext;
	private ControllerServiceHistorySupport controllerServiceHistorySupport;

	// The name of the Map used by BatchController to get id's for prototype
	// batch-object beans to run.
	// Map key is batchName
	@SuppressWarnings("unchecked")
	private Map batchNameMap;

	private static final String SLICE_STRING = " slice ";

	/** {@inheritDoc} */
	public int executeBatch(String batchName, int slice) {
		if (batchName != null) {
			MDC.put(JOB_NAME, batchName.toLowerCase());
		}

		String batchBeanId = (String) batchNameMap.get(batchName);

		// batchBeanMap needs to reference map of batch ids
		if (batchBeanId == null) {
			throw new MissingPropertyException("Batch controller configuration missing property in batch name map for batch: "
					+ batchName, new String[] { "batchBeanId" });
		}

		BatchBi batch;
		try {
			batch = (BatchBi) applicationContext.getBean(batchBeanId);
			batch.setBatchName(batchName);
			batch.setSlice(slice);

			registerBatch(batchName, slice, batch);

			long batchHistoryId = 0;

			if (controllerServiceHistorySupport != null) {
				batchHistoryId = controllerServiceHistorySupport.saveInitialBatchInformation(batchName, slice);
			}

			int result = batch.executeBatch(slice);

			if (batchHistoryId != 0) {
				controllerServiceHistorySupport.saveAdditionalBatchInformation(batchHistoryId, result);
			}

			return result;

		} catch (BeansException be) {
			throw new InvalidPropertyException("Could not create batch instance, due to bad configuration",
					new Object[] { batchBeanId }, be);
		} finally {
			unregisterBatch(batchName, slice);
			MDC.remove(JOB_NAME);
		}
	}

	/** {@inheritDoc} */
	public boolean stopBatch(String batchName, int slice) {
		if (batchName != null) {
			MDC.put(JOB_NAME, batchName.toLowerCase());
		}
		boolean batchWasLocatedAndStopped = false;
		if (isRegistryConfiguredCorrectly()) {
			BatchRegistry runningBatches = getBatchRegistry();
			if (runningBatches.isBatchRegistered(batchName, slice)) {
				BatchBi runningBatch = getBatchRegistry().getBatch(batchName, slice);
				runningBatch.stopBatch();
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Running batch " + batchName + SLICE_STRING + slice + " was located and stop was requested");
				}
				batchWasLocatedAndStopped = true;		
			} else {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("No running batch with name " + batchName + SLICE_STRING + slice + " found");
				}
			}
		}
		MDC.remove(JOB_NAME);
		return batchWasLocatedAndStopped;
	}

	/**
	 * Registers a batch execution with the registry configured in the applicatioContext.
	 * 
	 * @param batchName
	 *            batch name
	 * @param slice
	 *            slice
	 * @param batch
	 *            batch
	 */
	@SuppressWarnings("unchecked")
	private void registerBatch(String batchName, int slice, BatchBi batch) {
		Map<String, BatchRegistry> registries = applicationContext.getBeansOfType(BatchRegistry.class);
		if (registries.isEmpty()) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.debug("No BatchRegistry defined in ApplicationContext, batch " + batchName + SLICE_STRING + slice
						+ " will still" + " be executed, but can not be stopped");
			}
		} else if (registries.values().size() > 1) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("More than 1 BatchRegistry defined in ApplicationContext, batch " + batchName + SLICE_STRING
						+ slice + " will still" + " be executed, but can not be stopped.");
				LOGGER.debug("The following BatchRegistries was found: " + registries.keySet().toString());
			}
		} else {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Batch registry was succesfully retrieved");
				LOGGER.info("Registering batch " + batchName + SLICE_STRING + slice);
			}
			BatchRegistry runningBatches = registries.values().iterator().next();
			runningBatches.registerBatch(batchName, slice, batch);
		}
	}

	/**
	 * Unregister a batch.
	 *  @param batchName
	 *            batch name
	 * @param slice
	 *            slice
	 */
	private void unregisterBatch(String batchName, int slice) {
		if (isRegistryConfiguredCorrectly()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Batch registry was succesfully retrieved");
				LOGGER.debug("Unregistering batch " + batchName + SLICE_STRING + slice);
			}
			BatchRegistry runningBatches = getBatchRegistry();
			boolean unregistered = runningBatches.unregisterBatch(batchName, slice);
			if (LOGGER.isInfoEnabled()) {
				if (unregistered) {
					LOGGER.info("Batch " + batchName + SLICE_STRING + slice + " was unregistered");
				} else {
					LOGGER.info("No batch matching batchName=" + batchName + SLICE_STRING + slice
							+ " was found, and could thus not be unregistered");
				}
			}

		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Only 1 BatchRegistry may be configured in an applicationContext. Unable to unregistrer Batch");
			}
		}
	}

	/**
	 * Checks if registry is correctly configured.
	 * 
	 * @return <code>true</code> if 1 and only 1 is configured in applicationContext, otherwise <code>false</code>
	 */
	private boolean isRegistryConfiguredCorrectly() {
		return (applicationContext.getBeansOfType(BatchRegistry.class).values().size() == 1);
	}

	/**
	 * Get batch registry.
	 * 
	 * @return batch registry
	 */
	private BatchRegistry getBatchRegistry() {
		return applicationContext.getBeansOfType(BatchRegistry.class).values().iterator().next();
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
	@SuppressWarnings("unchecked")
	public Map getBatchNameMap() {
		return batchNameMap;
	}

	/**
	 * Set the batchNameMap.
	 * 
	 * @param batchNameMap
	 *            - String
	 */
	@SuppressWarnings("unchecked")
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
	protected ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

	/**
	 * Set the controllerServiceHistorySupport.
	 * 
	 * @param controllerServiceHistorySupport
	 *            - ControllerServiceHistorySupport
	 */
	public void setControllerServiceHistorySupport(ControllerServiceHistorySupport controllerServiceHistorySupport) {
		this.controllerServiceHistorySupport = controllerServiceHistorySupport;
	}
}
