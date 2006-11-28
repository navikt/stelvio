package no.stelvio.common.aop;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.transferobject.ServiceRequest;
import no.stelvio.common.transferobject.ContextContainer;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Advice for intercepting calls from remote applications in order to store 
 * RequestContext on the local thread before proceeding.  
 * 
 * Assumption: the intercepted method's first argument must be of type BusinessRequest.
 * 
 * @author personff564022aedd
 */
public class ContextHandlerAdvice implements MethodInterceptor {

	public Object invoke(MethodInvocation i) throws Throwable {
		
		Object[] args = i.getArguments();
		if (args.length > 0 && args[0] instanceof ServiceRequest) {
			ContextContainer cc = ((ServiceRequest)args[0]).getContextContainer();
			
			if (cc != null) {
				RequestContext.importContextValues(cc);
			}
		}
		
		return i.proceed();
	}

}
