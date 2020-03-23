package no.stelvio.presentation.security.page;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import no.stelvio.presentation.security.page.constants.Constants;

import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.execution.RequestContextHolder;

/**
 * PageAuthenticationRequest.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class PageAuthenticationRequest {

	private String urlUponSuccessfulAuthentication;
	private String jsfViewId;
	private String securedUrlPattern;
	private Map originalPageParameterMap;

	/**
	 * Creates a new instance of PageAuthenticationRequest.
	 * 
	 * @param securedUrlPattern
	 *            pattern
	 */
	public PageAuthenticationRequest(String securedUrlPattern) {
		this.securedUrlPattern = securedUrlPattern;
	}

	/**
	 * Get jsfViewId.
	 * 
	 * @return jsfViewId
	 */
	public String getJsfViewId() {
		return jsfViewId;
	}

	/**
	 * Get securedUrlPattern.
	 * 
	 * @return securedUrlPattern
	 */
	public String getSecuredUrlPattern() {
		return securedUrlPattern;
	}

	/**
	 * Get urlUponSuccessfulAuthentication.
	 * 
	 * @return urlUponSuccessfulAuthentication
	 */
	public String getUrlUponSuccessfulAuthentication() {
		return urlUponSuccessfulAuthentication;
	}

	/**
	 * Set jsfViewId.
	 * 
	 * @param jsfViewId
	 *            jsfViewId
	 */
	public void setJsfViewId(String jsfViewId) {
		this.jsfViewId = jsfViewId;
	}

	/**
	 * Set securedUrlPattern.
	 * 
	 * @param securedUrlPattern
	 *            securedUrlPattern
	 */
	public void setSecuredUrlPattern(String securedUrlPattern) {
		this.securedUrlPattern = securedUrlPattern;
	}

	/**
	 * Send response.
	 *
     */
	public void sendRequest() {

		ExternalContext ctx = RequestContextHolder.getRequestContext().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) ctx.getNativeRequest();

		String j2eeContextPath = request.getContextPath();

		HttpSession session = request.getSession();
		session.setAttribute(Constants.JSFPAGE_TOGO_AFTER_AUTHENTICATION, this.urlUponSuccessfulAuthentication);

		// Use externalContext redirect as this also invokes the facescontext.responseComplete

		ctx.requestExternalRedirect(j2eeContextPath + this.securedUrlPattern);
	}

	/**
	 * Get parameter.
	 * 
	 * @param key
	 *            key
	 * @return parameter
	 */
	private String getParameter(String key) {
		if (originalPageParameterMap != null) {
			return originalPageParameterMap.get(key) != null ? (String) originalPageParameterMap.get(key) : null;
		}

		return null;
	}

	/**
	 * Add parameter from original page request.
	 * 
	 * @param paramName
	 *            param name
	 */
	public void addParameterFromOriginalPageRequest(String paramName) {
		String paramValue = getParameter(paramName);
		if (paramValue != null) {
			this.securedUrlPattern = this.securedUrlPattern + "?" + paramName + "=" + paramValue;
		}
	}

	/**
	 * Get original page parameter map.
	 * 
	 * @return map
	 */
	public Map getOriginalPageParameterMap() {
		return originalPageParameterMap;
	}

	/**
	 * Set original page parameter map.
	 * 
	 * @param originalPageParameterMap
	 *            map
	 */
	public void setOriginalPageParameterMap(Map originalPageParameterMap) {
		this.originalPageParameterMap = originalPageParameterMap;
	}

	/**
	 * Set url upon successful authentication.
	 * 
	 * @param urlUponSuccessfulAuthentication
	 *            url
	 */
	public void setUrlUponSuccessfulAuthentication(String urlUponSuccessfulAuthentication) {
		this.urlUponSuccessfulAuthentication = urlUponSuccessfulAuthentication;
	}
}
