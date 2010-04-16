package no.stelvio.common.interceptor;

import com.ibm.websphere.sca.scdl.OperationType;

/**
 * This interface is implemented by classes defining a chain of SCA service component interceptors.
 * 
 * @author test@example.com
 */
public interface InterceptorChain {
	Object doIntercept(OperationType operationType, Object input);
}
