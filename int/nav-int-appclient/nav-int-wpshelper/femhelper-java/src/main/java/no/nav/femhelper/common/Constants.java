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
	 * Public constant to represent the 'maxResultSet' string from
	 * <code>CommandLine</code> object
	 */
	public static final String maxResultSet = "maxResultSet";

	/**
	 * Public constant to represent the 'maxResultSetPaging' string from
	 * <code>CommandLine</code> object
	 */
	public static final String maxResultSetPaging = "maxResultSetPaging";

	/**
	 * Public constant to represent the 'sourceModule' string from
	 * <code>CommandLine</code> object
	 */
	public static final String sourceModule = "sourceModule";

	/**
	 * Public constant to represent the 'sourceComponent' string from
	 * <code>CommandLine</code> object
	 */
	public static final String sourceComponent = "sourceComponent";

	/**
	 * Public constant to represent the 'destinationModule' string from
	 * <code>CommandLine</code> object
	 */
	public static final String destinationModule = "destinationModule";

	/**
	 * Public constant to represent the 'destinationComponent' string from
	 * <code>CommandLine</code> object
	 */
	public static final String destinationComponent = "destinationComponent";

	/**
	 * Public constant to represent the 'failureMessage' string from
	 * <code>CommandLine</code> object
	 */
	public static final String failureMessage = "failureMessage";

	/**
	 * Public constant to represent the 'dataObject' string from
	 * <code>CommandLine</code> object
	 */
	public static final String dataObject = "dataObject";

	/**
	 * Public constant to represent the 'messageType' string from
	 * <code>CommandLine</code> object
	 */
	public static final String messageType = "messageType";
	public static final String[] messageTypeOptions = { "ALL", "TPSEndringsmelding" };

	/**
	 * Public constant to represent the 'action' string from
	 * <code>CommandLine</code> object
	 */
	public static final String action = "action";

	/**
	 * Public constant to represent the 'noStop' parameter from
	 * <code>CommandLine</code> object
	 */
	public static final String noStop = "noStop";

	/**
	 * ActionOptions are 0:REPORT, 1:DISCARD, 2:RESUBMIT, 3:STATUS, 4:TIMEFRAME.
	 */
	public static final String[] actionOptions = { "REPORT", "DISCARD", "RESUBMIT", "STATUS" };

	public static final String ACTION_REPORT = "REPORT";
	public static final String ACTION_DISCARD = "DISCARD";
	public static final String ACTION_RESUBMIT = "RESUBMIT";
	public static final String ACTION_STATUS = "STATUS";

	public static final List ACTIONS = Arrays.asList(ACTION_REPORT, ACTION_DISCARD, ACTION_RESUBMIT, ACTION_STATUS);

	/**
	 * Public constant to represent the 'FEM_EDA_TYPE_ACTION_PARAM' string from
	 * the configuration file and properties related to that
	 */
	public static final String FEM_EDA_TYPE_ACTION_PARAM = "FEM_EDA_TYPE_ACTION_PARAM";
	public static final String[] FEM_EDA_TYPE_PARAM_OPTIONS = { "GENERIC" };

	/**
	 * Public constant to represent the 'timeFrame' string from
	 * <code>CommandLine</code> object
	 */
	public static final String timeFrame = "timeFrame";

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
