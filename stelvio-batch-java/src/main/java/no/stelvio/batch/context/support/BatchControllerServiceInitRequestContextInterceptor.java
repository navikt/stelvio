package no.stelvio.batch.context.support;

import no.stelvio.batch.controller.support.DefaultBatchControllerService;
import no.stelvio.batch.exception.BatchFunctionalException;
import no.stelvio.common.context.support.AbstractInitRequestContextInterceptor;

import org.aopalliance.intercept.MethodInvocation;

/**
 * AbstractInitRequestContextInterceptor implementation to be used by Batch jobs. Batch jobs need to have the thread-bound
 * RequestContext in RequestContextHolder initialized. Batch jobs are typically executed by components that are unable to
 * initialize the thread-bound RequestContext.
 * 
 * @see AbstractInitRequestContextInterceptor
 */
public class BatchControllerServiceInitRequestContextInterceptor extends AbstractInitRequestContextInterceptor {

	/**
	 * Retrieves the userId from the batchNameMap from DefaultBatchControllerService
	 * 
	 * @param invocation method invocation
	 * @return userId as String
	 */
	@Override
	protected String retrieveUserId(MethodInvocation invocation) {
		Object obj = invocation.getThis();
		if (obj instanceof DefaultBatchControllerService) {
			DefaultBatchControllerService batchControllerService = (DefaultBatchControllerService) obj;
			String batchName = (String) invocation.getArguments()[0];
			
			return (String) batchControllerService.getBatchNameMap().get(batchName);
		} else {
			// Intercepted call to an object that is not an instance of DefaultBatchControllerService.
			// This interceptor should ONLY intercept DefaultBatchControllerService instances.
			throw new BatchFunctionalException(this.getClass() + "must be set up to intercept an instance of DefaultBatchControllerService");
		}
	}

}
