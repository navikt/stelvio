/*
 * Created on Oct 10, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.stelvio.common.bus.util;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;
import com.ibm.websphere.security.auth.callback.WSCallbackHandlerImpl;

/**
 * @author lsb2812
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FGSAKCredentials {

	final static String FGSAKRUNAS_CONTEXT = "cell/persistent/binding/FGSAK/Credentials";
	final static String FGSAKRUNAS_SPLITCHAR = "#";
	
	final static String FGSAKREALM_ELEMENT = "REALM=";
	final static String FGSAKENDUSER_ELEMENT = "ENDUSER=";
	final static String FGSAKCREDENTIAL_ELEMENT = "CREDENTIAL=";
	
	private Subject originalRunAsSubject = null;
	private String targetRealm = null;
	private String username = null;
	private String password = null;
	
	private final static String className = FGSAKCredentials.class.getName();
	private final Logger log = Logger.getLogger(className);
	
	/**
	 * @return Returns the password.
	 */
	@SuppressWarnings("unused")
	private String getPassword() {
		log.logp(Level.FINE, className, "getPassword()", "->" + password);
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	private void setPassword(String password) {
		log.logp(Level.FINE, className, "setPassword()", "->" + password);
		this.password = password;
	}
	/**
	 * @return Returns the targetRealm.
	 */
	private String getTargetRealm() {
		log.logp(Level.FINE, className, "getTargetRealm()", "->" + targetRealm);
		return targetRealm;
	}
	/**
	 * @param targetRealm The targetRealm to set.
	 */
	private void setTargetRealm(String targetRealm) {
		log.logp(Level.FINE, className, "setTargetRealm()", "->" + targetRealm);
		this.targetRealm = targetRealm;
	}
	/**
	 * @return Returns the username.
	 */
	private String getUsername() {
		log.logp(Level.FINE, className, "getUsername()", "->" + username);
		return username;
	}
	/**
	 * @param username The username to set.
	 */
	private void setUsername(String username) {
		log.logp(Level.FINE, className, "setUsername()", "->" + username);
		this.username = username;
	}
	
	/**
	 * @see Set the new FGSAK context based on the defined Name Space Binding
	 * @throws WSSecurityException, LoginException, NamingException
	 */
	public void changeRunAsSubject() throws WSSecurityException, LoginException, NamingException {
		originalRunAsSubject = WSSubject.getRunAsSubject();
		if (originalRunAsSubject != null)
		{	
			log.logp(Level.FINE, className, "changeRunAsSubject()", "-> Call getNameSpace() with " + FGSAKRUNAS_CONTEXT + " to get the context.");
			getNameSpace();
			log.logp(Level.FINE, className, "changeRunAsSubject()", "-> Set new loginContext based on targetRealm="+getTargetRealm()+" and securityName="+getUsername()+ " values.");
			//CallbackHandler loginCallbackHandler = new WSCallbackHandlerImpl("srvPensjon", "10.80.5.2:389", "pwd");
			CallbackHandler loginCallbackHandler = new WSCallbackHandlerImpl(username, targetRealm, password);
			LoginContext lc = new LoginContext("WSLogin",loginCallbackHandler);
			lc.login();
			WSSubject.setRunAsSubject(lc.getSubject());
			log.logp(Level.FINE, className, "changeRunAsSubject()", "-> Login with new subject based on targetRealm="+getTargetRealm()+" and securityName="+getUsername()+ " succeeds.");
		}
		else
		{
			log.logp(Level.FINE, className, "changeRunAsSubject()", "-> No security enabled on server. Don't excecute implementation.");
		}
		 	
	}
	
	public void restoreRunAsSubject() throws WSSecurityException {
		if (originalRunAsSubject != null)
		{
				WSSubject.setRunAsSubject(originalRunAsSubject);
				log.logp(Level.FINE, className, "restoreRunAsSubject()", "-> Restore original login context succeeds.");
		}
		else
		{
			log.logp(Level.FINE, className, "restoreRunAsSubject()", "-> No security enabled on server. Don't excecute implementation.");
		}
	}

	/**
	 * @see Get the JNDI name space context for the login format REALM=10.80.5.2:389#ENDUSER=pselvUsr#CREDENTIAL=pwd
	 * @throws NamingException
	 */
	private void getNameSpace() throws NamingException
	{
		log.logp(Level.FINE, className, "getNameSpace()", "-> Try to retrieve Name Space Binding for " + FGSAKRUNAS_CONTEXT);
		Context ctx = new InitialContext();
		String lookup = (String) ctx.lookup(FGSAKRUNAS_CONTEXT);
		StringTokenizer st = new StringTokenizer(lookup,FGSAKRUNAS_SPLITCHAR, false);
		while (st.hasMoreTokens())
		{
			String element = st.nextToken();
			log.logp(Level.FINE, className, "getNameSpace()", "--> Element:" +element);
			
			if (element.indexOf(FGSAKREALM_ELEMENT) != -1)
			{	
				setTargetRealm(element.substring(element.lastIndexOf("=")+1, element.length()));
			}	
			if (element.indexOf(FGSAKENDUSER_ELEMENT) != -1)
			{
				setUsername(element.substring(element.lastIndexOf("=")+1, element.length()));
			}
			if (element.indexOf(FGSAKCREDENTIAL_ELEMENT) != -1)
			{
				setPassword(element.substring(element.lastIndexOf("=")+1, element.length()));
			}
		}
		log.logp(Level.FINE, className, "getNameSpace()", "-> Retrieved Name Space Binding for " + FGSAKRUNAS_CONTEXT + " done without error. Check content!");
	}
}
