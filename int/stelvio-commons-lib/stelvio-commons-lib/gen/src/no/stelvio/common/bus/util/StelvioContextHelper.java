/*
 * StelvioConctextHelper.java Created on Feb 26, 2006 Author: persona2c5e3b49756 Schnell
 * (test@example.com)
 * 
 * This is a utility class that provides context information from StelvioContext or 
 * 
 * @Updated:
 *  26-02-2006 -> initial Version
 *  08-03-2006 -> fixed NullPointerException WBISessionManager -  CC NAV00000491
 *    
 */
package no.stelvio.common.bus.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.security.auth.Subject;

import com.ibm.websphere.security.auth.WSSubject;
import com.ibm.websphere.security.cred.WSCredential;
import com.ibm.websphere.workarea.UserWorkArea;
import com.ibm.ws.session.WBISessionManager;

/*
 * <p> This is a utility class that provides helper methods around StelvioContext, useful
 * for different purpose as get an userId or the WBISessionId or other funtionality around StelvioContext</p>
 * 
* @usage 
 * <p>
 *  no.stelvio.common.bus.util.StelvioContextHelper stelvioCtx = new StelvioContextHelper();
 *  String myUserId = stelvioCtx.getUserId();
 * 
 * Don't use System.out it is only an example. * 
 * @author persona2c5e3b49756 Schnell, test@example.com
 *  
 */
public class StelvioContextHelper {

    // Sample - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	private final static String className = StelvioContextHelper.class.getName();
	private final Logger log = Logger.getLogger(className);

	//	static variables
	final static String workAreaName = "BUS_STELVIO_CONTEXT";
	final static String DEFAULT_USER_NAME = "WPS2812";
	final static String DEFAULT_LANGUAGE = "no";
	final static String DEFAULT_APPLICATION_NAME = "PEN";
	final static String DEFAULT_NO_WA  = "NO_BUS_STELVIO_CONTEXT";
	
	// initial UserWorkArea
	private UserWorkArea workArea = null;
	private InitialContext workAreaCtx = null;
	private WBISessionManager sessionManager = null; 
		
	// variables 
	private String userId = null;
	private String correlationId = null;
	private String languageId = null;
	private String applicationId = null;
	private String wbiSessionId = null;
	
	
		
	/**
	 * <p>
	 * Constructor
	 * </p>
	 * 
	 */	
	public StelvioContextHelper() {
	
		workArea= null;
		try {
		    // 1. WorkArea
			InitialContext initialContext = new InitialContext();
			workArea = (UserWorkArea) initialContext.lookup("java:comp/websphere/UserWorkArea");
			
			// 2. WBISession
			sessionManager = WBISessionManager.getInstance();
			
			// 3. Initial variables
			setStelvioBusContext();
			
		} catch (Exception e) {
			log.logp(Level.SEVERE, className, "constructor()", "CatchedError: " + getExceptionTrace(e));
		}
	}

	/**
	 * @return Returns the applicationId.
	 */
	public String getApplicationId() {
		return applicationId;
	}
	/**
	 * @param applicationId The applicationId to set.
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	/**
	 * @return Returns the corrleationId.
	 */
	public String getCorrelationId() {
		return correlationId;
	}
	/**
	 * @param corrleationId The corrleationId to set.
	 */
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	/**
	 * @return Returns the languageId.
	 */
	public String getLanguageId() {
		return languageId;
	}
	/**
	 * @param languageId The languageId to set.
	 */
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * getWBISessionId
	 */
	private String getWBISessionId()
	{
		
		if (sessionManager.isSessionExisted())
		{
			wbiSessionId = sessionManager.getSessionContext().getSessionId();	
		}
		else
		{			
			//TODO
			wbiSessionId = null;
		}

		log.logp(Level.FINE, className, "getWBISessioId()", "WBISessionId=" + wbiSessionId);
		
		return wbiSessionId;
	}
	
	/**
	 * @return internal systemUserId.
	 */
	private String getSecurityIdentity()
	{
		String tName = Thread.currentThread().getName();
		String sysUser = null;
		
		log.logp(Level.FINE, className, "getInternIdentity()", "Credential executing on the thread " + tName);
		try {
			Subject runAsSecurity = WSSubject.getRunAsSubject();
			if (runAsSecurity != null) {
				Set security_credentials = runAsSecurity.getPublicCredentials(WSCredential.class);
				WSCredential security_credential = (WSCredential)security_credentials.iterator().next();
				sysUser = (String) security_credential.getSecurityName();
			}
			else
				sysUser = DEFAULT_USER_NAME;
			
		} catch (Exception e) {
			log.logp(Level.SEVERE, className, "getInternIdentity()", "CatchedError: " + getExceptionTrace(e));
		}
		log.logp(Level.FINE, className, "getInternIdentity()", "return " + sysUser);
		return sysUser;
	}
	
	
	/**
	 * internal method to set the context
	 */
	private void setStelvioBusContext() {
		
		if (workArea != null) {
			
			String wName = workArea.getName();
			if (wName==null) wName=DEFAULT_NO_WA;
			
			if (wName.equalsIgnoreCase(workAreaName))
			{
				log.logp(Level.INFO, className, "setStelvioBusContext()", "StelvioContext exists in WorkArea");
				
				userId = (String) workArea.get("userId");
				if (userId == null || userId.length() <= 0 ) {
					userId = getSecurityIdentity(); 
				}
				
				languageId = (String) workArea.get("languageId");
				if (languageId == null || languageId.length() <= 0 ) {
					languageId = DEFAULT_LANGUAGE;
				}

				applicationId = (String) workArea.get("applicationId");
				if (applicationId == null || applicationId.length() <= 0 ) {
					applicationId = DEFAULT_APPLICATION_NAME;
				}

				correlationId = (String) workArea.get("correlationId");
				if (correlationId == null || correlationId.length() <= 0 ) {
				  correlationId = getWBISessionId();
				}

				
			}
			// another WorkArea
			else {
				log.logp(Level.FINE, className, "setStelvioBusContext()", "StelvioContext doesn't exists within WorkArea - use default values");			
				userId = DEFAULT_USER_NAME;
				languageId = DEFAULT_LANGUAGE;
				applicationId = DEFAULT_APPLICATION_NAME;
				correlationId = getWBISessionId();
			}
		}
		// set the default values in general
		else {
			log.logp(Level.FINE, className, "setStelvioBusContext()", "StelvioContext doesn't exists within WorkArea - use default values");			
			userId = DEFAULT_USER_NAME;
			languageId = DEFAULT_LANGUAGE;
			applicationId = DEFAULT_APPLICATION_NAME;
			correlationId = getWBISessionId(); 			
		}
	}

	/**
	 * <p>
	 * Pretty print the Stelvio workArea context.
	 * </p>
	 * 
	 * @param sModulIdent for logging
	 *            
	 * @return void
	 */
	public void printStelvioContext(String sModulIdent)
	{
		if (workArea != null)
		{
			log.logp(Level.INFO, className, "printStelvioContext()", "-- " + sModulIdent + ": " + workAreaName);
			log.logp(Level.INFO, className, "printStelvioContext()", "--- NameWorkArea:" + workArea.getName());
			log.logp(Level.INFO, className, "printStelvioContext()", "--- userId:" + userId);
			log.logp(Level.INFO, className, "printStelvioContext()", "--- correlationId:" + correlationId);
			log.logp(Level.INFO, className, "printStelvioContext()", "--- languageId:" + languageId);
			log.logp(Level.INFO, className, "printStelvioContext()", "--- applicationId:" + applicationId);
			log.logp(Level.INFO, className, "printStelvioContext()", "-- " + sModulIdent + ": " + workAreaName);
		}
		else
			log.logp(Level.INFO, className, "printStelvioContext()", "-- " + sModulIdent + ": Work Area <" + workAreaName + "> doesn't exists");	
	}
	
	/**
	 * Convert exception stacktrace to string
	 * 
	 * @param exception	the Exception to convert
	 * @return 	the string representation of the Exception
	 */
	private String getExceptionTrace(Exception ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
