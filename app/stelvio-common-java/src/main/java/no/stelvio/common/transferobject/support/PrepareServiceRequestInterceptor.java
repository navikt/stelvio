package no.stelvio.common.transferobject.support;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.ejb.access.StelvioRemoteStatelessSessionProxyFactoryBean;
import no.stelvio.common.transferobject.ServiceRequest;
import no.stelvio.common.util.ReflectUtil;

/**
 * Intercepts calls on the client side of a remote call that passes an
 * ServiceRequest as an argument. The interceptor copies RequestContext from
 * RequestContextHolder puts it in the ServiceRequest that is being passed to
 * the remote resource.
 * <p>
 * On the remote resource an interceptor that copies the RequestContext from the
 * ServiceRequest to the RequestContextHolder should be configured. This can be
 * achieved by applying the {@link ExecuteServiceRequestInterceptor}
 * interceptor.
 * </p>
 * <p>
 * This interceptor can be used both in a true AOP context, and by the stelvio
 * component {@link StelvioRemoteStatelessSessionProxyFactoryBean}. The
 * configuration of this interceptor is however slightly different in the two
 * use cases. See <code>{@link #setExecutedInAopContext(boolean)}</code>} for a
 * detailed description of the different configurations
 * </p>
 *
 * @author person983601e0e117 (Accenture)
 * @see RequestContextHolder
 * @see ServiceRequest
 */
public class PrepareServiceRequestInterceptor implements MethodInterceptor, Ordered {

    private static Log log = LogFactory.getLog(PrepareServiceRequestInterceptor.class);

    private int order = Integer.MAX_VALUE;

    private boolean executedInAopContext = false;

    /**
     * Retrieves the RequestContext from the client's thread and puts it inside
     * the <code>{@link ServiceRequest}</code>.
     * This method will by default return <code>null</code> as it's not part of
     * a AOP interceptor chain when used by
     * {@link StelvioRemoteStatelessSessionProxyFactoryBean}
     *
     * @param invocation MethodInvocation
     * @return value from method <code>{@link #proceed(MethodInvocation)}</code>
     * @throws Throwable a throwable exception
     * @see #proceed(MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] methodArguments = invocation.getArguments();

        RequestContext requestContext = RequestContextHolder.currentRequestContext();

        for (Object arg : methodArguments) { // Loop through method arguments
            if (arg instanceof ServiceRequest) { // Check if argument is of type

                ServiceRequest serviceRequest = (ServiceRequest) arg; // ServiceRequest
                setRequestContext(serviceRequest);
                break; // There shouldn't be more than 1 ServiceRequest per call
            }
        }
        // Delegates the return value to hook
        try {
            return proceed(invocation);
        } finally {
            RequestContextSetter.setRequestContext(requestContext);
        }
    }

    /**
     * Hook method to be implemented by subclasses that want to add custom
     * behavior to this interceptor before continuing execution, or after other
     * interceptors in the chain have executed.
     * <p>
     * The code sample below shows how code can be added before and after the
     * execution of additional aspects/interceptors:
     * <pre>
     * protected Object proceed(MethodInvocation invocation) {
     * 	//Custom business logic, before additional interceptors are executed
     * 	Object objReturnedFromChain = invocation.proceed();
     * 	//Custom business logic, after additional interceptors are executed
     * }
     * </pre>
     * </p>
     * This method will return the result of invocation.proceed() if
     * {@link #isExecutedInAopContext()} is <code>true</code>, otherwise it will
     * return <code>null</code>
     *
     * @param invocation invocation
     * @return <code>null</code>
     * @throws Throwable if exceptions are thrown later in call chain
     * @see #invoke(MethodInvocation)
     */
    protected Object proceed(MethodInvocation invocation) throws Throwable {
        return isExecutedInAopContext() ? invocation.proceed() : null;
    }

    /**
     * Gets <code>{@link RequestContext}</code> from
     * <code>{@link RequestContextHolder}</code> and sets it on the supplied
     * <code>{@link ServiceRequest}</code>.
     *
     * @param serviceRequest to set <code>RequestContext</code> on
     */
    protected void setRequestContext(ServiceRequest serviceRequest) {
        if (serviceRequest != null) {
            log.info("RequestContext is being copied from RequestContextHolder to serviceRequest");
            ReflectUtil.setFieldOnInstance(serviceRequest, "requestContext", RequestContextHolder.currentRequestContext());
            log.info("RequestContext was successfully copied onto ServiceRequest");
        } else {
            log.info("ServiceRequest is null, nothing to copy RequestContext to");
        }
    }

    /**
     * Returns the order of this interceptor when used in an interceptor chain.
     * Defaults to Integer.MAX_VALUE.
     *
     * @return the order
     * @see Ordered
     */
    public int getOrder() {
        return order;
    }

    /**
     * Sets the order of this interceptor when part of an interceptor chain.
     *
     * @param order int specifying order
     * @see Ordered
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Signals whether this interceptor is called in an AOP context and is part
     * of a call chain managed by an AOP framework.
     *
     * @return <code>true</code> if this interceptor is executed by an AOP
     * framework
     * @see #setExecutedInAopContext(boolean)
     */
    public boolean isExecutedInAopContext() {
        return executedInAopContext;
    }

    /**
     * Configures whether this interceptor is called in an AOP context and is
     * part of a call chain managed by an AOP framework. Defaults to
     * <code>false</code>.
     * <p>
     * Usage:
     * <ul>
     * <li>Use <code>true</code> if this interceptor is called by AOP-code (ie:
     * setup by aop-schema in Spring</li>
     * <li>Use <code>false</code> if this interceptor is called by EJB proxy
     * (ie: setup by stelvio-schema</li>
     * </ul>
     * </p>
     *
     * @param executedInAopContext <code>true</code> if this interceptor is executed by an AOP
     * framework
     */
    public void setExecutedInAopContext(boolean executedInAopContext) {
        this.executedInAopContext = executedInAopContext;
    }
}
