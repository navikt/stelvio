package no.nav.femhelper.common;

import java.util.Arrays;
import java.util.List;

/**
 * Class to keep common contants
 * 
 * @author Andreas Røe
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

	public static final List<String> ACTIONS = Arrays.asList(ACTION_REPORT, ACTION_DISCARD, ACTION_RESUBMIT, ACTION_STATUS,
			ACTION_COUNT);

	/**
	 * Public constant to represent the formatter pattern to be used together with <code>Constants.timeFrame</code> operation
	 */
	public static final String TIME_FRAME_FORMAT = "dd.MM.yyyy:hhmm";

	/**
	 * Public constant to represent the Date operation and parameter string from the configuration file and properties related
	 * to that
	 */
	public static String DEFAULT_DATE_FORMAT = "yyyyMMddHHmmss";
	public static String DEFAULT_DATE_FORMAT_MILLS = "yyyy-MM-dd HH:mm:ss:S";
	public static final long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
	public static final long ONE_MILLI = 1;
	public static final int MONTH_RANGE = 3;
	public static final int MAX_DELETE = 100;
	public static final String FILE_PREFIX = "LOG";

	/**
	 * Public properties representing failed event type
	 */
	public static final String EVENT_TYPE_BPC = "BPC";
	public static final String EVENT_TYPE_JMS = "JMS";
	public static final String EVENT_TYPE_MQ = "MQ";
	public static final String EVENT_TYPE_SCA = "SCA";

	/**
	 * Public properties representing max cell lengths in report
	 */
	public static final int REPORT_MAX_CELL_LENGTH = 31416;
	public static final String REPORT_TRUNCATE_STRING = " ...(truncated)";
	
	/**
	 * Public properties representing information messages related to events that are currently not supported
	 */
	public static final String MQ_EVENT_INFORMATION_MESSAGE = "This line represents an MQ event. FEM Helper does not currently support reporting details of MQ events";
}
