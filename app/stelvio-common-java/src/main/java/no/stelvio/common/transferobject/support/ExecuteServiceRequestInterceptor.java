package no.stelvio.common.transferobject.support;

import java.lang.reflect.Field;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.support.ComponentIdHolder;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.transferobject.ServiceRequest;

/**
 * Interceptor for calls from remote applications that copies RequestContext from the ServiceRequest into the
 * ServiceRequestHolder before the method call is executed proceeding.
 * <p/>
 *
 * On the remote client an interceptor that copies the RequestContext from the RequestContextHolder to the ServiceRequest should
 * be configured. This can be achieved by applying the {@link PrepareServiceRequestInterceptor} interceptor.
 *
 * @author personff564022aedd
 * @author person983601e0e117
 * @version $Id$
 */
public class ExecuteServiceRequestInterceptor implements MethodInterceptor, Ordered, ApplicationContextAware {

	private Log log = LogFactory.getLog(ExecuteServiceRequestInterceptor.class);

	private int order = Ordered.HIGHEST_PRECEDENCE;

	private ApplicationContext applicationContext;

	/**
	 * Specifies whether to look for componentId in appCtx and override the one passed from the calling system if one is present
	 * in appCtx.
	 * */
	private boolean overideComponentId = false;

	/**
	 * Method used to intercept service calls, retrieve RequestContext and set it on thread.
	 *
	 * @param i
	 *            MethodInvocation
	 * @return the value returned by the method that is intercepted
	 * @throws Throwable
	 *             a throwable exception
	 */
	public Object invoke(MethodInvocation i) throws Throwable {
		if (log.isDebugEnabled()) {
			log.debug("Method invoke(MethodInvocation) has been invoced");
		}
		Object[] args = i.getArguments();

		if (log.isTraceEnabled()) {
			log.trace("Traversing method parameters");
		}
		for (Object arg : args) {
			if (log.isTraceEnabled()) {
				log.trace("Found method argument: " + arg);
			}
			if (arg instanceof ServiceRequest) {
				if (log.isTraceEnabled()) {
					log.trace("Argument was of type ServiceRequest: " + arg.getClass().getSimpleName());
				}

				RequestContext requestContext = getRequestContext(arg);

				if (requestContext == null) {
					if (log.isWarnEnabled()) {
						log.warn(String.format(
								"RequestContext in ServiceRequest was null. Something was likely wrong with the Service call "
										+ "[%s:%s]", i.getThis().getClass().getSimpleName(), i.getMethod().getName()));
					}
					requestContext = new SimpleRequestContext.Builder()
											.userId("unknown-userId")
											.componentId("unknown-componentId")
											.transactionId("unknown-transactionId")
											.moduleId("unknown-moduleId")
											.processId("unknown-processId")
											.screenId("unknown-screenId")
											.build();
				}

				if (overideComponentId) {
					if (log.isTraceEnabled()) {
						log.trace("ComponentId passed by ServiceRequest is set to be overridden if possible");
					}
					requestContext = overrideComponentIdIfPresentInConfiguration(requestContext);
				}

				RequestContextSetter.setRequestContext(requestContext); // Set requestContext on thread
				if (log.isDebugEnabled()) {
					log.debug("RequestContext was retrieved from ServiceRequest and bound to thread");
				}
				break; // Should not be more than one service request per call
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Method invoke is about to proceed on MethodInvocation");
		}
		Object ret;
		try{
			ret =  i.proceed();
		}finally{
			RequestContextSetter.resetRequestContext();
		}

		if (log.isDebugEnabled()) {
			log.debug("Method invoke is about to exit from MethodInvocation");
		}
		return ret;
	}

	/**
	 * Method for retrieving a {@link RequestContext} from a {@link ServiceRequest}.
	 *
	 * @param serviceRequest
	 *            a service request
	 * @return {@link RequestContext}
	 * @throws NoSuchFieldException
	 *             if the method tries to access a field that does not exist
	 * @throws IllegalAccessException
	 *             if the method does not have access
	 */
	private RequestContext getRequestContext(Object serviceRequest) throws NoSuchFieldException, IllegalAccessException {
		Field requestContextField = ServiceRequest.class.getDeclaredField("requestContext");
		ReflectionUtils.makeAccessible(requestContextField);
		return (RequestContext) requestContextField.get(serviceRequest);
	}

	/**
	 * Method that overrides the ComponentId currently in the RequestContext if one is configured in the ApplicationContext of
	 * this Interceptor.
	 *
	 * @param requestContext
	 *            a request context
	 * @return a request context. If the componentId is null then the return context is a new SimpleRequestContext, otherwise
	 *         its the input request context.
	 */
	private RequestContext overrideComponentIdIfPresentInConfiguration(RequestContext requestContext) {
		String componentId = retrieveComponentId();
		if (componentId != null) {
			return new SimpleRequestContext(requestContext.getScreenId(), requestContext.getModuleId(), requestContext
					.getTransactionId(), componentId, requestContext.getUserId());
		}
		return requestContext;

	}

	/**
	 * Retrieves the componentId specified in the ApplicationContext.
	 *
	 * @return componentId or <code>null</code> if none is configured
	 */
	private String retrieveComponentId() {
		String componentId = null;

		if (applicationContext != null) {

			// Get the ComponentId configured somewhere in the applicationContext
			Map<?, ?> beanNameComponentIdHolderPairs = applicationContext.getBeansOfType(ComponentIdHolder.class, false, true);
			// ComponentId should ALWAYS be configured
			if (beanNameComponentIdHolderPairs == null || beanNameComponentIdHolderPairs.values().isEmpty()) {
				log.error("No ComponentIdHolder bean  defined in the ApplicationContext."
						+ " The ComponentId hasn't been configured. Value from calling system will be used.");
			} else {
				// ApplicationId should only be configured once
				int numberOfComponentIds = beanNameComponentIdHolderPairs.values().size();
				if (numberOfComponentIds > 1) {
					log.warn("Duplicate [#" + numberOfComponentIds + "] ComponentIdHolder has been specified. "
							+ "Will be using the first entry in the ApplicationContext. "
							+ "Make sure only one ComponentIdHolder is configured per configuration module");
				}
				componentId = ((ComponentIdHolder) beanNameComponentIdHolderPairs.values().iterator().next())
						.getComponentId();
				if (log.isDebugEnabled()) {
					log.debug("Application Id set to " + componentId);
				}
			}
		}
		return componentId;
	}

	/**
	 * Gets the order of this interceptor.
	 *
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Sets the order of this interceptor.
	 *
	 * @param order the order
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Sets the application context.
	 *
	 * @param applicationContext
	 *            the application context
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * Specifies whether this Interceptor should look for a ComponentId in the ApplicationContext and override the one passed
	 * through the ServiceRequest. Will only override the one in the ServiceRequest if this property is true AND a componentId
	 * has been set up in the applicationContext.
	 *
	 * overrideComponentId defaults to false
	 *
	 * @param overideComponentId
	 *            the override component id
	 */
	public void setOverideComponentId(boolean overideComponentId) {
		this.overideComponentId = overideComponentId;
	}
}
