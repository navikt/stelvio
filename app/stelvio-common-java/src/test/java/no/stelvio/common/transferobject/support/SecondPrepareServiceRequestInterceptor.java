package no.stelvio.common.transferobject.support;

import no.stelvio.common.transferobject.ServiceRequest;
import no.stelvio.common.util.ReflectUtil;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * SecondPrepareServiceRequestInterceptor.
 * 
 * @author MA
 * 
 */
public class SecondPrepareServiceRequestInterceptor implements MethodInterceptor {

	/**
	 * This interceptor should be called AFTER the PrepareServiceRequestInterceptor. it's purpose is to verify that the
	 * proceed-implementation in PrepareServiceRequestInterceptor. is implemented to allow execution of additional
	 * interceptors/aspects in an AOP chain.
	 * 
	 * @param invocation the invocation
	 * @return null
	 * @throws Throwable exception
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		ServiceRequest request = (ServiceRequest) invocation.getArguments()[0];
		ReflectUtil.setPropertyOnClass(Boolean.TRUE, request, "secondInterceptorBefore");
		Object obj = invocation.proceed();
		ReflectUtil.setPropertyOnClass(Boolean.TRUE, request, "secondInterceptorAfter");
		return null;
	}

}
