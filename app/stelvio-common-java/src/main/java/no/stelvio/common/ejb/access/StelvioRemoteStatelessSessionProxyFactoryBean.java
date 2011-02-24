package no.stelvio.common.ejb.access;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean;

/**
 * Extension of Spring Framework's <code>{@link SimpleRemoteStatelessSessionProxyFactoryBean}</code>. Extends by adding the
 * possibility to have interceptors that are called prior to the EJB proxy.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class StelvioRemoteStatelessSessionProxyFactoryBean extends SimpleRemoteStatelessSessionProxyFactoryBean {

	private List<MethodInterceptor> methodInterceptors = new ArrayList<MethodInterceptor>();

	/**
	 * Executes the invoke-method on all interceptors set by <code>{@link #setMethodInterceptors(List)}</code>, then delegates
	 * call to Spring EJB proxy to perform actual EJB call.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Object doInvoke(MethodInvocation invocation) throws Throwable {
		// For each interceptor configured invoke
		for (MethodInterceptor methodInterceptor : methodInterceptors) {
			if (methodInterceptor != null) {
				methodInterceptor.invoke(invocation);
			}
		}
		// Delegate to Spring super type to do proxy magic
		return super.doInvoke(invocation);
	}

	/**
	 * Sets a list of MethodInterceptor instances.
	 * 
	 * @param methodInterceptors
	 *            list of {@link MethodInterceptor}
	 */
	public void setMethodInterceptors(List<MethodInterceptor> methodInterceptors) {
		this.methodInterceptors = methodInterceptors;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterPropertiesSet() throws NamingException {
		super.afterPropertiesSet();
		// Consider implementing this to check that interceptors are instances of MethodInterceptor
	}

}
