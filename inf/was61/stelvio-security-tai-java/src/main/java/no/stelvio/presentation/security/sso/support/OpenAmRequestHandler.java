package no.stelvio.presentation.security.sso.support;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import no.stelvio.presentation.security.sso.RequestValueKeys;
import no.stelvio.presentation.security.sso.RequestValueType;
import no.stelvio.presentation.security.sso.SSORequestHandler;
import no.stelvio.presentation.security.sso.accessmanager.support.DefaultStelvioPrincipal;
/**
 * An implementation of the SSORequestHandler which by default targets requests from openAM. 
 * It can determine whether the current request should be acted on and get the representation 
 * of a userprincipal depending on the values present in the request. When requests are routed through
 * openAm the openam-sessionid is placed in a cookie, so by default the RequestValueType,
 * i.e. the type of value to retrieve from the request, is set to RequestValueType.COOKIE. 
 * 
 * @author person0a5e006fe6fb Hilstad
 * @see SSORequestHandler
 * @see RequestValueType
 */
public class OpenAmRequestHandler implements SSORequestHandler {

	//private Properties requestValueKeyConfig;
	private RequestValueType requestValueType = defaultRequestValueType;
	private static RequestValueType defaultRequestValueType = RequestValueType.COOKIE;
	private Logger log;
	private RequestValueKeys requestValueKeys;
	
	
	/**
	 * Initializes the logger.
	 */
	public OpenAmRequestHandler(){
		log = Logger.getLogger("no.stelvio.presentation.security.sso.ibm.OpenAmRequestHandler");
	}
	
	/**
	 * Private helper method which prints out the headers in the current request.
	 * @param request the current request.
	 */
	private void printHeaders(HttpServletRequest request){
		if(log.isLoggable(Level.FINER)){
			log.finer("Listing values in request.");
			if(requestValueType == RequestValueType.COOKIE){
				Cookie[] cookies = request.getCookies();
				for (int i=0; i<cookies.length;i++) {
					log.finer("Found cookie:" + cookies[i].getName() + " - " + cookies[i].getValue());	
				}
			}
			
		}	
	}

	/**
	 * Decides whether the current request should be acted on based on whether or
	 * not the request contains values for the id of the access-manager user and the original user.
	 * If these values are present and the access-manager userid equals the access-manager userid
	 * configured, the method will return true or otherwise false.
	 * 
	 * @param request the current request 
	 * @return <b>true</b> if the proper request values are present and correct, <b>false</b> otherwise.
	 */
	public boolean actOnRequest(HttpServletRequest request) {
		printHeaders(request);
		if(log.isLoggable(Level.FINE)){
			log.fine("Checking for request values of type:" 
				+ this.requestValueType);
		}
		if(isRequestValuesPresent(request)){		
			if(log.isLoggable(Level.FINE)){
				log.fine("All request values present.");
			}
			return true;
		} else {
			if(log.isLoggable(Level.FINE)){
				log.fine("This request did not contain the required attributes for a single sign on." );
			}
			return false;
		}		
	}
	
	private boolean isRequestValuesPresent(HttpServletRequest request){
		return isRequestValuePresent(request, requestValueKeys.getCookieKey());
	}
	
	/**
	 * Extracts request values representing a user's authentication data from the current request 
	 * and returns a userprincipal representation.
	 * 
	 * @param request the current request
	 * @return a userprincipal representation of type StelvioPrincipal
	 * @see DefaultStelvioPrincipal
	 *   
	 */
	public Object getPrincipalRepresentation(HttpServletRequest request) {
		// printHeaders(request);
		String sessionId = getCookieValueAsString(request, requestValueKeys.getCookieKey());

		if (log.isLoggable(Level.FINE)) {
			String values = requestValueKeys.getCookieKey() + " = " + sessionId;
			log.fine("Extracting the following values from the request: "
					+ values);
			log.fine("Request:" + request.getRequestURL() + "(?)"
					+ request.getQueryString());
		}

		return (!isEmpty(sessionId)) ? sessionId : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getValueFromRequest(HttpServletRequest request, String key) {
		if( requestValueType == RequestValueType.COOKIE){
			return getCookieValue(request, key);
		} 
		return null;
	}
	/**
	 * Private helper method which checks if a string is <b>null</b> or blank.
	 * @param value the string to check
	 * @return true if string is null or blank, false otherwise.
	 */
	private boolean isEmpty(String value){
		return (value == null || value.equalsIgnoreCase(""));
	}
	/**
	 * Private helper method which checks if a value is present in the request.
	 * @param request the current request
	 * @param entry the configuration entry for the value to check 
	 * @return true if the request value is present, false otherwise
	 */
	private boolean isRequestValuePresent(HttpServletRequest request, String key){
		return (getRequestValue(request, key) != null);
	}
	/**
	 * Private helper method which casts the supplied object to a String. If the object is not 
	 * of type String the object's toString() method will be returned unless the object is null. 
	 * If the object is null an empty String will be returned.
	 * 
	 * @param value the object to cast
	 * @return a String object
	 */
	private String castToString(Object value){
		return (value instanceof String) ? (String) value : 
				((value != null) ? value.toString() : "" );
	}
	/**
	 * Private helper method which extracts a request value specied by the 
	 * configuration entry parameter as a String.
	 * 
	 * @param request the current request
	 * @param entry the configuration entry for the value to extract. 
	 * @return the request value as a String.
	 */
	private String getCookieValueAsString(HttpServletRequest request, String key){
		Object value = getCookieValue(request, key);
		return castToString(value);
	}	
	/**
	 * Private helper method to extract a request value from the current request using
	 * a configuration entry.
	 * 
	 * @param request the current request.
	 * @param entry the configuration entry for the value to extract.
	 * @return the request value
	 */
	private Object getRequestValue(HttpServletRequest request, String key){	
		return getValueFromRequest(request, key);
	}
	/**
	 * Private helper method which extracts the value specified by the key parameter 
	 * from the HttpSession.
	 * 
	 * @param request the current request
	 * @param key the key for the value to extract
	 * @return an object from the HttpSession.
	 */
	private Object getCookieValue(HttpServletRequest request, String key){
		Cookie[] cookies= request.getCookies();
		String value = null;
		for(int i=0; i<cookies.length;i++) {
			if(cookies[i].getName().equals(requestValueKeys.getCookieKey())) {
				value = cookies[i].getValue(); 
				break;
			}
		}
		return value;
	}

	/**
	 * Gets the type of value to extract from the request.
	 * 
	 * @return the request value type.
	 */
	public RequestValueType getRequestValueType() {
		return requestValueType;
	}
	/**
	 * Sets the type of value to extract from the request.

	 * @param requestValueType the requestValueType to set.
	 */
	public void setRequestValueType(RequestValueType requestValueType) {
		this.requestValueType = requestValueType;
	}

	/**
	 * @return the requestValueKeys
	 */
	public RequestValueKeys getRequestValueKeys() {
		return requestValueKeys;
	}

	/**
	 * @param requestValueKeys the requestValueKeys to set
	 */
	public void setRequestValueKeys(RequestValueKeys requestValueKeys) {
		this.requestValueKeys = requestValueKeys;
	}
	
}
