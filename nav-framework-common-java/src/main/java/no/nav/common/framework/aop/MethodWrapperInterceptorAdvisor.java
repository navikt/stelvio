package no.nav.common.framework.aop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.UnknownAdviceTypeException;
import org.springframework.aop.support.AbstractRegexpMethodPointcut;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Dynamically wraps invocations.
 *
 * Areas for improvement (see <code>ProxyFactoryBean</code>):
 * - handle a target (TargetSource) to be called as the last in the interceptor chain
 * - handle global advisors or interceptors
 * - handle singleton/prototpye
 * - refresh advisors
 * - have a setter for advisorAdapterRegistry
 * - use AopProxyFactory().createAopProxy(this); for creating the proxy
 *
 * @author personf8e9850ed756
 * @version $Revision: 2788 $, $Date: 2006-02-28 15:41:06 +0100 (Tue, 28 Feb 2006) $
 * @todo javadoc
 * @todo a lot of the code is copied from <code>ProxyFactoryBean</code> --> refactor
 * @todo should we extend AdvisedSupport? A lot there we don't use just to have addAdvisor (++?)
 * @see org.springframework.aop.framework.ProxyFactoryBean
 */
public class MethodWrapperInterceptorAdvisor extends AdvisedSupport
		implements MethodInterceptor, PointcutAdvisor, BeanFactoryAware {
	private static final Log log = LogFactory.getLog(MethodWrapperInterceptorAdvisor.class);
	private AbstractRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
	private String[] interceptorNames;
	private BeanFactory beanFactory;
	private AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		final Object result = methodInvocation.proceed();
		final ProxyFactory proxyFactory = new ProxyFactory(result);

		//  TODO use AopProxyFactory().createAopProxy(this); for creating the proxy; see ProxyFactoryBean
		final Advisor[] advisors = getAdvisors();
		for (int i = 0; i < advisors.length; i++) {
			Advisor advisor = advisors[i];
			proxyFactory.addAdvisor(advisor);
		}

		return proxyFactory.getProxy();
	}

	public boolean isPerInstance() {
		// TODO: what does this mean?
		return false;
	}

	public Advice getAdvice() {
		return this;
	}

	public Pointcut getPointcut() {
		return pointcut;
	}

	public void setPattern(final String methodNamePattern) {
		pointcut.setPattern(methodNamePattern);
	}

	public void setPatterns(final String[] patterns) {
		pointcut.setPatterns(patterns);
	}

	public void setInterceptorNames(final String[] interceptorNames) {
		this.interceptorNames = (String[]) interceptorNames.clone();
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
		createAdvisorChain();
	}

	/**
	 * Create the advisor (interceptor) chain. Aadvisors that are sourced
	 * from a BeanFactory will be refreshed each time a new prototype instance
	 * is added. Interceptors added programmatically through the factory API
	 * are unaffected by such changes.
	 */
	private void createAdvisorChain() throws AopConfigException, BeansException {
		if (this.interceptorNames == null || this.interceptorNames.length == 0) {
			return;
		}

		/* TODO implement this?
		        // Globals can't be last unless we specified a targetSource using the property...
		        if (this.interceptorNames[this.interceptorNames.length - 1].endsWith(GLOBAL_SUFFIX) &&
		            this.targetSource == EMPTY_TARGET_SOURCE)
		        {
		            throw new AopConfigException("Target required after globals");
		        }
		*/

		// materialize interceptor chain from bean names
		for (int i = 0; i < this.interceptorNames.length; i++) {
			final String name = this.interceptorNames[i];

			if (log.isDebugEnabled()) {
				log.debug("Configuring advisor or advice '" + name + "'");
			}

			/* TODO implement this?
			        if (i == interceptorNames.length - 1)
			         {
			             // The last name in the chain may be an Advisor/Advice or a target/TargetSource.
			             // Unfortunately we don't know; we must look at type of the bean.
			            if (name.endsWith(GLOBAL_SUFFIX))
			            {
			                if (!(this.beanFactory instanceof ListableBeanFactory))
			                {
			                    throw new AopConfigException(
			                    "Can only use global advisors or interceptors with a ListableBeanFactory");
			                }
			                addGlobalAdvisor((ListableBeanFactory) this.beanFactory,
			                                 name.substring(0, name.length() - GLOBAL_SUFFIX.length()));
			                continue;
			            }
			            else if (i == this.interceptorNames.length - 1 &&
			                     this.targetName == null && this.targetSource == EMPTY_TARGET_SOURCE)
			            {
			                    // Must be an interceptor.
			                    this.targetName = name;
			                    if (log.isDebugEnabled())
			                    {
			                        log.debug("Bean with name '" + name +
			                                     "' concluding interceptor chain is not an advisor class: " +
			                                     "treating it as a target or TargetSource");
			                    }
			                    continue;
			                }
			                // If it IS an advice, or we can't tell, fall through and treat it as an advice...
			         }
			
			            // If we get here, we need to add a named interceptor.
			            // We must check if it's a singleton or prototype.
			            Object advice = null;
			            if (isSingleton() || this.beanFactory.isSingleton(name))
			            {
			                // Add the real Advisor/Advice to the chain.
			                advice = this.beanFactory.getBean(name);
			            }
			            else
			            {
			                // It's a prototype Advice or Advisor: replace with a prototype.
			                // Avoid unnecessary creation of prototype bean just for advisor chain initialization.
			                advice = new PrototypePlaceholderAdvisor(name);
			            }
			*/
			addAdvisorOnChainCreation(beanFactory.getBean(name), name);
		}
	}

	/**
	 * Invoked when advice chain is created.
	 * <p>Add the given advice, advisor or object to the interceptor list.
	 * Because of these three possibilities, we can't type the signature
	 * more strongly.
	 *
	 * @param next advice, advisor or target object
	 * @param name bean name from which we obtained this object in our owning
	 *             bean factory
	 */
	private void addAdvisorOnChainCreation(Object next, String name) {
		if (log.isDebugEnabled()) {
			log.debug("Adding advisor or TargetSource [" + next + "] with name [" + name + "]");
		}

		// We need to convert to an Advisor if necessary so that our source reference
		// matches what we find from superclass interceptors.
		Advisor advisor = namedBeanToAdvisor(next);

		// If it wasn't just updating the TargetSource.
		if (log.isDebugEnabled()) {
			log.debug("Adding advisor with name [" + name + "]");
		}

		addAdvisor(advisor);
	}

	/**
	 * Convert the following object sourced from calling getBean() on a name in the
	 * interceptorNames array to an Advisor or TargetSource.
	 */
	private Advisor namedBeanToAdvisor(Object next) {
		try {
			return this.advisorAdapterRegistry.wrap(next);
		} catch (UnknownAdviceTypeException ex) {
			// We expected this to be an Advisor or Advice,
			// but it wasn't. This is a configuration error.
			throw new AopConfigException(
				"Unknown advisor type "
					+ next.getClass()
					+ "; Can only include Advisor or Advice type beans in interceptorNames chain except for last entry,"
					+ "which may also be target or TargetSource",
				ex);
		}
	}

}
