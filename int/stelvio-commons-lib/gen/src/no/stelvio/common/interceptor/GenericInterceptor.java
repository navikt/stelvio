package no.stelvio.common.interceptor;

import java.util.logging.Logger;

import com.ibm.websphere.sca.scdl.OperationType;

public abstract class GenericInterceptor implements Interceptor {
	protected final String className = getClass().getName();
	protected final Logger logger = Logger.getLogger(className);

	private boolean enabled = true;

	protected abstract Object doInterceptInternal(OperationType operationType, Object input, InterceptorChain interceptorChain);

	public final Object doIntercept(OperationType operationType, Object input, InterceptorChain interceptorChain) {
		if (isEnabled()) {
			return doInterceptInternal(operationType, input, interceptorChain);
		} else {
			return interceptorChain.doIntercept(operationType, input);
		}
	}

	protected void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}