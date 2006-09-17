package no.stelvio.common.framework.jmx;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;

/**
 * MBeanInfo for service level reporting.
 * 
 * @author person1f201b37d484
 * @version $Id: ServiceLevelMBeanInfo.java 2577 2005-10-21 14:43:34Z psa2920 $
 */
public class ServiceLevelMBeanInfo extends MBeanInfo {

	private static final String DESCRIPTION = "Service Level Dynamic Management Interface";

	/** Minimum attribute. */
	public static final String ATTR_MIN = "Minimum";

	/** Maximum attribute. */
	public static final String ATTR_MAX = "Maximum";

	/** Average attribute. */
	public static final String ATTR_AVG = "Average";

	/** Count attribute. */
	public static final String ATTR_COUNT = "Count";

	/** ErrorCount attribute. */
	public static final String ATTR_ERRORS = "ErrorCount";

	// Is attribute readable and/or writable?
	private static final boolean READABLE = true;
	private static final boolean WRITABLE = true;

	// Is attribute getter in boolean isAttribute() form?
	private static final boolean IS_GETTER = true;

	// Management constructor
	private static final MBeanConstructorInfo DEFAULT =
		new MBeanConstructorInfo("Default Constructor", "Creates a new service level instance", null);

	private static final MBeanAttributeInfo MIN =
		new MBeanAttributeInfo(
			ATTR_MIN,
			Long.class.getName(),
			"The minimum response time measured",
			READABLE,
			!WRITABLE,
			!IS_GETTER);

	private static final MBeanAttributeInfo MAX =
		new MBeanAttributeInfo(
			ATTR_MAX,
			Long.class.getName(),
			"The maximum response time measured",
			READABLE,
			!WRITABLE,
			!IS_GETTER);

	private static final MBeanAttributeInfo AVG =
		new MBeanAttributeInfo(
			ATTR_AVG,
			Long.class.getName(),
			"The average of all the response times measured",
			READABLE,
			!WRITABLE,
			!IS_GETTER);

	private static final MBeanAttributeInfo COUNT =
		new MBeanAttributeInfo(
			ATTR_COUNT,
			Long.class.getName(),
			"The number of times the service is invoked",
			READABLE,
			!WRITABLE,
			!IS_GETTER);

	private static final MBeanAttributeInfo ERRORS =
		new MBeanAttributeInfo(
			ATTR_ERRORS,
			Long.class.getName(),
			"The number of times the service failed",
			READABLE,
			!WRITABLE,
			!IS_GETTER);

	// Constructor, attribute and operation lists
	private static final MBeanConstructorInfo[] CONSTRUCTORS = new MBeanConstructorInfo[] { DEFAULT };
	private static final MBeanAttributeInfo[] ATTRIBUTES = new MBeanAttributeInfo[] { MIN, MAX, AVG, COUNT, ERRORS };
	private static final MBeanOperationInfo[] OPERATIONS = new MBeanOperationInfo[0];

	/**
	 * Creates an MBeanInfo for specified service.
	 * 
	 * @param serviceName the name the service is bound to in service registry.
	 */
	public ServiceLevelMBeanInfo(String serviceName) {
		super(serviceName, DESCRIPTION, ATTRIBUTES, CONSTRUCTORS, OPERATIONS, null);
	}
}
