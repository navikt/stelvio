package no.stelvio.presentation.security.eai.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.HttpRequestHandler;

import no.stelvio.presentation.security.eai.Authentication;

/**
 * Handles requests to the Eai.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * @see Authentication
 * 
 */
public class EaiRequestHandler implements HttpRequestHandler, InitializingBean {

	private Authentication authMethod;

	@Override
	public void afterPropertiesSet() {
		if (authMethod == null) {
			throw new IllegalArgumentException("An authentication method must be set.");
		}
	}

	/**
	 * Handle a request to the Eai.
	 * 
	 * @param request
	 *            the HttpServletRequest
	 * @param response
	 *            the HttpServletResponse
	 */
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
		authMethod.setRequest(request);
		authMethod.setResponse(response);
		authMethod.authenticate();
	}

	/**
	 * Returns the atuthentication method.
	 * 
	 * @return the authentication method
	 */
	public Authentication getAuthMethod() {
		return authMethod;
	}

	/**
	 * Sets the authentication method.
	 * 
	 * @param authMethod
	 *            the authentication method to set
	 */
	public void setAuthMethod(Authentication authMethod) {
		this.authMethod = authMethod;
	}
}
