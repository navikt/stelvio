package no.stelvio.common.framework.performance;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

import no.stelvio.common.framework.jmx.ServiceLevelConstants;
import no.stelvio.common.framework.jmx.ServiceLevelMBean;
import no.stelvio.common.framework.jmx.ServiceLevelMBeanFactory;

/**
 * Monitor implementation that uses JMX for reporting performance statistics.
 *
 * @author person7553f5959484
 * @version $Revision: 2606 $ $Author: psa2920 $ $Date: 2005-11-01 18:45:38 +0100 (Tue, 01 Nov 2005) $
 */
public final class ServiceLevelMBeanMonitorImpl extends AbstractMonitor {

	// ServiceLevelMBeans for Presentation, Business and Integration
	private final ServiceLevelMBean serviceLevelsPresentation =
		ServiceLevelMBeanFactory.getMBean(ServiceLevelConstants.SERVICE_TYPE_ARCHITECTURE, "Presentation");
	private final ServiceLevelMBean serviceLevelsBusiness =
		ServiceLevelMBeanFactory.getMBean(ServiceLevelConstants.SERVICE_TYPE_ARCHITECTURE, "Business");
	private final ServiceLevelMBean serviceLevelsIntegration =
		ServiceLevelMBeanFactory.getMBean(ServiceLevelConstants.SERVICE_TYPE_ARCHITECTURE, "Integration");
	private final ServiceLevelMBean serviceLevelsResource =
		ServiceLevelMBeanFactory.getMBean(ServiceLevelConstants.SERVICE_TYPE_ARCHITECTURE, "Resource");
	private final ServiceLevelMBean serviceLevelsAdditional =
		ServiceLevelMBeanFactory.getMBean(ServiceLevelConstants.SERVICE_TYPE_ARCHITECTURE, "Additional");

	// The hasmap where the timestamps for each key are stored
	private HashMap hashMap = new HashMap();

	/** Constructs a default ServiceLevelMBeanMonitorImpl. */
	public ServiceLevelMBeanMonitorImpl() {
	}

	/**
	 * @see no.stelvio.common.framework.performance.Monitor#start(MonitorKey)
	 */
	public void start(MonitorKey key) {
		if (key.getLevel() <= level) {
			this.start(key, "");
		}
	}

	/**
	 * @see no.stelvio.common.framework.performance.Monitor#fail(MonitorKey)
	 */
	public void fail(MonitorKey key) {
		if (key.getLevel() <= level) {
			this.fail(key, 0);
		}
	}

	/**
	 * @see no.stelvio.common.framework.performance.Monitor#start(no.stelvio.common.framework.performance.MonitorKey, java.lang.String)
	 */
	public void start(MonitorKey key, String contextName) {
		if (key.getLevel() <= level) {
			try {
				TimeElement startTime = new TimeElement(System.currentTimeMillis());
				startTime.setContextName(contextName);

				// Check if the thread already contains monitoring info
				Stack stack = (Stack) hashMap.get((Object) Thread.currentThread());
				if (null == stack) {
					stack = new Stack();
				}
				stack.push(startTime);

				// Store the stack for the current thread
				hashMap.put((Object) Thread.currentThread(), stack);
			} catch (Throwable t) {
				return; // Choke this
			}
		}
	}

	/**
	 * @see no.stelvio.common.framework.performance.Monitor#end(no.stelvio.common.framework.performance.MonitorKey)
	 */
	public void end(MonitorKey key) {
		add(key);
	}

	/**
	 * @see no.stelvio.common.framework.performance.Monitor#fail(no.stelvio.common.framework.performance.MonitorKey, int)
	 */
	public void fail(MonitorKey key, int lineNumber) {
		add(key);
	}

	/**
	 * Update the statistics.
	 * 
	 * @param level the monitoring level.
	 * @param duration the transaction duration.
	 */
	private void add(MonitorKey key) {
		if (key.getLevel() <= level) {
			try {
				long currentTime = System.currentTimeMillis();

				// Retrieve the stack for the current thread
				Stack stack = (Stack) hashMap.get((Object) Thread.currentThread());

				// Start must have been called for each end and opposite;
				// end must be called for each start that has been called
				TimeElement timeElement = (TimeElement) stack.pop();

				// Calculate total duration for current element
				long totalDuration = currentTime - timeElement.getStartTime();

				// If the stack is not empty, this is a nested element
				// We need to tell the next element in the stack the
				// total duration of this time element
				try {
					TimeElement callerTimeElement = (TimeElement) stack.pop();
					callerTimeElement.addNestedDuration(totalDuration);

					// Push the updated time element into the stack again
					stack.push(callerTimeElement);
				} catch (EmptyStackException ese) {
					return; // Choke this
				}

				// Store the updated stack for the current thread
				hashMap.put((Object) Thread.currentThread(), stack);
				if (key.getLevel() == MonitorKey.PRESENTATION) {
					serviceLevelsPresentation.add(totalDuration, null);
				} else if (key.getLevel() == MonitorKey.BUSINESS) {
					serviceLevelsBusiness.add(totalDuration, null);
				} else if (key.getLevel() == MonitorKey.INTEGRATION) {
					serviceLevelsIntegration.add(totalDuration, null);
				} else if (key.getLevel() == MonitorKey.RESOURCE) {
					serviceLevelsResource.add(totalDuration, null);
				} else {
					serviceLevelsAdditional.add(totalDuration, null);
				}
			} catch (Throwable t) {
				return; // Choke this
			}
		}
	}
}
