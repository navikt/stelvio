package no.stelvio.common.context.support;

import java.util.Map;
import java.util.UUID;

import no.stelvio.common.context.RequestContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Abstract class used as a base class for interceptors responsible for initializing the RequestContext. For web applications,
 * the RequestContextFilter should be used to initialize the RequestContext. Subclasses of this interceptor should only be used
 * to initialize the RequestContext in business components where the RequestContext is not passed as part of the business
 * component invocation.
 * 
 * <p>
 * The retrieveUserId must be implemented to return a userId. The rest of the retrieve-methods have a default implementation,
 * however they may be overriden and used as hooks for implementations that require special processing of the different
 * RequestContext parameters.
 * </p>
 * 
 * <p>
 * The hooks with their default behavior:
 * <ul>
 * <li>{@link #retrieveComponentId()} - retrieves componentId from Spring context</li>
 * <li>{@link #retrieveModuleId()} - defaults to <code>null</code></li>
 * <li>{@link #retrieveScreenId()} - defaults to <code>null</code></li>
 * <li>{@link #retrieveTransactionId()} - creates transactionId using UUID</li>
 * </ul>
 * </p>
 * 
 * @see no.stelvio.presentation.context.RequestContextFilter sets the requestcontext for web applications
 * @see no.stelvio.batch.context.support.BatchInitRequestContextInterceptor
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public abstract class AbstractInitRequestContextInterceptor implements MethodInterceptor, ApplicationContextAware {

	private ApplicationContext applicationContext;
	private static final Log LOG = LogFactory.getLog(AbstractInitRequestContextInterceptor.class);

	/**
	 * Sets the thread-bound RequestContext.
	 * 
	 * @param invocation
	 *            invocation
	 * @return invocation.proceed()
	 * @throws Throwable
	 *             any exception
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {

		// Call hooks
		String componentId = retrieveComponentId();
		String screenId = retrieveScreenId();
		String userId = retrieveUserId(invocation);
		String transactionId = retrieveTransactionId();
		String moduleId = retrieveModuleId();

		// Construct RequestContext using values retrieved from hook-methods
		RequestContext reqCtx = new SimpleRequestContext(screenId, moduleId, transactionId, componentId, userId);

		// Set RequestContext on RequestcontextHolder
		RequestContextSetter.setRequestContext(reqCtx);

		LOG.info("RequestContext was initialized and bound to RequestContextHolder. RequestContext: " + reqCtx);

		return invocation.proceed();
	}

	/**
	 * Method called by {@link #invoke(MethodInvocation)} to retrieve the user id to be set on the thread-bound RequestContext.
	 * 
	 * @param invocation
	 *            invocation
	 * @return userId
	 */
	protected abstract String retrieveUserId(MethodInvocation invocation);

	/**
	 * Goes through the ApplicationContext to find the ComponentIdHolder that sets the ComponentId.
	 * 
	 * Logs error if no ComponentId has been specified. Logs warn if more than one ComponentId has been specified
	 * 
	 * If more than one componentId is configured in the ApplicationContext, the first will be used. If more than one is
	 * configured with the same name (or no name), the result is unspecified (the last one will probably be used)
	 * 
	 * @return componentId as String - first componentId specified in configuration if more than one is configured in the
	 *         ApplicationContext. <code>null</code> if no componentId is specified
	 * @see ComponentIdHolder
	 */
	protected String retrieveComponentId() {
		String componentId = null;

		if (applicationContext == null) {
			// No applicationContext means that this filter hasn't been configured in a spring app applicationContext
			LOG.error("AbstractInitRequestContextInterceptor implementation hasn't been set up as a Spring component."
					+ " It's most likely misconfigured. As a result the ComponentId will be set to NULL");
		} else {

			// Get the ComponentId configured somewhere in the applicationContext
			Map beanNameComponentIdHolderPairs = applicationContext.getBeansOfType(ComponentIdHolder.class, false, true);
			// ComponentId should ALWAYS be configured
			if (beanNameComponentIdHolderPairs == null || beanNameComponentIdHolderPairs.values().size() == 0) {
				LOG.error("No ComponentIdHolder bean  defined in the ApplicationContext."
						+ " The ComponentId hasn't been configured. <null> value will be used.");
			} else {
				// ApplicationId should only be configured once
				int numberOfComponentIds = beanNameComponentIdHolderPairs.values().size();
				if (numberOfComponentIds > 1) {
					LOG.warn("Duplicate [#" + numberOfComponentIds + "] ComponentIdHolder has been specified. "
							+ "Will be using the first entry in the ApplicationContext. "
							+ "Make sure only one ComponentIdHolder is configured per configuration module");
				}
				componentId = (String) ((ComponentIdHolder) beanNameComponentIdHolderPairs.values().iterator().next())
						.getComponentId();
				LOG.debug("Component Id set to " + componentId);
			}
		}
		return componentId;
	}

	/**
	 * Provides a transactionId using UUID.randomUUID().
	 * 
	 * @return transactionId
	 */
	protected String retrieveTransactionId() {
		return String.valueOf(UUID.randomUUID());
	}

	/**
	 * Provides a screenId. Will always return <code>null</code>, must be overridden to change this behavior.
	 * 
	 * @return null
	 */
	protected String retrieveScreenId() {
		return null;
	}

	/**
	 * Provides a moduleId. Will always return <code>null</code>, must be overridden to change this behavior.
	 * 
	 * @return null
	 */
	protected String retrieveModuleId() {
		return null;
	}

	/**
	 * Sets the ApplicationContext, called by the Spring container.
	 * 
	 * @see ApplicationContextAware
	 * 
	 * @param ctx
	 *            context
	 * @throws BeansException
	 *             beans exception
	 */
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.applicationContext = ctx;
	}

	/**
	 * Gets the ApplicationContext.
	 * 
	 * @see #setApplicationContext(ApplicationContext)
	 * @return ApplicationContext
	 */
	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
