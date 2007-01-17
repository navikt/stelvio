package no.stelvio.common.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.transferobject.ServiceRequest;

/**
 * Advice for intercepting calls from remote applications in order to store RequestContext on the local thread before
 * proceeding.
 * 
 * Assumption: the intercepted method's first argument must be of type BusinessRequest.
 * 
 * @author personff564022aedd
 * @version $Id$
 */
public class RequestContextHandlerAdvice implements MethodInterceptor {

	public Object invoke(MethodInvocation i) throws Throwable {
		
		Object[] args = i.getArguments();
		if (args.length > 0 && args[0] instanceof ServiceRequest) {
			RequestContext requestContext = ((ServiceRequest)args[0]).getRequestContext();
			
			if (requestContext != null) {
				RequestContextHolder.setRequestContext(requestContext);
			}
		}
		
		return i.proceed();
	}

}
