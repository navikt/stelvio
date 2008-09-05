package no.nav.femhelper.common;
/**
 * Class to keep common contants
 * 
 * @author Andreas R�e
 */
public class Constants {

	/**
	 * Public constant to keep the "Enter" method statement
	 */
	public static final String METHOD_ENTER = "Enter method - ";
	
	/**
	 * Public constant to keep the "Exit" method statement
	 */
	public static final String METHOD_EXIT = "Exit method- ";
	
	
	/**
	 * Public constant for logging
	 */
	public static final String METHOD_ERROR = "ERROR - ";
	
	/**
	 * Public constant to represent the 'CONNECTOR_HOST' string from
	 * the configuration file and properties related to that
	 */
	public static final String CONNECTOR_HOST = "CONNECTOR_HOST";
	
	/**
	 * Public constant to represent the 'CONNECTOR_PORT' string from
	 * the configuration file and properties related to that
	 */
	public static final String CONNECTOR_PORT = "CONNECTOR_PORT"; 
	
	/**
	 * Public constant to represent the 'CONNECTOR_TYPE' string from
	 * the configuration file and properties related to that
	 */
	public static final String CONNECTOR_TYPE = "CONNECTOR_TYPE"; 
	
	
	/**
	 * Public constant to represent the 'CONNECTOR_SECURITY_ENABLED' string from
	 * the configuration file and properties related to that
	 */
	public static final String CONNECTOR_SECURITY_ENABLED = "CONNECTOR_SECURITY_ENABLED"; 
		
	/**
	 * Public constant to represent the 'USERNAME' string from
	 * the configuration file and properties related to that
	 */
	public static final String USERNAME = "USERNAME";
		
	/**
	 * Public constant to represent the 'PASSWORD' string from
	 * the configuration file and properties related to that
	 */
	public static final String PASSWORD = "PASSWORD";
	
	/**
	 * Public constant to represent the 'SSL_KEYSTORE' string from
	 * the configuration file and properties related to that
	 */
	public static final String SSL_KEYSTORE = "SSL_KEYSTORE";
	
	/**
	 * Public constant to represent the 'SSL_KEYSTORE_PASSWORD' string from
	 * the configuration file and properties related to that
	 */
	public static final String SSL_KEYSTORE_PASSWORD = "SSL_KEYSTORE_PASSWORD";
	
	/**
	 * Public constant to represent the 'SSL_TRUSTSTORE' string from
	 * the configuration file and properties related to that
	 */
	public static final String SSL_TRUSTSTORE = "SSL_TRUSTSTORE";
	
	/**
	 * Public constant to represent the 'SSL_TRUSTSTORE_PASSWORD' string from
	 * the configuration file and properties related to that
	 */
	public static final String SSL_TRUSTSTORE_PASSWORD = "SSL_TRUSTSTORE_PASSWORD";
	
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
	public static final String logFilePath = "logFilePath";
	
	/**
	 * Public constant to represent the 'logFileName' string from
	 * <code>CommandLine</code> object
	 */
	public static final String logFileName = "logFileName";
	
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
	 * Public constant to represent the 'messageType' string from
	 * <code>CommandLine</code> object
	 */
	public static final String messageType = "messageType";
	public static final String[] messageTypeOptions = {"ALL", "TPSEndringsmelding"};

	/**
	 * Public constant to represent the 'action' string from
	 * <code>CommandLine</code> object
	 */
	public static final String action = "action";
	public static final String[] actionOptions = {"REPORT", "DISCARD", "RESUBMIT", "STATUS", "TIMEFRAME"};
	
	/**
	 * Public constant to represent the 'FEM_EDA_TYPE_ACTION_PARAM' string from
	 * the configuration file and properties related to that
	 */
	public static final String FEM_EDA_TYPE_ACTION_PARAM = "FEM_EDA_TYPE_ACTION_PARAM";
	public static final String[] FEM_EDA_TYPE_PARAM_OPTIONS = {"GENERIC"};
	
	/**
	 * Public constant to represent the 'timeFrame' string from
	 * <code>CommandLine</code> object
	 */
	public static final String timeFrame = "timeFrame";
	
	/**
	 * Public constant to represent the formatter pattern to be used
	 * together with <code>Constants.timeFrame</code> operation
	 */
	public static final String TIME_FRAME_FORMAT = "dd.MM.yyyy:mmss";
	
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
