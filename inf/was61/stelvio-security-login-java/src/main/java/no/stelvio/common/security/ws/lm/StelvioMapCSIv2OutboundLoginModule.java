package no.stelvio.common.security.ws.lm;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import com.ibm.websphere.logging.WsLevel;
import com.ibm.websphere.security.auth.WSLoginFailedException;
import com.ibm.websphere.security.auth.callback.WSCallbackHandlerImpl;
import com.ibm.ws.security.util.InvalidPasswordDecodingException;
import com.ibm.ws.security.util.PasswordUtil;
import com.ibm.ws.security.util.UnsupportedCryptoAlgorithmException;
import com.ibm.wsspi.security.auth.callback.WSProtocolPolicyCallback;
import com.ibm.wsspi.security.csiv2.CSIv2PerformPolicy;

public class StelvioMapCSIv2OutboundLoginModule implements LoginModule {

	private CSIv2PerformPolicy csiv2PerformPolicy = null;
    private Subject basicAuthSubject = null;
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Hashtable hashtable = new Hashtable();
    private Map sharedState;
    private Map options;
    private Properties props = null;
    private static final Logger logger = Logger.getLogger("no.stelvio.common.security.ws.lm.StelvioMapCSIv2OutboundLoginModule");
    
    public StelvioMapCSIv2OutboundLoginModule(){
    	
    }
    
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
        if (logger.isLoggable(WsLevel.FINE)){
            logger.log(WsLevel.FINE, " Initializing... subject: " 
            		+ (subject == null ? "" : subject.toString()) 
            		+ ", callbackHandler:" + (callbackHandler == null ? "" : callbackHandler.toString()) + ", map:" 
            		+ (sharedState == null ? "" : sharedState.toString()) + " , options:" + (options == null ? "" : options.toString()));
        }
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
    }

    public boolean login() throws LoginException {
        if (logger.isLoggable(WsLevel.FINE))
            logger.log(WsLevel.FINE, "** Starting login()");
        if (logger.isLoggable(WsLevel.FINE))
            logger.log(WsLevel.FINE, "** subject = " + subject);        
                
        //Using callbacks to get the WSProtocolPolicyCallback object, it has the information about the remote call 
        Callback callbacks[] = new Callback[1];
        callbacks[0] = new WSProtocolPolicyCallback("Protocol Policy Callback: ");

        try {
            callbackHandler.handle(callbacks);
        } catch (Exception e) {
            if (logger.isLoggable(WsLevel.FATAL))
                logger.log(WsLevel.FATAL, "** Error with the callbacks.");
            e.printStackTrace();
            return false;
        }
        
        //Receives the RMI (CSIv2) policy object for checking the target realm based upon information from the IOR. 
        csiv2PerformPolicy = (CSIv2PerformPolicy) ((WSProtocolPolicyCallback) callbacks[0]).getProtocolPolicy();

        //returns the local realm 
        if (logger.isLoggable(WsLevel.FINE))
            logger.log(WsLevel.FINE, "** csiv2PerformPolicy.getCurrentSecurityName() = "+csiv2PerformPolicy.getCurrentSecurityName());
        //returns the remote realm 
        if (logger.isLoggable(WsLevel.FINE))
            logger.log(WsLevel.FINE, "** csiv2PerformPolicy.getTargetSecurityName() = "+csiv2PerformPolicy.getTargetSecurityName());
        
        if (logger.isLoggable(WsLevel.FINE))
            logger.log(WsLevel.FINE, "Checking if the current realm matches the target security realm..");
        
        //Checks if the realms do not match. If they do not match, then log in to perform a mapping
        if (!csiv2PerformPolicy.getTargetSecurityName().equalsIgnoreCase(csiv2PerformPolicy.getCurrentSecurityName())) {
            try {
                
            	if (logger.isLoggable(WsLevel.FINE))
                    logger.log(WsLevel.FINE, "** The target realm did not match the the current security realm. Attempting to map to another identity.");
            	
                //Creates the login context with basic authentication data gathered from custom mapping
                String currentUsername = ((Principal)subject.getPrincipals().iterator().next()).getName();
                String targetRealm = csiv2PerformPolicy.getTargetSecurityName();
                //Calling the mapping function to retrieve the new user credentials
                String[] mappedCreds = getRealmUsernameAndPassword(targetRealm);
                String targetUsername = mappedCreds[0];                
                String targetPassword = mappedCreds[1];
                
                if(targetUsername == null || targetPassword == null){
                	throw new com.ibm.websphere.security.auth.WSLoginFailedException("An error occurred while trying to map to another identity." +
                			"No username and/or password is configured for the realm - " + targetRealm);
                }
                
                if (logger.isLoggable(WsLevel.FINE))
                    logger.log(WsLevel.FINE, "** Using targetUsername = "+targetUsername+", targetRealm = "+targetRealm+", targetPassword = "+targetPassword+" to login.");

                if (logger.isLoggable(WsLevel.FINE))
                    logger.log(WsLevel.FINE, "** logging in for the mapped user...");
                //setting up the callback handler for the login
               
                WSCallbackHandlerImpl callbk = new WSCallbackHandlerImpl(targetUsername, targetRealm, targetPassword);
                LoginContext ctx = new LoginContext("WSLogin", callbk);
                //logging in the new user
                ctx.login();
                if (logger.isLoggable(WsLevel.FINE))
                    logger.log(WsLevel.FINE, "** login done.");
                //Gets the Subject from the context. This subject is used to replace the passed-in Subject during the commit phase.
                basicAuthSubject = ctx.getSubject();
                if (logger.isLoggable(WsLevel.FINE))
                    logger.log(WsLevel.FINE, "** basicAuthSubject = "+basicAuthSubject);
            } catch (javax.security.auth.login.LoginException e) {
                throw new com.ibm.websphere.security.auth.WSLoginFailedException(e.getMessage(), e);
            }
        } else{
        	 if (logger.isLoggable(WsLevel.FINE))
                 logger.log(WsLevel.FINE, "The current realm match the target security realm - identity mapping not neccessary.");
        }
        if (logger.isLoggable(WsLevel.FINE))
            logger.log(WsLevel.FINE, "** end login()");
        return true;
    }

    //during the comit the subject is replaced with the new subject retrieved when the new user was logged in
    public boolean commit() throws LoginException {
        if (logger.isLoggable(WsLevel.FINE))
            logger.log(WsLevel.FINE, "** Starting commit()");
        if (basicAuthSubject != null) {    
                subject.getPublicCredentials().clear();
                subject.getPrivateCredentials().clear();
                subject.getPrincipals().clear();
               
                //Adds everything from basicAuthSubject into the login subject.
                //This completes the mapping to the new user.
                
                subject.getPublicCredentials().addAll(basicAuthSubject.getPublicCredentials());
                subject.getPrivateCredentials().addAll(basicAuthSubject.getPrivateCredentials());
                subject.getPrincipals().addAll(basicAuthSubject.getPrincipals());
        }
        if (logger.isLoggable(WsLevel.FINE))
            logger.log(WsLevel.FINE, "** end commit()");
        return true;
    }

    public boolean abort() throws LoginException {
        return true;
    }

    public boolean logout() throws LoginException {
        return true;
    }

    //This function simply looks up the mapped username and password from the custom properties passed to 
    //the loginmodule based on the target realm.
    private String[] getRealmUsernameAndPassword(String targetRealm) throws WSLoginFailedException {
        if (logger.isLoggable(WsLevel.FINE))
            logger.log(WsLevel.FINE, "** getRealmUsernameAndPassword() - getting username and password for the target realm:" + targetRealm);
        String[] retVal = new String[2];
        
        Object user = null;
        Object pwd = null;

        //Attempt an exact match of the target realm address and the property 
        user = getRealmProperty(true, targetRealm, "username");
        pwd = getRealmProperty(true, targetRealm, "password");
        
        if(user == null || pwd == null){
        	//Attempt an partial match of the target realm address (to get the domain) and the property
        	user = getRealmProperty(false, targetRealm, "username");
            pwd = getRealmProperty(false, targetRealm, "password");
        }
        
        if(user != null && pwd != null){
        	String username = (String)user;
        	String password = (String)pwd;
        	
        	 if (logger.isLoggable(WsLevel.FINE)){
        		 logger.log(WsLevel.FINE, "Found the username - "  + username);
        		 logger.log(WsLevel.FINE, "Found the password - "  + password);
        	 }
                 
        	
        	try{
        		if (password.startsWith("{")) {
        			if (logger.isLoggable(WsLevel.FINE)){
               		 	logger.log(WsLevel.FINE, "The password has been encoded. Decoding using the PasswordUtil.");
        			}
        			password=PasswordUtil.decode(password);
        			
        			if (logger.isLoggable(WsLevel.FINE)){
               		 	logger.log(WsLevel.FINE, "The password has been decoded.");
        			}		
        		}
            		
        	} catch (InvalidPasswordDecodingException e){
        		throw new WSLoginFailedException(e);
        	} catch (UnsupportedCryptoAlgorithmException e){
        		throw new WSLoginFailedException(e);
        	}
        	
        	retVal[0] = username;
        	retVal[1] = password;
        	
        } else {
        	if (logger.isLoggable(WsLevel.FINE)){
       		 	logger.log(WsLevel.FINE, "No mapping properties could be found for the target realm '"
       		 			 + targetRealm
       		 			 + "'.");
			}
        }
   		
        return retVal;
    }
    
    
    private Object getRealmProperty(boolean completeMatch, String targetRealm, String key){
    	
    	 String[] withoutPort = targetRealm.split(":");
         String baseLookupKey = withoutPort[0];
         if(completeMatch){
        	 if (logger.isLoggable(WsLevel.FINE)){
        		 logger.log(WsLevel.FINE, "Attempting to extract custom properties based on a" +
        		 						  " complete match of the target realm address.");
                 logger.log(WsLevel.FINE, "Get property based on the key:" + 
                		 					"{" + baseLookupKey + "}" + key);
        	 }
      	   return options.get("{" + baseLookupKey + "}" + key);
         } else {
        	 int index = withoutPort[0].indexOf(".");
             baseLookupKey = withoutPort[0].substring(index + 1);
             
             if (logger.isLoggable(WsLevel.FINE)){
        		 logger.log(WsLevel.FINE, "Attempting to extract custom properties based on a" +
        		 						  " partial match of the target realm address (e.g. domain name).");
                 logger.log(WsLevel.FINE, "Get property based on the key:" + 
                		 "{" + baseLookupKey + "}" + key);
        	 } 
             return options.get("{" + baseLookupKey + "}" + key); 
         }
    }
	
}
