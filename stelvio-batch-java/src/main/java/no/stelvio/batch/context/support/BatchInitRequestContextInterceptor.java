package no.stelvio.batch.context.support;

import no.stelvio.batch.BatchBi;
import no.stelvio.batch.exception.BatchFunctionalException;
import no.stelvio.common.context.support.AbstractInitRequestContextInterceptor;

import org.aopalliance.intercept.MethodInvocation;

/**
 * AbstractInitRequestContextInterceptor implementation to be used by Batch jobs. Batch jobs need to have the thread-bound
 * RequestContext in RequestContextHolder initialized. Batch jobs are typically executed by components that are unable to
 * initialize the thread-bound RequestContext.
 * 
 * This implementation sets up the batchName available through {@link BatchBi#getBatchName()} as userId
 * 
 * @see AbstractInitRequestContextInterceptor
 */
public class BatchInitRequestContextInterceptor extends AbstractInitRequestContextInterceptor {

	/**
	 * Retrieves the userId. uses the BatchName set up in the BatchBi implementation as userID
	 * 
	 * @param invocation method invocation
	 * @return userId as String
	 */
	@Override
	protected String retrieveUserId(MethodInvocation invocation) {
		Object obj = invocation.getThis();
		if (obj instanceof BatchBi) {
			BatchBi batch = (BatchBi) obj;
			return batch.getBatchName();
		} else {
			// Intercepted call to an object that is not an instance of BatchBi.
			// This interceptor should ONLY intercept BatchBi instances.
			throw new BatchFunctionalException(this.getClass() + "must be set up to intercept an instance of BatchBi");
		}
	}

}
