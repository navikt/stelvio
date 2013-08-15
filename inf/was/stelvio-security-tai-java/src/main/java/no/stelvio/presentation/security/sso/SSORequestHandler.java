package no.stelvio.presentation.security.sso;

import javax.servlet.http.HttpServletRequest;
/**
 * Interface for a single-sign-on request handler which can determine whether the current request should be
 * acted on and get the representation of a userprincipal.
 * 
 * @author persondab2f89862d3
 */
public interface SSORequestHandler {
	/**
	 * Decides whether any action should be done on the current request.
	 *  
	 * @param request the current request
	 * @return true if any action should be done, false otherwise.
	 */
	boolean actOnRequest(HttpServletRequest request);
	/**
	 * Gets the representation of a userprincipal from the current request if such a representation is
	 * present.
	 * @param request the current request
	 * @return the representation of a userprincipal if one is present
	 */
	Object getPrincipalRepresentation(HttpServletRequest request);
	/**
	 * Gets a value from the request which could be a header, parameter, attribute or sessionattribute 
	 * @param request the current request
	 * @param key the key used to retrieve the value
	 * @return the value correponding to the key from the current request.
	 */
	Object getValueFromRequest(HttpServletRequest request, String key);
	
	/**
	 * Gets the type of value to extract from the request.
	 * 
	 * @return the request value type.
	 */
	RequestValueType getRequestValueType();
}
