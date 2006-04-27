package no.trygdeetaten.integration.framework.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;

import no.trygdeetaten.common.framework.jmx.ServiceLevelConstants;
import no.trygdeetaten.common.framework.jmx.ServiceLevelMBean;
import no.trygdeetaten.common.framework.jmx.ServiceLevelMBeanFactory;
import no.trygdeetaten.common.framework.monitor.MonitorChain;
import no.trygdeetaten.common.framework.monitor.MonitorEvent;
import no.trygdeetaten.common.framework.performance.MonitorKey;
import no.trygdeetaten.common.framework.performance.PerformanceMonitor;
import no.trygdeetaten.common.framework.service.LocalService;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;

/**
 * Abstract class that all implementations of an <i>Integration Service</i> should extend.
 * <p/>
 * Sub classes should be implemented to support concurrent processing by multiple threads.
 * In order for sub classes to be thread safe, they should never:
 * <ul>
 * 		<li> implement class variables to hold state </li>
 * 		<li> implement instance variables to hold state </li>
 * </ul>
 * All references must be passed by value between methods and classes as input and output parameters.
 * 
 * @author person7553f5959484
 * @version $Revision: 2603 $ $Author: psa2920 $ $Date: 2005-11-01 18:31:59 +0100 (Tue, 01 Nov 2005) $
 */
public abstract class IntegrationService implements LocalService, BeanNameAware {

	private static final MonitorKey MONITOR_KEY = new MonitorKey("IntegrationService", MonitorKey.ADDITIONAL);

	/** wether or not this service can run in a transaction */
	private boolean transactional = true;

	/** Logger to be used for writing <i>TRACE</i> and <i>DEBUG</i> messages. */
	protected Log log = LogFactory.getLog(this.getClass());

	/** The monitorChain */
	private MonitorChain monitorChain = null;

	/** Holds the <code>ServiceLevelMBean</code> to use. */
	private ServiceLevelMBean serviceLevelMBean = null;

	/**
	 * Set the name of the bean in the bean factory that created this bean.
	 * <p/>
	 * Invoked after population of normal bean properties but before an init
	 * callback like InitializingBean's afterPropertiesSet or a custom init-method.
	 * 
	 * @param beanName the name of the bean in the factory
	 * @see BeanNameAware#setBeanName(String)
	 */
	public void setBeanName(String beanName) {
		serviceLevelMBean = ServiceLevelMBeanFactory.getMBean(ServiceLevelConstants.SERVICE_TYPE_INTEGRATION, beanName);
	}

	/**
	 * Executes all integration service processing steps in a predefined order using this template method.
	 * 
	 * {@inheritDoc}
	 */
	public final ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {
		long start = System.currentTimeMillis();
		ServiceResponse response = null;

		if (monitorChain != null) {
			MonitorEvent monitorEvent = new MonitorEvent();
			monitorChain.preExecute(monitorEvent);

			if (log.isDebugEnabled()) {
				log.debug("MONITORING - Time spent in pre monitor:" + monitorEvent.getPreMonitorTime());
				log.debug("MONITORING - Time spent in pre manage:" + monitorEvent.getPreManageTime());
			}

			try {
				response = doExecute(request);
				serviceLevelMBean.add(System.currentTimeMillis() - start, null);
			} catch (ServiceFailedException sfe) {
				monitorEvent.setException(sfe);
				serviceLevelMBean.add(System.currentTimeMillis() - start, sfe);
				throw sfe;
			} catch (RuntimeException re) {
				monitorEvent.setException(re);
				serviceLevelMBean.add(System.currentTimeMillis() - start, re);
				throw re;
			} finally {
				monitorChain.postExecute(monitorEvent);

				if (log.isDebugEnabled()) {
					log.debug("MONITORING - Time spent in post monitor:" + monitorEvent.getPostMonitorTime());
					log.debug("MONITORING - Time spent in post manage:" + monitorEvent.getPostManageTime());
				}
			}
		} else {
			try {
				PerformanceMonitor.start(MONITOR_KEY);
				response = doExecute(request);
				PerformanceMonitor.end(MONITOR_KEY);
				serviceLevelMBean.add(System.currentTimeMillis() - start, null);
			} catch (ServiceFailedException sfe) {
				PerformanceMonitor.fail(MONITOR_KEY);
				serviceLevelMBean.add(System.currentTimeMillis() - start, sfe);
				throw sfe;
			} catch (RuntimeException re) {
				PerformanceMonitor.fail(MONITOR_KEY);
				serviceLevelMBean.add(System.currentTimeMillis() - start, re);
				throw re;
			}
		}

		return response;
	}

	/**
	 * Sub classes should implement the core integration service processing inside this method.
	 * 
	 * @param	request the <i>ServiceRequest</i> object sent from
	 * 			the client as input to the service.
	 * 
	 * @return	the <i>ServiceResponse</i> object sent to the client
	 * 			as output from the service.
	 * 
	 * @throws 	ServiceFailedException if service execution fails.
	 */
	protected abstract ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException;

	/**
	 * Sets the monitor chain. If the monitor chain is set, monitoring is enabled since
	 * a monitor MUST have monitors.
	 * @param chain the monitor chain.
	 */
	public void setMonitorChain(MonitorChain chain) {
		monitorChain = chain;
	}

	/**
	 * Returns wether or not this service can run in a transaction or not.
	 * Default is true;
	 * @return false of this service cannot run in a transaction.
	 */
	public boolean isTransactional() {
		return transactional;
	}

	/**
	 * Sets wether or not this service can run in a transaction.
	 * @param transactional wether or not this service can run in a transaction.
	 */
	public void setTransactional(boolean transactional) {
		this.transactional = transactional;

	}

}
