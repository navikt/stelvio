package no.stelvio.presentation.security.eai;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Authentication.
 * 
 * @version $Id$
 */
public interface Authentication {
	
	/**
	 * Set request.
	 * 
	 * @param request request
	 */
	void setRequest(HttpServletRequest request);

	/**
	 * Set response.
	 * 
	 * @param response response
	 */
	void setResponse(HttpServletResponse response);
	
	/**
	 * Get request.
	 * 
	 * @return request
	 */
	HttpServletRequest getRequest();
		
	/**
	 * Get response.
	 * 
	 * @return response
	 */
	HttpServletResponse getResponse();
	
	/**
	 * Authenticate.
	 */
	void authenticate();
	
	/**
	 * Put EAI header on response.
	 */
	void putEaiHeadersOnResponse();
}
