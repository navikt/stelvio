package no.stelvio.common.performance;

/**
 * MonitorKey provides the key required by the Monitor.
 * Each key has internally associated a description and a level.
 * <p/>
 * Level is used to organize and filter monitoring. 
 *
 * @author person7553f5959484
 * @version $Revision: 1949 $ $Author: psa2920 $ $Date: 2005-02-08 12:33:09 +0100 (Tue, 08 Feb 2005) $
 */
public final class MonitorKey {

	/** Presentation tier monitoring level */
	public static final int PRESENTATION = 1;

	/** Business tier monitoring level */
	public static final int BUSINESS = 2;

	/** Integration tier monitoring level */
	public static final int INTEGRATION = 3;

	/** Resource tier monitoring level */
	public static final int RESOURCE = 4;

	/** Additional monitoring level */
	public static final int ADDITIONAL = 5;

	private int level;
	private String description;

	/**
	 * Default constructor used by Spring.
	 */
	public MonitorKey() {

	}

	/**
	 * Constructs a monitor key with a description of the unit to be 
	 * moitored and monitoring level.
	 *
	 * @param description String used to describe the monitoring
	 * @param level int used to organize monitoring
	 */
	public MonitorKey(String description, int level) {
		this.description = description;
		this.level = level;
	}

	/**
	 * @return a String representation of this instance
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return (description + ", " + level);
	}

	/**
	 * @return the monitoring level for this key.
	 */
	public int getLevel() {
		return this.level;
	}
	/**
	 * Sets the description for this MonitorKey.
	 * 
	 * @param description the description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the level for this monitor key.
	 * 
	 * @param level the level.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

}
