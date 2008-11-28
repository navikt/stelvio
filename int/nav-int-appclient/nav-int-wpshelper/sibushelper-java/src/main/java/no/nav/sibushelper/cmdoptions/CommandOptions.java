package no.nav.sibushelper.cmdoptions;

public interface CommandOptions {
	/**
	 * Public constant to represent the 'help' string from
	 * <code>CommandLine</code> object
	 */
	public static final String help = "help";
	
	/**
	 * Public constant to represent the 'configFile' string from
	 * <code>CommandLine</code> object
	 */
	public static final String configFile = "configFile";
	
	/**
	 * Public constant to represent the 'logFilePath' string from
	 * <code>CommandLine</code> object
	 */
	public static final String reportDirectory = "reportDirectory";
	
	/**
	 * Public constant to represent the 'sourceModule' string from
	 * <code>CommandLine</code> object
	 */
	public static final String action = "action";

	/**
	 * Public constant to represent the 'timeFrame' string from
	 * <code>CommandLine</code> object
	 */
	public static final String timeFrame = "timeFrame";

	/**
	 * Public constant to represent the 'maxResultSet' string from
	 * <code>CommandLine</code> object
	 */
	public static final String maxResultSet = "maxBatchSize";

	/**
	 * Public constant to represent the 'noStop' parameter from
	 * <code>CommandLine</code> object
	 */
	public static final String noStop = "noStop";

	/**
	 * Public constant to represent the 'failureMessage' string from
	 * <code>CommandLine</code> object
	 */
	public static final String failureMessage = "failureMessage";

	/**
	 * Public constant to represent the 'componentOption' string from
	 * <code>CommandLine</code> object
	 */
	public static final String component = "component";
	
	/**
	 * Public constant to represent the 'generic arguments' string from
	 * <code>CommandLine</code> object
	 */
	public static final String genericArguments = "busName:queue,selector";
	
}
