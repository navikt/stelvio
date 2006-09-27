package no.stelvio.common.jmx;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Dynamic MBean for service level reporting.
 *
 * @author person1f201b37d484, Accenture
 * @author person7553f5959484, Accenture
 * @version $Id: ServiceLevelMBean.java 2786 2006-02-28 13:24:08Z skb2930 $
 */
public class ServiceLevelMBean implements DynamicMBean {

	// management fields
	// JMX management attribute name constants
	private long minimum = Long.MAX_VALUE;
	private long maximum = 0;
	private long count = 0;
	private long sum = 0;
	private long failures = 0;

	private ServiceLevelMBeanInfo mBeanInfo = null;

	/** Logger to be used for writing <i>TRACE</i> and <i>DEBUG</i> messages. */
	protected final Log log = LogFactory.getLog(this.getClass());

	/**
	 * Constructs an instance of ServiceLevelMBean for specified service name and type.
	 *
	 * @param serviceType the service type.
	 * @param serviceName the service name.
	 */
	public ServiceLevelMBean(String serviceType, String serviceName) {
		// initialize MBean if not done before
		try {
			mBeanInfo = new ServiceLevelMBeanInfo(serviceName);

			MBeanServer server = JMXUtils.getMBeanServer(null);
			ObjectName objectName =
				new ObjectName(serviceType + ":name=" + serviceName);

			if (!server.isRegistered(objectName)) {
					server.registerMBean(this, objectName);
			}
		} catch (InstanceAlreadyExistsException iae) {
			// Defect #2984 As we have a cluster, we cannot guarantee that the MBean is not registered between the check
			// and our registering, so we only log this
			if (log.isDebugEnabled()) {
				log.debug("Failed to register service for management", iae);
			}
		} catch (JMException e) {
			if (log.isWarnEnabled()) {
				log.warn("Failed to register service for management", e);
			}
		} catch (RuntimeException e) {
			if (log.isWarnEnabled()) {
				log.warn("Failed to register service for management", e);
			}
		}
	}

	/**
	 * Reset the statistics.
	 */
	public void reset() {
		minimum = Long.MAX_VALUE;
		maximum = 0;
		count = 0;
		sum = 0;
		failures = 0;
	}

	/**
	 * Update the statistics.
	 *
	 * @param time the response time.
	 * @param t the exception / error if any
	 */
	public void add(long time, Throwable t) {
		sum += time;
		count++;
		if (time < minimum) {
			minimum = time;
		}
		if (time > maximum) {
			maximum = time;
		}

		if (null != t) {
			failures++;
		}
	}

	/**
	 * Returns the number of invocations.
	 *
	 * @return number of invocations.
	 */
	private long getCount() {
		return count;
	}

	/**
	 * Returns the number of invocations that failed.
	 *
	 * @return number of failures.
	 */
	private long getErrorCount() {
		return failures;
	}

	/**
	 * Returns the minimum response time measured.
	 *
	 * @return minimum response time.
	 */
	private long getMinimum() {
		if (minimum == Long.MAX_VALUE) {
			return 0;
		}
		return minimum;
	}

	/**
	 * Returns the maximum response time measured.
	 *
	 * @return maximum response time.
	 */
	private long getMaximum() {
		return maximum;
	}

	/**
	 * Returns the average response time.
	 *
	 * @return the average response time.
	 */
	private long getAverage() {
		if (count == 0) {
			return 0;
		}
		return sum / count;
	}

	/**
	 * {@inheritDoc}
	 * @see DynamicMBean#getAttribute(String)
	 */
	public Object getAttribute(String attribute)
		throws AttributeNotFoundException, MBeanException, ReflectionException {

		if (ServiceLevelMBeanInfo.ATTR_MIN.equals(attribute)) {
			return new Long(getMinimum());
		} else if (ServiceLevelMBeanInfo.ATTR_MAX.equals(attribute)) {
			return new Long(getMaximum());
		} else if (ServiceLevelMBeanInfo.ATTR_AVG.equals(attribute)) {
			return new Long(getAverage());
		} else if (ServiceLevelMBeanInfo.ATTR_COUNT.equals(attribute)) {
			return new Long(getCount());
		} else if (ServiceLevelMBeanInfo.ATTR_ERRORS.equals(attribute)) {
			return new Long(getErrorCount());
		} else {
			throw new AttributeNotFoundException(
				"Attribute " + attribute + " not found");
		}
	}

	/**
	 * {@inheritDoc}
	 * @see DynamicMBean#getAttributes(String[])
	 */
	public AttributeList getAttributes(String[] attributes) {

		if (null == attributes) {
			return null;
		}

		AttributeList list = new AttributeList();

		for (int i = 0; i < attributes.length; i++) {
			try {
				list.add(
					new Attribute(attributes[i], getAttribute(attributes[i])));
			} catch (JMException ignored) {
				// Skip the attribute
			}
		}

		return list;
	}

	/**
	 * {@inheritDoc}
	 * @see DynamicMBean#invoke(String, Object[], String[])
	 */
	public Object invoke(String action, Object[] params, String[] signature)
		throws MBeanException, ReflectionException {
		if ("reset".equals(action)) {
			reset();
		} else {
			throw new UnsupportedOperationException(
				"Unknown operation " + action);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see DynamicMBean#setAttribute(Attribute)
	 */
	public void setAttribute(Attribute attribute)
		throws
			AttributeNotFoundException,
			InvalidAttributeValueException,
			MBeanException,
			ReflectionException {
		throw new AttributeNotFoundException(
			"Attribute " + attribute + " not found");
	}

	/** 
	 * {@inheritDoc}
	 * @see DynamicMBean#setAttributes(AttributeList)
	 */
	public AttributeList setAttributes(AttributeList attributes) {
		if (null == attributes) {
			return null;
		}
		return new AttributeList();
	}

	/** 
	 * {@inheritDoc}
	 * @see DynamicMBean#getMBeanInfo()
	 */
	public MBeanInfo getMBeanInfo() {
		return mBeanInfo;
	}
}
