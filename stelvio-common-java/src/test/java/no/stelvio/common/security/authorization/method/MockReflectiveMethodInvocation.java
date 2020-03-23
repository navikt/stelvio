package no.stelvio.common.security.authorization.method;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.aop.framework.ReflectiveMethodInvocation;

/**
 * Mock class for testing in order to call the constructor in ReflectiveMethodInvocation.
 * 
 *
 */
public class MockReflectiveMethodInvocation extends ReflectiveMethodInvocation {

	/**
	 * Creates a new instance of MockReflectiveMethodInvocation.
	 * 
	 * @param proxy
	 *            proxy
	 * @param target
	 *            target
	 * @param method
	 *            method
	 * @param arguments
	 *            arguments
	 * @param targetClass
	 *            target class
	 * @param interceptorsAndDynamicMethodMatchers
	 *            interceptors and dynamic method matchers
	 */
	public MockReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class targetClass,
			List interceptorsAndDynamicMethodMatchers) {
		super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
	}

}
