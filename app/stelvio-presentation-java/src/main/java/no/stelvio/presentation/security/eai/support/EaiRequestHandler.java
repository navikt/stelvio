package no.stelvio.presentation.security.eai.support;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.stelvio.presentation.security.eai.Authentication;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.HttpRequestHandler;

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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
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
	 * @throws ServletException servlet exception
	 * @throws IOException ioexception
	 */
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
