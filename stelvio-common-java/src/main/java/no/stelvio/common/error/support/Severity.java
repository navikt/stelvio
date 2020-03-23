package no.stelvio.common.error.support;

/**
 * Error severity enumeration. The severity levels reflect the most severe log levels used in Jakarta Commons Logging where
 * <code>FATAL</code> has highest severity and <code>DEBUG</code> the lowest, se the ordered list below:
 * <ol>
 * <li> <code> FATAL </code></li>
 * <li> <code> ERROR </code></li>
 * <li> <code> WARN </code></li>
 * <li> <code> INFO </code></li>
 * <li> <code> DEBUG </code></li>
 * </ol>
 * Another way to se this is how the severity increases from left to right:
 * <p>
 * <code>INFO &lt; WARN &lt; ERROR &lt; FATAL</code>
 * 
 * @version $Id: Severity.java 1954 2005-02-08 13:35:42Z psa2920 $
 */
public enum Severity {
	/**
	 * Fatal level.
	 */
	FATAL(6),
	/**
	 * Error level.
	 */
	ERROR(5),
	/**
	 * Warning level.
	 */
	WARN(4),
	/**
	 * Info level.
	 */
	INFO(3),

	/**
	 * Debug level.
	 */
	DEBUG(1);

	private int level;

	/**
	 * Sets the severity level.
	 * 
	 * @param level
	 *            the severity level
	 */
	Severity(int level) {
		this.level = level;
	}

	/**
	 * Compares the object's serverity with the Severity object passed to the method and returns a boolean indicating the
	 * result.
	 * 
	 * @param other
	 *            the Serverity to compare the existing object to
	 * @return true if the the severity of the object is greater than that of other. Otherwise it returns false.
	 */
	public boolean isMoreFatalThan(Severity other) {
		return level > other.level;
	}

	/**
	 * Get the level.
	 * 
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

}
