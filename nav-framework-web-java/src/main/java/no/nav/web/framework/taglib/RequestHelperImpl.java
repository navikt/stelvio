package no.nav.web.framework.taglib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.util.Href;
import org.displaytag.util.RequestHelper;

/**
 * Implementation of the <code>RequestHelper</code>. Differs from the default implementation of
 * this interface in the displaytag library in that it does not conserve the parameters in the
 * request when creating a new link.
 * 
 * @author Christian Rømming, Accenture
 */
public class RequestHelperImpl implements RequestHelper {

	/**
	 * logger.
	 */
	private static Log log = LogFactory.getLog(RequestHelperImpl.class);

	/**
	 * Pattern of displaytag parameters
	 */
	private static final Pattern DISPLAYTAG_PATTERN = Pattern.compile("^d-\\d+-");

	/**
	 * original HttpServletRequest.
	 */
	private HttpServletRequest request;

	/**
	 * original HttpServletResponse.
	 */
	private HttpServletResponse response;

	/**
	 * Construct a new RequestHelper for the given request.
	 * @param servletRequest HttpServletRequest needed to generate the base href
	 * @param servletResponse HttpServletResponse needed to encode generated urls
	 */
	public RequestHelperImpl(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		this.request = servletRequest;
		this.response = servletResponse;
	}

	/**
	 * @see org.displaytag.util.RequestHelper#getHref()
	 */
	public Href getHref() {
		String requestURI = this.request.getRequestURI();
		// call encodeURL to preserve session id when cookies are disabled
		Href href = new Href(this.response.encodeURL(requestURI));
		href.setParameterMap(getParameterMap());
		return href;
	}

	/**
	 * @see org.displaytag.util.RequestHelper#getParameter(java.lang.String)
	 */
	public String getParameter(String key) {
		// actually simply return the parameter, this behaviour could be changed
		String attribute = (String) request.getAttribute(key);
		if (null != attribute) {
			return attribute;
		}
		return request.getParameter(key);
	}

	/**
	 * @see org.displaytag.util.RequestHelper#getIntParameter(java.lang.String)
	 */
	public Integer getIntParameter(String key) {
		String value = getParameter(key);
		if (value != null) {
			try {
				return new Integer(value);
			} catch (NumberFormatException e) {
				// It's ok to ignore, simply return null
				log.debug("Invalid \"" + key + "\" parameter from request: value=\"" + value + "\"");
			}
		}
		return null;
	}

	/**
	 * Functionality changed from returning all parameters to returning just the 
	 * parameters that have to do with the displaytag.
	 * 
	 * @see org.displaytag.util.RequestHelper#getParameterMap()
	 */
	public Map getParameterMap() {

		// Declare the map to be returned
		Map map = new HashMap();

		// Get the request parameters
		Map parameters = this.request.getParameterMap();

		// If there are request parameters, iterate through these and check them
		// against the displaytag parameter pattern (defined above)
		if (parameters != null && !parameters.isEmpty()) {

			for (Iterator i = parameters.entrySet().iterator(); i.hasNext();) {

				Map.Entry me = (Map.Entry) i.next();

				String name = (String) me.getKey();

				// If there is a match, add the parameter to the return map
				if (StringUtils.isNotEmpty(name) && DISPLAYTAG_PATTERN.matcher(name).find()) {
					map.put(name, me.getValue());
				}
			}
		}
		// Return the map
		return map;
	}
}