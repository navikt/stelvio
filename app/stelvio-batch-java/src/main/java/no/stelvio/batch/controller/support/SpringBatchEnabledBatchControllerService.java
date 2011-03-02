package no.stelvio.batch.controller.support;

import no.stelvio.batch.controller.SpringBatchEnabledBatchControllerServiceBi;
import no.stelvio.batch.controller.SpringBatchJobOperator;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.MDC;

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

	/** {@inheritDoc} */
	public int executeBatch(String jobName, String parameters) {
		if (jobName != null) {
			MDC.put(JOB_NAME, jobName.toLowerCase());
		}
		LOGGER.info("Executing batch with jobName=" + jobName + " , and parameters=" + parameters);

		RequestContextSetter.setRequestContext(new SimpleRequestContext.Builder().userId(jobName).build());

		int twsCode = springBatchOperator.executeBatch(jobName, parameters);
		MDC.remove(JOB_NAME);

		return twsCode;
	}

	/** {@inheritDoc} */
	public boolean stopBatch(String jobName) {
		if (jobName != null) {
			MDC.put(JOB_NAME, jobName.toLowerCase());
		}
		LOGGER.info("stopping batch with jobName=" + jobName);
		boolean stopped = springBatchOperator.stopBatch(jobName);
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

}
