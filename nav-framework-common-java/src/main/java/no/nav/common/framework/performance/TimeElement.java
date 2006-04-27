package no.nav.common.framework.performance;

/**
 * Container used to store time elements when performance monitoring
 *
 * @author Petter_Skodvin
 * @version $Revision: 192 $ $Author: psa2920 $ $Date: 2004-05-13 17:42:12 +0200 (Thu, 13 May 2004) $
 */
final class TimeElement {

	// The start time of this element
	// No need for an end time in the object
	private final long startTime;

	// The duration of the nested element if any
	private long nestedDuration = 0;

	//	The context name for this element
	private String contextName = "";

	/**
	 * Constructor
	 * @param startTime the start time of this time element in milliseconds
	 */
	TimeElement(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the start time of this time element in milliseconds
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * Adds the duration of the nested element
	 * @param nestedDuration the duration of the nested time element in milliseconds
	 */
	void addNestedDuration(long nestedDuration) {
		this.nestedDuration += nestedDuration;
	}

	/**
	 * @return the duration of the nested time element in milliseconds
	 */
	public long getNestedDuration() {
		return nestedDuration;
	}

	/**
	 * @return the context name of this time element
	 */
	public String getContextName() {
		return contextName;
	}

	/**
	 * Sets the context name of this time element
	 * @param string the context name of this time element
	 */
	public void setContextName(String string) {
		contextName = string;
	}
}
