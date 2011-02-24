package no.stelvio.batch.controller.support;

import no.stelvio.batch.controller.SpringBatchEnabledBatchControllerServiceBi;
import no.stelvio.batch.controller.SpringBatchJobOperator;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;

/**
 * Extension of {@link DefaultBatchControllerService} in order to handle execution of Spring Batches.
 * 
 * @author person47c121e3ccb5, BEKK
 * 
 */
public class SpringBatchEnabledBatchControllerService extends DefaultBatchControllerService implements
		SpringBatchEnabledBatchControllerServiceBi {

	private static Log log = LogFactory.getLog(SpringBatchEnabledBatchControllerService.class);
	private static final String JOB_NAME = "jobName";

	private SpringBatchJobOperator springBatchOperator;

	/** {@inheritDoc} */
	public int executeBatch(String jobName, String parameters) {
		MDC.put(JOB_NAME, jobName != null ? jobName.toLowerCase() : null);
		log.info("Executing batch with jobName=" + jobName + " , and parameters=" + parameters);
		
		RequestContextSetter.setRequestContext(new SimpleRequestContext.Builder().userId(jobName).build());

		int twsCode = springBatchOperator.executeBatch(jobName, parameters);
		MDC.remove(JOB_NAME);

		return twsCode;
	}

	/** {@inheritDoc} */
	public boolean stopBatch(String jobName) {
		MDC.put(JOB_NAME, jobName != null ? jobName.toLowerCase() : null);
		log.info("stopping batch with jobName=" + jobName);
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
