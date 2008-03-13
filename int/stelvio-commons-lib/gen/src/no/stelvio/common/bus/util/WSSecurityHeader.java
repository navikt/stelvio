/*
 * Created on Mar 12, 2008
 *
 */
package no.stelvio.common.bus.util;

import java.util.HashMap;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.ibm.websphere.security.auth.WSSubject;
import com.ibm.websphere.security.auth.callback.WSCallbackHandlerImpl;
import com.ibm.websphere.security.cred.WSCredential;
import com.ibm.ws.security.util.PasswordUtil;
import com.ibm.ws.webservices.engine.xmlsoap.SOAPFactory;
import com.ibm.ws.webservices.engine.encoding.Base64;
import com.ibm.ws.webservices.engine.xmlsoap.Name;
import com.ibm.ws.webservices.engine.xmlsoap.SOAPElement;


/**
 * @author persona2c5e3b49756 Schnell
 *
 */
public class WSSecurityHeader {

    private static String LTPAns = "wsst";
    private static String LTPAid = ":LTPA";
    private static String LTPAURL =  "http://www.ibm.com/websphere/appserver/tokentype/5.0.2";
    private static String passwordType ="http:// docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
    private static String SecurityURL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private static String SOAPEnvURL = "http://schemas.xmlsoap.org/soap/envelope/";
    private static HashMap tokenCache=new HashMap();

	private final static String className = WSSecurityHeader.class.getName();
	private final static Logger log = Logger.getLogger(className);
    
    // Create LTPA token header
    public static Object createLTPAHeader(String userName, String password){
    	try {
    		if (!tokenCache.containsKey(userName+password)){
	    		Subject currentSubject;
				currentSubject = WSSubject.getRunAsSubject();
				String decodedPassword=password;
				if (password.startsWith("{")) 
						decodedPassword=PasswordUtil.decode(password);
	    		setSecurityContext(userName, decodedPassword);
	        	tokenCache.put(userName+password, getSecurityToken());
	        	WSSubject.setRunAsSubject(currentSubject);
			}    	
			return createLTPAHeaderFromToken((byte[])tokenCache.get(userName+password));
    	}catch(Exception e){
    		log.logp(Level.SEVERE, className, "createLTPAHeader", "Error while setting RunAs subject for soap header. Using empty security header");
    		throw new RuntimeException ("Error while setting RunAs subject for soap header.",e); 
    	}
    }
    
    /**
     * @return
     */
    public static Object createLTPAHeader(){
    	// First try to get the token itself
        byte[] token = getSecurityToken();
        if(token == null)
        {	
        	log.logp(Level.SEVERE, className, "createLTPAHeader", "Return empty security header!");
        	return null;
        }
        return createLTPAHeaderFromToken(token);
    }		
    
    /**
     * @param token
     * @return
     */
    private static SOAPElement createLTPAHeaderFromToken(byte token[]){			
        // Now try to build the header
        SOAPElement header = null;
        try{
			
            // Create header Element
        	SOAPFactory sFactory = (SOAPFactory) SOAPFactory.newInstance();
            Name headerName = (Name) sFactory.createName("Security","wsse",SecurityURL);
            header = (SOAPElement) sFactory.createElement(headerName);
            Name mustName = (Name) sFactory.createName("mustUnderstand","soapenv",SOAPEnvURL);
            header.addAttribute(mustName,"1");

			
            // Create and add binary token Element
            Name binaryTokenName = (Name) sFactory.createName("BinarySecurityToken","wsse",SecurityURL);

            SOAPElement binaryToken = (SOAPElement) sFactory.createElement(binaryTokenName);
            header.addChildElement(binaryToken);
			
            // Populate token
            binaryToken.addNamespaceDeclaration(LTPAns,LTPAURL);
            Name attrName = (Name) sFactory.createName("ValueType");
            binaryToken.addAttribute(attrName,LTPAns+LTPAid);
            String stoken = Base64.encode(token);
            binaryToken.addTextNode(stoken);	

        }
        catch(Exception e){
            throw new RuntimeException("Error building LTPA security header", e);            
        }
        return header;

    }
	
    // Create BasicAuth header
    /**
     * @param user
     * @param password
     * @return
     */
    public static SOAPElement createBasicAuth(String user, String password){

        // Make sure we have the credentials
        if((user == null) || (password == null)){

            return null;
        }

        // Now try to build the header
        SOAPElement header = null;
        try{
			
            // Create header Element
            SOAPFactory sFactory = (SOAPFactory) SOAPFactory.newInstance();
            Name headerName = (Name) sFactory.createName("Security","wsse",SecurityURL);
            header = (SOAPElement) sFactory.createElement(headerName);
            Name mustName = (Name) sFactory.createName("mustUnderstand","soapenv",SOAPEnvURL);
            header.addAttribute(mustName,"1");

            // Create and add userName token Element
            Name userTokenName =(Name) sFactory.createName("UsernameToken","wsse",SecurityURL);
            SOAPElement userToken = (SOAPElement) sFactory.createElement(userTokenName);
            header.addChildElement(userToken);

            // Populate token
            Name userElementName =	(Name) sFactory.createName("Username","wsse",SecurityURL);
            Name passwordElementName =	(Name) sFactory.createName("Password","wsse",SecurityURL);		

            SOAPElement userElement = (SOAPElement) sFactory.createElement(userElementName);
            userElement.addTextNode(user);
            SOAPElement passwordElement = (SOAPElement) sFactory.createElement(passwordElementName);
            Name attrName = (Name) sFactory.createName("Type");
            passwordElement.addAttribute(attrName,passwordType);
            passwordElement.addTextNode(password);
			
            userToken.addChildElement(userElement);
            userToken.addChildElement(passwordElement);
        }
        catch(Exception e){
    		log.logp(Level.SEVERE, className, "createBasicAuth", "Error building Basic Auth security header");        	
            e.printStackTrace();
        }
        return header;
    }
    /**
     * Get security token
     * @return
     */
    public static byte[] getSecurityToken(){

    	byte[] token = null;
        try{
            // Get current security subject
            Subject security_subject = WSSubject.getRunAsSubject();
            if (security_subject != null){
                // Get all security credentials from the security subject
                Set security_credentials = security_subject.getPublicCredentials(WSCredential.class);
                // Get the first credential

                WSCredential security_credential = (WSCredential)security_credentials.iterator().next();
                String user = (String) security_credential.getSecurityName();
                // Get the security token
                token = security_credential.getCredentialToken();
            	log.logp(Level.FINE, className, "getSecurityToken", "My data from the Caller credential is:  "+ user + " security token is: "+token.toString());
            }
            else
            {	
            	log.logp(Level.FINE, className, "getSecurityToken", "Maybe security is not enabled - security object context is null");
            }
        }
        catch (Exception e){
            throw new RuntimeException("Error getting token",e);
        }
        return token;
    }
	
    /**
     * @param token
     */
    public static void setSecurityContext(byte[] token){
	
        if(token == null){
            throw new RuntimeException("Cannot create LoginContext. No token");

        }
        LoginContext lc = null;
            
        // Create new login context
        try {
            lc = new LoginContext("WSLogin",new WSCallbackHandlerImpl(token));

        } 
        catch (LoginException le) {
            throw new RuntimeException("Cannot create LoginContext. " ,le);

        } 
        catch(SecurityException se) {

           throw new RuntimeException("Cannot create LoginContext.",se);            
        }

        // Login with the new context
        try{
            lc.login(); 
        } 
        catch(LoginException le){
           throw new RuntimeException("Fails to Login. " + le.getMessage(),le);            
        }

        // Get security subject
        Subject security_subject = lc.getSubject();
		
        // Set security suject
        try{
            WSSubject.setRunAsSubject(security_subject);
        }        
        catch (Exception e){
            throw new RuntimeException("Error Setting security credentials. " + e.getMessage(),e);
        }

    }

    /**
     * @param user
     * @param password
     */
    public static void setSecurityContext(String user, String password){
	
        if((user == null) || (password == null)){
            throw new RuntimeException("Cannot create LoginContext. No token");           
        }
        LoginContext lc = null;
            
        // Create new login context
        try {
            lc = new LoginContext("WSLogin",new WSCallbackHandlerImpl(user, password));

        } 
        catch (Exception se) {

            throw new RuntimeException("Failed to create login context for user "+user+" message: "+se.getMessage(),se);
        }

        // Login with the new context
        try{
            lc.login(); 
        } 
        catch(LoginException le){
            throw new RuntimeException("Failed to login user "+user+" message: "+le.getMessage(),le);
        }

        // Get security subject
        Subject security_subject = lc.getSubject();
		
        // Set security suject
        try{
            WSSubject.setRunAsSubject(security_subject);
        }
        catch (Exception e){
            throw new RuntimeException("Unable to set security credentials for user "+user,e);
        }
    }
}
