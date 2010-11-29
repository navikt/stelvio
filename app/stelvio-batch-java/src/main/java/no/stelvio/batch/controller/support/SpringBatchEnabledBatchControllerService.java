package no.stelvio.batch.controller.support;

import no.stelvio.batch.controller.SpringBatchEnabledBatchControllerServiceBi;
import no.stelvio.batch.controller.SpringBatchJobOperator;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Extension of {@link DefaultBatchControllerService} in order to handle
 * execution of Spring Batches.
 * 
 * @author person47c121e3ccb5, BEKK
 * 
 */
public class SpringBatchEnabledBatchControllerService extends
		DefaultBatchControllerService implements
		SpringBatchEnabledBatchControllerServiceBi {

	private static Log log = LogFactory
			.getLog(SpringBatchEnabledBatchControllerService.class);

	private SpringBatchJobOperator springBatchOperator;

	/** {@inheritDoc} */
	public int executeBatch(String jobName, String parameters) {
		log.info("Executing batch with jobName=" + jobName
				+ " , and parameters=" + parameters);
		RequestContextSetter
				.setRequestContext(new SimpleRequestContext.Builder().userId(
						jobName).build());
		return springBatchOperator.executeBatch(jobName, parameters);
	}

	/** {@inheritDoc} */
	public boolean stopBatch(String jobName) {
		log.info("stopping batch with jobName=" + jobName);
		return springBatchOperator.stopBatch(jobName);
	}
	
	/**
	 * Sets implementation to use.
	 * @param springBatchOperator springBatchOperator to set.
	 */
	public void setSpringBatchOperator(
			SpringBatchJobOperator springBatchOperator) {
		this.springBatchOperator = springBatchOperator;
	}

}
