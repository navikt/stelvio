package no.stelvio.common.interceptor;

import com.ibm.websphere.sca.scdl.OperationType;

/**
 * This interface can be implemented by classes that want to intercept calls through SCA service components.
 * 
 * @author test@example.com
 */
public interface Interceptor {
	Object doIntercept(OperationType operationType, Object input, InterceptorChain interceptorChain);
}
