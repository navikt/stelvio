package no.stelvio.presentation.security.sso.support;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import no.stelvio.presentation.security.sso.RequestValueKeys;
import no.stelvio.presentation.security.sso.RequestValueType;
import no.stelvio.presentation.security.sso.SSORequestHandler;
import no.stelvio.presentation.security.sso.accessmanager.support.DefaultStelvioPrincipal;
/**
 * An implementation of the SSORequestHandler which by default targets requests from WebSEAL. 
 * It can determine whether the current request should be acted on and get the representation 
 * of a userprincipal depending on the values present in the request. When requests are routed through
 * WebSEAL the authentiction data is placed in http headers, so by default the RequestValueType,
 * i.e. the type of value to retrieve from the request, is set to RequestValueType.HEADER. However,
 * this implementation can also make its decisions on other request value types. This can be achieved 
 * by setting the RequestValueType property and can come in handy especially in environments where 
 * there is no WebSEAL instance. This implementation also supports the use of PAC headers from WebSEAL.
 * PAC is an IBM properitary encoded data-structure and is represented in the header as a string of bytes. 
 * 
 * @author persondab2f89862d3
 * @see SSORequestHandler
 * @see RequestValueType
 */
public class WebSealRequestHandler implements SSORequestHandler {

	//private Properties requestValueKeyConfig;
	private boolean usePacHeader = false;
	private String accessManagerUser;
	private RequestValueType requestValueType = defaultRequestValueType;
	private static RequestValueType defaultRequestValueType = RequestValueType.HEADER;
	private Logger log;
	private RequestValueKeys requestValueKeys;
	
	
	/**
	 * Initializes the logger.
	 */
	public WebSealRequestHandler(){
		log = Logger.getLogger("no.stelvio.presentation.security.sso.ibm.WebSealRequestHandler");
	}
	
	/**
	 * Enumeration representing the keys for the entries in the configuration
	 * for the request values to extract from the request.
	 */
	/*private enum ConfigEntry {
		PAC,
		ORIGINAL_USER_NAME,
		AUTHENTICATION_LEVEL,
		ACCESS_MANAGER_USER;
	}*/
	/**
	 * Private helper method which prints out the headers in the current request.
	 * @param request the current request.
	 */
	private void printHeaders(HttpServletRequest request){
		if(log.isLoggable(Level.FINER)){
			log.finer("Listing values in request.");
			if(requestValueType == RequestValueType.HEADER){
				for (Enumeration enumeration = request.getHeaderNames(); enumeration.hasMoreElements() ;) {
					String headerName = (String)enumeration.nextElement();
					log.finer("Found header:" + headerName + " - " + request.getHeader(headerName));	
				}
			} else if(requestValueType == RequestValueType.SESSION_ATTRIBUTE){
				HttpSession session = request.getSession();
				
				for (Enumeration enumeration = session.getAttributeNames(); enumeration.hasMoreElements() ;) {
					String attrName = (String)enumeration.nextElement();
					log.finer("Found session attribute:" + attrName + " - " + session.getAttribute(attrName));	
				}
			}
			
		}	
	}
	/**
	 * Gets the configuration for the names of the request values to extract from the request.
	 * @return the requestValueKeyConfig
	 */
	/*public Properties getRequestValueKeyConfig() {
		return requestValueKeyConfig;
	}*/
	/**
	 * Sets the configuration for the names of the request values to extract from the request.
	 * @param requestValueKeyConfig the requestValueKeyConfig to set.
	 */
	/*public void setRequestValueKeyConfig(Properties requestValueKeyConfig) {
		this.requestValueKeyConfig = requestValueKeyConfig;
	}*/
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
				log.fine("All request values present. Validating the access manager username.");
			}
			if(validAccessManagerUser(request)){
				if(log.isLoggable(Level.FINE)){
					log.fine("Valid username found. Continuing to establish trust." );
				}
				return true;
			} else {
				if(log.isLoggable(Level.FINE)){
					log.fine("No valid access manager username found."
							+ " The Tivoli Access Manager username did not match the ID configured in the interceptor."
							+ " The interceptor will not act on this request." );
				}
				return false;
			}
		} else {
			if(log.isLoggable(Level.FINE)){
				log.fine("This request did not contain the required attributes for a single sign on." );
			}
			return false;
		}
	}
	
	private boolean validAccessManagerUser(HttpServletRequest request){
		String amUser = getRequestValueAsString(request, requestValueKeys.getAccessManagerUserKey());
		return amUser.equalsIgnoreCase(this.accessManagerUser);
	}
	
	private boolean isRequestValuesPresent(HttpServletRequest request){
		return (isRequestValuePresent(request, requestValueKeys.getOriginalUserNameKey()) 
				&& isRequestValuePresent(request, requestValueKeys.getAuthenticationLevelKey())
				&& isRequestValuePresent(request, requestValueKeys.getAccessManagerUserKey()));
	}
	
	/**
	 * Extracts request values representing a user's authentication data from the current request 
	 * and returns a userprincipal representation. Unless PAC headers are used this will be a
	 * <code>DefaultStelvioPrincipal</code> object containing the userid and authentication level of 
	 * a user. If PAC headers are to be used the userprincipal representation will be returned 
	 * as a <code>byte[]</code> object.  
	 * 
	 * @param request the current request
	 * @return a userprincipal representation of type StelvioPrincipal or byte[] if request values are
	 * present, otherwise <b>null</b>.
	 * @see DefaultStelvioPrincipal
	 *   
	 */
	public Object getPrincipalRepresentation(HttpServletRequest request) {
		//printHeaders(request);
		if(usePacHeader){
			String pacHeader = getRequestValueAsString(request, "iv-creds");
			byte[] pac = pacHeader != null ? pacHeader.getBytes() : null;
			if(log.isLoggable(Level.FINE)){
				log.fine("Extracting requestValue with name:" + "iv-creds");
				log.fine("The value is:" + pac );
			}
			return pac;
		} else {
			
			String amUser = getRequestValueAsString(request, requestValueKeys.getAccessManagerUserKey());
			String userId = getRequestValueAsString(request, requestValueKeys.getOriginalUserNameKey());
			String authLevel = getRequestValueAsString(request, requestValueKeys.getAuthenticationLevelKey());
			String authorizedAs =  getRequestValueAsString(request, requestValueKeys.getAuthorizedAsKey());
			String authorizationType = getRequestValueAsString(request, requestValueKeys.getAuthorizationTypeKey());
			
			if(log.isLoggable(Level.FINE)){
				String values = requestValueKeys.getOriginalUserNameKey()
								+ " = " + userId + ", " + 
								requestValueKeys.getAuthenticationLevelKey() 
								+ " = " + authLevel + ", " +
								requestValueKeys.getAuthorizedAsKey()
								+ " = " + authorizedAs + ", " +
								requestValueKeys.getAuthorizationTypeKey()
								+ " = " + authorizationType;
				log.fine("Extracting the following values from the request: " + values);
				log.fine("Request:" + request.getRequestURL() + "(?)" + request.getQueryString());
			}
			
			return (!isEmpty(userId) && !isEmpty(authLevel)) ?  
					new PrincipalRepresentation(
							amUser,userId,authLevel,authorizedAs, authorizationType) 
								: null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getValueFromRequest(HttpServletRequest request, String key) {
		if (requestValueType == RequestValueType.HEADER){
			return getHeaderValue(request, key);
		} else if( requestValueType == RequestValueType.ATTRIBUTE){
			return getAttributeValue(request, key);
		} else if( requestValueType == RequestValueType.PARAMETER){
			return getParameterValue(request, key);
		} else if( requestValueType == RequestValueType.SESSION_ATTRIBUTE){
			return getSessionValue(request, key);
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
	private String getRequestValueAsString(HttpServletRequest request, String key){
		Object value = getRequestValue(request, key);
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
	private Object getSessionValue(HttpServletRequest request, String key){
		HttpSession session = request.getSession();
		return session != null ? session.getAttribute(key) : null;
	}
	/**
	 * Private helper method which extracts the attribute specified by the key parameter 
	 * from the current request.
	 * 
	 * @param request the current request
	 * @param key the key for the value to extract
	 * @return the request attribute specified by the key.
	 */
	private Object getAttributeValue(HttpServletRequest request, String key){
		return request.getAttribute(key);
	}
	/**
	 * Private helper method which extracts the request-parameter specified by the key  
	 * from the current request.
	 * 
	 * @param request the current request
	 * @param key the key for the value to extract
	 * @return the request parameter specified by the key.
	 */
	private Object getParameterValue(HttpServletRequest request, String key){
		return request.getParameter(key);
	}
	/**
	 * Private helper method which extracts the http header value specified by the key  
	 * from the current request.
	 * 
	 * @param request the current request
	 * @param key the key for the value to extract
	 * @return the http header value specified by the key.
	 */
	private Object getHeaderValue(HttpServletRequest request, String key){
		return request.getHeader(key);
	}
	/**
	 * Private helper method which extracts a configuration entry from the request value configuration. 
	 * 
	 * @param entry the entry to extract from the configuration
	 * @return the value for the configuration entry.
	 */
	/*private String getConfigEntry(ConfigEntry entry){
		return this.requestValueKeyConfig.getProperty(entry.name()) != null ?
				this.requestValueKeyConfig.getProperty(entry.name()).trim() : null;
	}*/
	/**
	 * Specifies if the request-handler should use PAC headers which is an encoded data-structure
	 * sent out from WebSEAL.
	 * @return true if PAC headers should be used, false otherwise.
	 */
	public boolean isUsePacHeader() {
		return usePacHeader;
	}
	/**
	 * Sets whether or not PAC headers should be used for this request-handler.
	 * 
	 * @param usePacHeader true or false 
	 */
	public void setUsePacHeader(boolean usePacHeader) {
		this.usePacHeader = usePacHeader;
	}
	/**
	 * Gets the type of value to extract from the request. Default is
	 * RequestValueType.HEADER.
	 * 
	 * @return the request value type.
	 */
	public RequestValueType getRequestValueType() {
		return requestValueType;
	}
	/**
	 * Sets the type of value to extract from the request. Default is
	 * RequestValueType.HEADER.
	 * @param requestValueType the requestValueType to set.
	 */
	public void setRequestValueType(RequestValueType requestValueType) {
		this.requestValueType = requestValueType;
	}
	/**
	 * Gets the userid of the access-manager user. 
	 * @return the userid of the access-manager user.
	 */
	public String getAccessManagerUser() {
		return accessManagerUser;
	}
	/**
	 * Gets the userid of the access-manager user. 
	 * @param accessManagerUser the userid of the access-manager user.
	 */
	public void setAccessManagerUser(String accessManagerUser) {
		this.accessManagerUser = accessManagerUser;
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
