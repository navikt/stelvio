package no.stelvio.presentation.util;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.presentation.constants.Constants;

/**
 * Utilities for handling HttpServletRequests.
 * 
 * @author person7553f5959484, Accenture
 * @author Jonas Lindholm, Accenture
 * @version $Revision: 2220 $ $Author: skb2930 $ $Date: 2005-04-21 13:38:23 +0200 (Thu, 21 Apr 2005) $
 * @todo we might need this in new framework.
 */
public final class RequestUtils {

	// Logging
	private static final Log LOG = LogFactory.getLog(RequestUtils.class);

	/** Default value to be used when button is not identified. Value is <code>executeLoad</code>
	 *  that is the default method to load the window. 
	 */
	public static final String DEFAULT_METHOD = "Load";

	/**
	 * The regexp to use to separate the different methods in the struts-config parameter property.
	 */
	private static final String SEPARATORS_REGEXP = "[,\\s]+";

	/**
	 * Enforcing non instantiability using a private constructor.
	 */
	private RequestUtils() {
	}

	// --------------------------------------------------------- Public Methods

	/**
	 * Identify the id of the requested screen. This method is implemented 
	 * to parse the request URI for the path Jakarta Struts use to identify
	 * which <code>Action</code> should be processing this request.
	 * 
	 * @param request the HttpServletRequest we are processing
	 * @return The id of requested screen ~ "the stripped servlet path"
	 */
	public static String getScreenId(HttpServletRequest request) {
		return getScreenId(request.getServletPath());
	}

	/**
	 * Identify the id of the requested screen. This method is implemented 
	 * to parse the request URI for the path Jakarta Struts use to identify
	 * which <code>Action</code> should be processing this request.
	 * 
	 * @param uri the uri
	 * @return The id of requested screen ~ "the stripped servlet path"
	 */
	public static String getScreenId(String uri) {

		// Identify the part of the request that calls the servlet. This is either the
		// servlet name or a path to the servlet, but does not include any extra path 
		// information or a query string. The returned path will depend on how the servlet
		// mapping is configured, either suffix mapping or base URL mapping.
		int slash = uri.lastIndexOf("/");
		int period = uri.lastIndexOf(".");

		// Match requests of type '/Oppgaveliste.do'
		if (0 <= period && slash < period) {
			return uri.substring(1, period);
		} else { // Match requests of type '/do/Oppgaveliste'
			return uri.substring(slash + 1);
		}
	}

	/**
	 * Identify the id of the requested process. This method is implemented 
	 * to search the request for parameter names starting with 
	 * {@link no.stelvio.web.constants.Constants#METHOD_PREFIX}.
	 * If no parameter names are found, this method returns {@link #DEFAULT_METHOD}.
	 * The implementation uses the first occurence of the parameter name fond to extract
	 * the process id.
	 * 
	 * @param request the HttpServletRequest we are processing
	 * @return The id of requested process ~ "the id of the button pressed"
	 */
	public static String getProcessId(HttpServletRequest request) {
		Enumeration names = request.getParameterNames();
		int methodProsessIdStart;
		int arrayInicatorStart;
		int dotInicatorStart;
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			methodProsessIdStart = name.indexOf(Constants.METHOD_PREFIX);
			if (0 == methodProsessIdStart) {

				// Used if image buttons send the request
				dotInicatorStart = name.indexOf(".");
				// Used if a button is inside a nested loop
				arrayInicatorStart = name.indexOf("[");
				if (-1 < arrayInicatorStart) {
					int arrayInicatorStop = name.indexOf("]");
					// Find the choosen row
					request.setAttribute(Constants.CHOOSEN_ROW, name.substring(arrayInicatorStart + 1, arrayInicatorStop));
					return name.substring(Constants.METHOD_PREFIX.length() + methodProsessIdStart, arrayInicatorStart);
				}
				if (-1 < dotInicatorStart) {
					return name.substring(Constants.METHOD_PREFIX.length() + methodProsessIdStart, dotInicatorStart);
				}
				return name.substring(Constants.METHOD_PREFIX.length() + methodProsessIdStart);
			}
		}
		return DEFAULT_METHOD;
	}

	/**
	 * Identify the method to execute in the action that the control is forwarded to. If the control
	 * is forwarded between actions a attribute is used in the request context to direct the control.
	 * 
	 * @param request the HttpServletRequest we are processing
	 * @return The method to execute in the action.
     * @todo probably not needed.
	 */
	public static String getMethod(HttpServletRequest request) {
		String methodToExecute = (String) request.getAttribute(Constants.METHOD_TO_EXECUTE);
		if (null == methodToExecute) {
			methodToExecute = Constants.METHOD_PREFIX + getProcessId(request);
		}

		return methodToExecute;
	}

	/**
	 * Find the ProcessId and extract the index of the choosen row. The row where the submitbutton
	 * where pressed. (If a submitbutton is used in a table. Prefered usage is a link.)
	 * 
	 * @param request the HttpServletRequest we are processing
	 * @return The index of the choosen row.
	 */
	public static Integer getMethodNumber(HttpServletRequest request) {
		Enumeration names = request.getParameterNames();
		int methodProsessIdStart;
		int arrayInicatorStart;
		int arrayInicatorEnd;
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			methodProsessIdStart = name.indexOf(Constants.METHOD_PREFIX);
			if (-1 < methodProsessIdStart) {
				arrayInicatorStart = name.indexOf("[");
				arrayInicatorEnd = name.indexOf("]");
				if (-1 < arrayInicatorStart) {
					return Integer.valueOf(name.substring(arrayInicatorStart + 1, arrayInicatorEnd));
				}
			}
		}
		return null;
	}

	/**
	 * Returns the object bound with the specified name in this session, 
	 * or null if no object is bound under the name.   
	 * 
	 * @param request The HTTP request we are processing
	 * @param name a string specifying the name of the object  
	 * @return the object with the specified name
	 * @throws IllegalStateException if this method is called on an invalidated session
	 */
	public static Object getSessionAttribute(HttpServletRequest request, String name) throws IllegalStateException {
		return request.getSession().getAttribute(name);
	}

	/**
	 * Removes the object bound with the specified name from this session. If the session does not 
	 * have an object bound with the specified name, this method does nothing. 
	 * <p/>
	 * After this method executes, and if the object implements <code>HttpSessionBindingListener</code>,
	 * the container calls <code>HttpSessionBindingListener.valueUnbound</code>. The container then notifies 
	 * any <code>HttpSessionAttributeListener</code>s in the web application.
	 * 
	 * @param request The HTTP request we are processing
	 * @param name the name of the object to remove from this session
	 * @throws IllegalStateException if this method is called on an invalidated session
	 */
	public static void removeSessionAttribute(HttpServletRequest request, String name) throws IllegalStateException {
		request.getSession().removeAttribute(name);
	}

	/**
	 * Binds an object to this session, using the name specified. If an object of the same name 
	 * is already bound to the session, the object is replaced.
	 * <p/>
	 * After this method executes, and if the new object implements <code>HttpSessionBindingListener</code>,
	 * the container calls <code>HttpSessionBindingListener.valueBound</code>. The container then notifies 
	 * any <code>HttpSessionAttributeListener</code>s in the web application.
	 * <p/>
	 * If an object was already bound to this session with this name and that object implements 
	 * <code>HttpSessionBindingListener</code>, its <code>HttpSessionBindingListener.valueUnbound</code> 
	 * is called.
	 * <p/>
	 * If the value passed in is null, this has the same effect as calling <code>removeAttribute()</code>.
	 * 
	 * @param request The HTTP request we are processing
	 * @param name the name to which the object is bound; cannot be null
	 * @param value the object to be bound
	 * @throws IllegalStateException if this method is called on an invalidated session
	 */
	public static void setSessionAttribute(HttpServletRequest request, String name, Object value)
		throws IllegalStateException {
		request.getSession().setAttribute(name, value);
	}

	/**
	 * Logs the name and value of all attributes bound to session.
	 * 
	 * @param session current HttpSession.
	 */
	public static void debug(HttpSession session) {
		if (LOG.isDebugEnabled() && null != session) {
			StringBuffer sb = new StringBuffer("HttpSession Attributes:\n");
			sb.append("--------------------------------------------------\n");
			Enumeration names = session.getAttributeNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				sb.append(name).append(": ");
				sb.append(session.getAttribute(name)).append("\n");
			}
			sb.append("--------------------------------------------------");
			LOG.debug(sb.toString());
		}
	}

	/**
	 * Logs the name and value of all parameters submitted on the request.
	 * 
	 * @param request current HttpServletRequest.
	 */
	public static void debug(HttpServletRequest request) {
		if (LOG.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer("HttpServletRequest Parameters:\n");
			sb.append("--------------------------------------------------\n");
			Enumeration names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				sb.append(name).append(": ");
				sb.append(request.getParameter(name)).append("\n");
			}
			sb.append("--------------------------------------------------");
			LOG.debug(sb.toString());
		}
	}

    /**
	 * Retrieves a property on a bean in the page context as a string.
	 *
	 * @param pageContext the page context to retrieve the bean from.
	 * @param beanName the name of the bean on the page context.
	 * @param property the property on the bean.
	 * @return a property on a bean in the page context as a string.
	 * @throws javax.servlet.jsp.JspException if something went wrong retrieving the property.
	 */
	public static String retrievePropertyOnBeanAsString(PageContext pageContext, String beanName, String property)
		throws JspException {
		Object beanProp = "TODO";// TODO RequestUtils.lookup(pageContext, beanName, property, null);
		String value = ConvertUtils.convert(beanProp);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Converted " + beanProp + "to " + value);
		}
		return value;
	}
}