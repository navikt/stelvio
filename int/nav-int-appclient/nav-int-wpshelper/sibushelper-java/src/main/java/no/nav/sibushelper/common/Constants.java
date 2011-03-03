package no.nav.sibushelper.common;

import java.util.Arrays;
import java.util.List;

/**
 * @author persona2c5e3b49756 Schnell
 * 
 */
public class Constants {

	/**
	 * Public constant for logging
	 */
	public static final String METHOD_ERROR = "ERROR - ";

	public static final String ACTION_REPORT = "REPORT";
	public static final String ACTION_DISCARD = "DELETE";
	public static final String ACTION_RESUBMIT = "RESUBMIT";
	public static final String ACTION_MOVE = "MOVE";
	public static final String ACTION_STATUS = "STATUS";

	/*
	 * COMPONENT Options
	 */
	public static final String HELPER_COMPONENT_SERVER = "server";
	public static final String HELPER_COMPONENT_BUS = "sibus";
	public static final String HELPER_COMPONENT_MQLINK = "wmqlink";
	public static final String HELPER_COMPONENT_QUEUE = "queue";

	public static final List<String> ACTIONS = Arrays.asList(ACTION_REPORT, ACTION_DISCARD, ACTION_RESUBMIT, ACTION_MOVE,
			ACTION_STATUS);
	public static final List<String> COMPONENTS = Arrays.asList(HELPER_COMPONENT_SERVER, HELPER_COMPONENT_BUS,
			HELPER_COMPONENT_MQLINK, HELPER_COMPONENT_QUEUE);

	/**
	 * Public constant to represent the formatter pattern to be used together with <code>Constants.timeFrame</code> operation
	 */
	public static final String TIME_FRAME_FORMAT = "dd.MM.yyyy:hhmm";

	/**
	 * Public constant to represent the Date operation and parameter string from the configuration file and properties related
	 * to that
	 */
	public static String DEFAULT_DATE_FORMAT = "yyyyMMddHHmmss";
	public static String DEFAULT_DATE_FORMAT_MILLS = "yyyy-MM-dd HH:mm:ss:SSS";
	public static String DEFAULT_DATE_FORMAT_TZ = "dd-MM-yyyy HH:mm:ss:SSS zzz";
	public static final int MAX_MOVE = 50;
	public static final String FILE_PREFIX = "LOG";

	/**
	 * SIBUS internal properties we need to get it work
	 */
	public static final String OUTBOUND_JFAP_CHAIN_NAME = "BootstrapBasicMessaging";
	public static final String INBOUND_JFAP_CHAIN_NAME = "InboundBasicMessaging";
	public static final String OUTBOUND_JFAP_SSL_CHAIN_NAME = "BootstrapSecureMessaging";
	public static final String INBOUND_JFAP_SSL_CHAIN_NAME = "InboundSecureMessaging";
	public final static String HA_BUS = "WSAF_SIB_BUS";
	public final static String SOAP_PROTOCOL = "SOAP";
	public final static String RMI_PROTOCOL = "RMI";
	public final static String MQ_PROTOCOL = "WMQ";

	/**
	 * Further properties admin client
	 */
	public final static String PROP_SERVER_HOST_NAME = "CONNECTOR_HOST";
	public final static String PROP_SERVER_PORT = "CONNECTOR_PORT";
	public final static String PROP_USER_NAME = "username";
	public final static String PROP_PASSWORD = "password";
	public final static String PROP_SERVER_PROTOCOL = "CONNECTOR_TYPE";
	public final static String PROP_SECURITY_ENABLED = "CONNECTOR_SECURITY_ENABLED";

	public final static String PROP_VIEW_SYSTEM = "viewSystemObjs";
	public final static String PROP_VIEW_TEMP = "viewTempObjs";
	public final static String PROP_AUTO_REFRESH = "autoRefresh";
	public final static String PROP_RESYNC_ON_CREATE = "resyncOnObjCreate";
	public final static String PROP_USEJMS = "useJMS";

	public final static String PROP_SERVER_NAME = "serverName";
	public final static String PROP_MSGING_HOST = "meEngineHost";
	public final static String PROP_MSGING_PORT = "meEnginePort";
	public final static String PROP_MSGING_CHAIN = "meEngineChain";
	public final static String PROP_MSGING_USER_NAME = "meEngineUserName";
	public final static String PROP_MSGING_PASSWORD = "meEnginePassword";
	public final static String PROP_MSGING_ALTERNATE_CRED = "meEngineUserAlternateUserName";

	public final static String PROP_TRUST_LOCATION = "SSL_TRUSTSTORE";
	public final static String PROP_TRUST_PASSWORD = "SSL_TRUSTSTORE_PASSWORD";

	public final static String PROP_KEY_LOCATION = "SSL_KEYSTORE";
	public final static String PROP_KEY_PASSWORD = "SSL_KEYSTORE_PASSWORD";
	public final static String PROP_CREATE_NEW_SSL_STORES = "SSL_STORE_CREATENEW";

	public final static String SE_QUEUE = "_SYSTEM.Exception.Destination.";
	public final static String ARG_DELIMITER = ":";
	public final static String ARG_FILTER = "*";
	public final static String ARG_QUEUE_DEL = ",";

	public final static String JS_SIBHELP_REDELIVERY = "SIBHELP_REDELIVERY";
	public final static Integer JS_MAX_REDELIVERY = 6;

}
