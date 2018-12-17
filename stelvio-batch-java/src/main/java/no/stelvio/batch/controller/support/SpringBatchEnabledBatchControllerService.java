package no.stelvio.batch.controller.support;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;

import no.stelvio.batch.controller.SpringBatchEnabledBatchControllerServiceBi;
import no.stelvio.batch.controller.SpringBatchJobOperator;
import no.stelvio.common.context.support.ComponentIdHolder;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.context.support.SimpleRequestContext.Builder;

/**
 * Extension of {@link DefaultBatchControllerService} in order to handle execution of Spring Batches.
 * 
 * @author person47c121e3ccb5, BEKK
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class SpringBatchEnabledBatchControllerService extends DefaultBatchControllerService implements
		SpringBatchEnabledBatchControllerServiceBi {

	private static final Log LOGGER = LogFactory.getLog(SpringBatchEnabledBatchControllerService.class);

	private SpringBatchJobOperator springBatchOperator;

	@Override
	public int executeBatch(String jobName, String parameters) {

		Builder contextBuilder = new SimpleRequestContext.Builder();
		String componentId = retrieveComponentId();
		String uuid = UUID.randomUUID().toString();
				
		contextBuilder.userId(jobName);
		contextBuilder.componentId(componentId);
		contextBuilder.transactionId(uuid);
			
		if (componentId != null)
			MDC.put("componentId", componentId);
		if (jobName != null) 
			MDC.put(JOB_NAME, jobName.toLowerCase());
		
		MDC.put("transactionId", uuid);
		
		LOGGER.info("Executing batch with jobName=" + jobName + " , parameters=" + parameters + " , transactionID=" + uuid + " and componentId=" + componentId);

		RequestContextSetter.setRequestContext(contextBuilder.build());
		
		// TODO: Intitiell batchhistorikk
		int twsCode = springBatchOperator.executeBatch(jobName, parameters);
		//TODO: avslutningsvis historikk
		MDC.remove(JOB_NAME);
		MDC.remove("transactionId");
		MDC.remove("componentId");

		return twsCode;
	}

	@Override
	public boolean stopBatch(String jobName) {
		if (jobName != null) {
			MDC.put(JOB_NAME, jobName.toLowerCase());
		}
		LOGGER.info("stopping batch with jobName=" + jobName);
		boolean stopped = springBatchOperator.stopBatch(jobName);
		//TODO: Historikk om at batch er stoppet 
		MDC.remove(JOB_NAME);
		return stopped;
	}

	/**
	 * Sets implementation to use.
	 * 
	 * @param springBatchOperator
	 *            springBatchOperator to set.
	 */
	public void setSpringBatchOperator(SpringBatchJobOperator springBatchOperator) {
		this.springBatchOperator = springBatchOperator;
	}

	/**
	 * Goes through the ApplicationContext to find the ComponentIdHolder that sets the ComponentId.
	 * 
	 * Logs error if no ComponentId has been specified. Logs warn if more than one ComponentId has been specified
	 * 
	 * If more than one componentId is configured in the ApplicationContext, the first will be used. If more than one is
	 * configured with the same name (or no name), the result is unspecified (the last one will probably be used)
	 * 
	 * @return componentId as String - first componentId specified in configuration if more than one is configured in the
	 *         ApplicationContext. <code>null</code> if no componentId is specified
	 * @see ComponentIdHolder
	 */
	private String retrieveComponentId() {
		String componentId = null;
		ApplicationContext applicationContext = getApplicationContext();
		
		if (applicationContext == null) {
			// No applicationContext means that this filter hasn't been configured in a spring app applicationContext
			LOGGER.error("AbstractInitRequestContextInterceptor implementation hasn't been set up as a Spring component."
					+ " It's most likely misconfigured. As a result the ComponentId will be set to NULL");
		} else {

			// Get the ComponentId configured somewhere in the applicationContext
			Map beanNameComponentIdHolderPairs = applicationContext.getBeansOfType(ComponentIdHolder.class, false, true);
			// ComponentId should ALWAYS be configured
			if (beanNameComponentIdHolderPairs == null || beanNameComponentIdHolderPairs.values().size() == 0) {
				LOGGER.error("No ComponentIdHolder bean  defined in the ApplicationContext."
						+ " The ComponentId hasn't been configured. <null> value will be used.");
			} else {
				// ApplicationId should only be configured once
				int numberOfComponentIds = beanNameComponentIdHolderPairs.values().size();
				if (numberOfComponentIds > 1) {
					LOGGER.warn("Duplicate [#" + numberOfComponentIds + "] ComponentIdHolder has been specified. "
							+ "Will be using the first entry in the ApplicationContext. "
							+ "Make sure only one ComponentIdHolder is configured per configuration module");
				}
				componentId = ((ComponentIdHolder) beanNameComponentIdHolderPairs.values().iterator().next())
						.getComponentId();
				LOGGER.debug("Component Id set to " + componentId);
			}
		}
		return componentId;
	}
}
