package no.nav.femhelper.common;

import java.util.Arrays;
import java.util.List;

/**
 * Class to keep common contants
 * 
 * @author Andreas R�e
 */
public class Constants {
	/**
	 * Public constant for logging
	 */
	public static final String METHOD_ERROR = "ERROR - ";

	public static final String ACTION_REPORT = "REPORT";
	public static final String ACTION_DISCARD = "DISCARD";
	public static final String ACTION_RESUBMIT = "RESUBMIT";
	public static final String ACTION_STATUS = "STATUS";
	public static final String ACTION_COUNT = "COUNT";

	public static final List<String> ACTIONS = Arrays.asList(ACTION_REPORT, ACTION_DISCARD, ACTION_RESUBMIT, ACTION_STATUS, ACTION_COUNT);

	/**
	 * Public constant to represent the formatter pattern to be used together
	 * with <code>Constants.timeFrame</code> operation
	 */
	public static final String TIME_FRAME_FORMAT = "dd.MM.yyyy:hhmm";

	/**
	 * Public constant to represent the Date operation and parameter string from
	 * the configuration file and properties related to that
	 */
	public static String DEFAULT_DATE_FORMAT = "yyyyMMddHHmmss";
	public static String DEFAULT_DATE_FORMAT_MILLS = "yyyy-MM-dd HH:mm:ss:S";
	public static final long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
	public static final long ONE_MILLI = 1;
	public static final int MONTH_RANGE = 3;
	public static final int MAX_DELETE = 100;
	public static final String FILE_PREFIX = "LOG";

}
