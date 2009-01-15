package no.stelvio.common.interceptor;

import java.util.Collection;
import java.util.Iterator;

import com.ibm.websphere.sca.Service;
import com.ibm.websphere.sca.scdl.OperationType;

/**
 * Default interceptor chain implementation.
 * 
 * @author test@example.com
 */
public class InterceptorChainImpl implements InterceptorChain {
	private Iterator<? extends Interceptor> interceptors;
	private Service partnerService;

	public InterceptorChainImpl(Collection<? extends Interceptor> interceptors, Service partnerService) {
		this.interceptors = interceptors.iterator();
		this.partnerService = partnerService;
	}

	public Object doIntercept(OperationType operationType, Object input) {
		if (interceptors.hasNext()) {
			return interceptors.next().doIntercept(operationType, input, this);
		} else {
			return partnerService.invoke(operationType, input);
		}
	}
}
