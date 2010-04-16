package no.stelvio.common.context;

import java.util.Collection;
import java.util.Collections;

import no.stelvio.common.interceptor.Interceptor;
import no.stelvio.common.interceptor.InterceptorChain;
import no.stelvio.common.interceptor.InterceptorService;
import no.stelvio.common.interceptor.InterceptorServiceContext;

import com.ibm.websphere.sca.scdl.OperationType;

/**
 * This class can be used as a base class for components that wants to create (or update) StelvioContext.
 * 
 * When subclassing, the referenced methods will have to be overridden with default behavior (call to the corresponding method
 * in the superclass). Example: <code>
 * @Override
 *	public Object invoke(OperationType operationType, Object input) throws ServiceBusinessException {
 *		return super.invoke(operationType, input);
 *	}
 * </code>
 * 
 * @see com.ibm.websphere.sca.ServiceImplSync#invoke(OperationType, Object)
 * @see com.ibm.websphere.sca.ServiceImplAsync#invokeAsync(OperationType, Object, com.ibm.websphere.sca.ServiceCallback,
 *      com.ibm.websphere.sca.Ticket)
 * @see com.ibm.websphere.sca.ServiceCallback#onInvokeResponse(com.ibm.websphere.sca.Ticket, Object, Exception)
 * @author test@example.com
 */
public abstract class SetStelvioContextComponent extends InterceptorService implements Interceptor {
	@Override
	protected Collection<? extends Interceptor> buildInterceptors(InterceptorServiceContext context) {
		return Collections.singletonList(this);
	}

	public final Object doIntercept(OperationType operationType, Object input, InterceptorChain interceptorChain) {
		try {
			StelvioContextRepository.createOrUpdateContext(getContextData());
			return interceptorChain.doIntercept(operationType, input);
		} finally {
			StelvioContextRepository.removeContext();
		}
	}

	protected abstract StelvioContextData getContextData();
}
