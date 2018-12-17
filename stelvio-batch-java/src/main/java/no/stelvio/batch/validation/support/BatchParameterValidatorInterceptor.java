package no.stelvio.batch.validation.support;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import no.stelvio.batch.BatchBi;
import no.stelvio.batch.exception.BatchFunctionalException;

/**
 * Validated batches before they start executing.
 * 
 * @author persondfa1fa919e87(Accenture)
 */
public class BatchParameterValidatorInterceptor implements MethodInterceptor {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of BatchParameterValidatorInterceptor.
	 */
	public BatchParameterValidatorInterceptor() {
	}

	/**
	 * Invoked before a batch starts.
	 *
	 * @param invocation the method invocation
	 * @return object
	 * @throws Throwable if invocation fails
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object obj = invocation.getThis();
		if (obj instanceof BatchBi) {
			new BatchParameterValidator((BatchBi) obj).validate();
		} else {
			// Intercepted call to an object that is not an instance of BatchBi.
			// This interceptor should ONLY intercept BatchBi instances.
			throw new BatchFunctionalException(this.getClass() + "must be set up to intercept an instance of BatchBi");
		}

		return invocation.proceed();
	}
}