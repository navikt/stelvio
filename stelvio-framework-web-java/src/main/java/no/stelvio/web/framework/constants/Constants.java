package no.stelvio.web.framework.constants;

/**
 * Constants used in the web framework.
 * 
 * @author Jonas Lindholm, Accenture
 * @version  $Revision: 2730 $ $Author: skb2930 $ $Date: 2006-01-04 08:01:37 +0100 (Wed, 04 Jan 2006) $
 */
public final class Constants {

	/** Name of the request parameter that contains the choosen row number. */
	public static final String CHOOSEN_ROW = "CHOOSEN_ROW";

	/** Prefix to be used to identify the button pressed. Value is <code>execute</code> */
	public static final String METHOD_PREFIX = "execute";

	/** Name of the Spring web configutration file.
	 *  Value is: <code>PRESENTATION_SERVICES</code> */
	public static final String PRESENTATION_SERVICES = "PRESENTATION_SERVICES";

	/** Name of the <code>BusinessDelegate</code> bean.
	 *  Value is: <code>BusinessDelegate</code> */
	public static final String BUSINESS_DELEGATE_BEAN = "BusinessDelegate";

	/** Name of the <code>CodesTableManager</code> bean.
	 *  Value is: <code>CodesTableManager</code> */
	public static final String CODES_TABLE_MANAGER_BEAN = "CodesTableManager";

	/** Name of the <code>BusinessConfig</code> servlet context attribute.
	 *  Value is: <code>BusinessConfig</code> */
	public static final String BUSINESS_CONFIG_ATTRIBUTE = "BusinessConfig";

	/** Name of the method that loads a window.
	 *  Value is: <code>executeLoad</code> */
	public static final String WINDOW_LOAD_METHOD = "executeLoad";

	/** Name of the method that forwards to the method specified in the request parameters.
	 *  Value is: <code>executeForward</code> */
	public static final String WINDOW_FORWARD_METHOD = "executeForward";

	/** Name of the method that takes care of exceptions.
	 *  Value is: <code>executeException</code> */
	public static final String EXCEPTION_METHOD = "executeException";

	/** Attribute in the request context that hold the method to execute in the next action.
	 *  Value is: <code>METHOD_TO_EXECUTE</code> */
	public static final String METHOD_TO_EXECUTE = "METHOD_TO_EXECUTE";

	/** Attribute in the servlet context that hold the admin messages.
	 *  Value is: <code>ADMIN_MESSAGE</code> */
	public static final String ADMIN_MESSAGE = "ADMIN_MESSAGE";

	/** Name of the <code>SingleSignOnConfiguration</code> bean.*/
	public static final String SSO_CONFIG_BEAN = "SingleSignOnConfiguration";

	/**
	 * Name of the <code>DONT_CHECK_TOKEN</code> session attribute and request parameter.
	 */
	public static final String DONT_CHECK_TOKEN = "dontCheckToken";

	/**
	 * Name of the <code>state</code> request parameter.
	 */
	public static final String CURRENT_STATE = "state";

	/**
	 * Name of the <code>SakAccessController</code> bean registered in the Spring Framework configurations.
	 */
	public static final String SAK_ACCESS_CONTROLLER = "SakAccessController";

	/**
	 * Name of the <code>PersonAccessController</code> bean registered in the Spring Framework configurations.
	 */
	public static final String PERSON_ACCESS_CONTROLLER = "PersonAccessController";

	/** Should not be instantiated. */
	private Constants() {

	}
}
