package no.stelvio.common.framework.cache;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;

import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.error.SystemException;

/**
 * Caches results of method invocations.
 *
 * @author personf8e9850ed756
 * @version $Revision: $, $Date: $
 */
public class MethodCacheInterceptor extends RegexpMethodPointcutAdvisor implements MethodInterceptor {
	private Cache cache;

	/**
	 * Called when an intercepted method is called.
	 *
	 * @param methodInvocation holds state regarding the invocation of the method.
	 * @return the result from the method invocation, might be a cached result.
	 * @throws Throwable if something went wrong calling the method.
	 */
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		final String cacheKey = cacheKey(methodInvocation);
		Object result = cache.get(cacheKey);

		if (null == result) {
			if (logger.isDebugEnabled()) {
				logger.debug("Nothing cached for key '" + cacheKey + "'; caching result from method call");
			}

			result = methodInvocation.proceed();
			cache.put(cacheKey, result);
		} else if (logger.isDebugEnabled()) {
			logger.debug("Item is present in cache for key: " + cacheKey);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("result retrieved: " + result);
		}

		return result;
	}

	/**
	 * The advice to use; in this case, it is the interceptor itself.
	 *
	 * @return the advice to use.
	 */
	public Advice getAdvice() {
		return this;
	}

	/**
	 * Checks whether the instance is setup correctly. This method should be set as an init method in the Spring setup.
	 */
	public void init() {
		if (null == cache) {
			throw new SystemException(FrameworkError.SERVICE_INIT_ERROR, "MethodCacheInterceptor");
		}
	}

	/**
	 * A <code>String</code> representation of the interceptor.
	 *
	 * @return A <code>String</code> representation of the interceptor.
	 */
	public String toString() {
		return "MethodCacheInterceptor: pointcut [" + getPointcut() + "]";
	}

	/**
	 * Creates the key to be used to lookup/save the result from the invocation of the method.
	 *
	 * @param methodInvocation holds state regarding the invocation of the method.
	 * @return the key to be used to lookup/save the result from the invocation of the method.
	 */
	private String cacheKey(final MethodInvocation methodInvocation) {
		final String className = methodInvocation.getMethod().getDeclaringClass().getName();
		final String methodName = methodInvocation.getMethod().getName();
		final String argumentsHash = hashOf(methodInvocation.getArguments());

		return className + '.' + methodName + '.' + argumentsHash;
	}

	/**
	 * Calculates the sum of the hash codes for each object in the array.
	 *
	 * @param objects objects to calculate hash codes for.
	 * @return the sum of the hash codes for each object in the array.
	 */
	private String hashOf(final Object[] objects) {
		final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();

		for (int idx = 0; idx < objects.length; idx++) {
			Object object = objects[idx];
			hashCodeBuilder.append(object.hashCode());
		}

		return String.valueOf(hashCodeBuilder.toHashCode());
	}

	/**
	 * Sets the <code>Cache</code> implementation that should be used.
	 *
	 * @param cache
	 * @see Cache
	 */
	public void setCache(final Cache cache) {
		this.cache = cache;
	}
}
