package no.stelvio.consumer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;
import com.ibm.websphere.security.auth.callback.WSCallbackHandlerImpl;
import com.ibm.websphere.security.cred.WSCredential;
import com.ibm.ws.security.util.PasswordUtil;
import com.ibm.ws.webservices.engine.encoding.Base64;

public class SecurityHeader {

    private static String LTPAns = "wsst";
    private static String LTPAid = ":LTPA";
    private static String LTPAURL =  "http://www.ibm.com/websphere/appserver/tokentype/5.0.2";
    private static String passwordType ="http:// docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
    private static String SecurityURL =     "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private static String SOAPEncURL = "http://schemas.xmlsoap.org/soap/encoding/";
    private static String SOAPEnvURL = "http://schemas.xmlsoap.org/soap/envelope/";
    private static String XMLSchemaURL = "http:// www.w3.org/2001/XMLSchema";
    private static String XMLSchemaInsURL = "http://www.w3.org/2001/XMLSchema-instance";
    protected final static Log log = LogFactory.getLog(SecurityHeader.class);
    private static HashMap<String,byte[]> tokenCache=new HashMap<String, byte[]>();
    // Create LTPA token header

    
    public static SOAPElement createLTPAHeader(String userName, String password){
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
			return createLTPAHeaderFromToken(tokenCache.get(userName+password));
    	}catch(Exception e){
    		log.warn("Error while setting RunAs subject for soap header. Using empty security header",e);    		
    		return null;
    	}
    }
    
    
    public static SOAPElement createLTPAHeader(){
    	// First try to get the token iself
        
        byte[] token = getSecurityToken();
        if(token == null)
            return null;
        return createLTPAHeaderFromToken(token);
    }		
    
    private static SOAPElement createLTPAHeaderFromToken(byte token[]){			
        // Now try to build the header

        SOAPElement header = null;
        try{
			
            // Create header Element
            SOAPFactory sFactory = SOAPFactory.newInstance();
            Name headerName = sFactory.createName("Security","wsse",SecurityURL);

            header = sFactory.createElement(headerName);
            header.addNamespaceDeclaration("soapenc",SOAPEncURL);
            header.addNamespaceDeclaration("xsd",XMLSchemaURL);
            header.addNamespaceDeclaration("xsi",XMLSchemaInsURL);
            Name mustName = sFactory.createName("mustUnderstand","soapenv",SOAPEnvURL);
            header.addAttribute(mustName,"1");

			
            // Create and add binary token Element
            Name binaryTokenName = sFactory.createName("BinarySecurityToken","wsse",
                                                                         SecurityURL);

            SOAPElement binaryToken = sFactory.createElement(binaryTokenName);
            header.addChildElement(binaryToken);
			
            // Populate token
            binaryToken.addNamespaceDeclaration(LTPAns,LTPAURL);
            Name attrName = sFactory.createName("ValueType");
            binaryToken.addAttribute(attrName,LTPAns+LTPAid);
            String stoken = Base64.encode(token);
            binaryToken.addTextNode(stoken);	

        }
        catch(Exception e){
            log.error("Error building LTPA security header", e);            
        }
        return header;

    }
	
    // Create BasicAuth header
    public static SOAPElement createBasicAuth(String user, String password){

        // Make sure we have the credentials
        if((user == null) || (password == null)){

            return null;
        }

        // Now try to build the header
        SOAPElement header = null;
        try{
			
            // Create header Element
            SOAPFactory sFactory = SOAPFactory.newInstance();
            Name headerName = sFactory.createName("Security","wsse",SecurityURL);
            header = sFactory.createElement(headerName);
            header.addNamespaceDeclaration("soapenc",SOAPEncURL);
            header.addNamespaceDeclaration("xsd",XMLSchemaURL); 
            header.addNamespaceDeclaration("xsi",XMLSchemaInsURL); 
            Name mustName = sFactory.createName("mustUnderstand","soapenv",SOAPEnvURL);
            header.addAttribute(mustName,"1");

            // Create and add userName token Element
            Name userTokenName =sFactory.createName("UsernameToken","wsse",SecurityURL);
            SOAPElement userToken = sFactory.createElement(userTokenName);
            header.addChildElement(userToken);

            // Populate token

            Name userElementName =	sFactory.createName("Username","wsse",SecurityURL);
            Name passwordElementName =	sFactory.createName("Password","wsse",
                                                                          SecurityURL);		

            SOAPElement userElement = sFactory.createElement(userElementName);
            userElement.addTextNode(user);
            SOAPElement passwordElement = sFactory.createElement(passwordElementName);
            Name attrName = sFactory.createName("Type");
            passwordElement.addAttribute(attrName,passwordType);
            passwordElement.addTextNode(password);
			
            userToken.addChildElement(userElement);
            userToken.addChildElement(passwordElement);
        }
        catch(Exception e){
            System.out.println("Error building Basic Auth security header");
            e.printStackTrace();
        }
        return header;
    }

    // Process security header
    public static void processHeader(SOAPElement header){

        try{
			
            // Print it first

            try{
                OutputFormat ouf = new OutputFormat();
                ouf.setIndenting(true);
                XMLSerializer serializer = new XMLSerializer(System.out,ouf);
                serializer.serialize(header);
                System.out.println(" ");
            }
            catch(Exception e){
                e.printStackTrace();
            }
			
            // Check for the token type

            SOAPFactory sFactory = SOAPFactory.newInstance();
            Name binaryTokenName = sFactory.createName("BinarySecurityToken","wsse",                                                                         SecurityURL);
            Name userTokenName = sFactory.createName("UsernameToken","wsse",SecurityURL);

            // Is it an binary token ?
            Iterator tokens = header.getChildElements(binaryTokenName);

            if(tokens != null){ 
                while(tokens.hasNext()){
                    SOAPElement binaryToken = (SOAPElement)tokens.next();
                    
                    // Make sure it is LTPA

                    String tType = binaryToken.getAttribute("ValueType");
                    if((tType == null) || !tType.endsWith(LTPAid))
                        continue;
                    String stoken = binaryToken.getValue();
                    byte[] token = Base64.decode(stoken);
                    setSecurityContext(token);
                    return;
                }
            }
            
            // Is it an Basic Auth token ?
            tokens = header.getChildElements(userTokenName);
            if((tokens != null) && (tokens.hasNext())){
				
                // Process Basic Auth token

                SOAPElement userToken = (SOAPElement)tokens.next();
								
                // Get name and password
                Name userElementName =	sFactory.createName("Username","wsse",SecurityURL);		
                Name passwordElementName =	sFactory.createName("Password","wsse",
                                                                    SecurityURL);		

                Iterator users = userToken.getChildElements(userElementName);
                Iterator passwords = userToken.getChildElements(passwordElementName);
                if((users != null) && (users.hasNext()) && 
                                  (passwords != null) && (passwords.hasNext())){
                    String user = ((SOAPElement)users.next()).getValue();
                    String password = ((SOAPElement)passwords.next()).getValue();

                    setSecurityContext(user,password);
                }
                return;
            }
			
            // Unknown security token, ignore
        }
        catch(Exception e){

            System.out.println("Error processing security header");
            e.printStackTrace();
        }
    }

    // Get security token
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
                log.debug("My data from the Caller credential is:  " + 
                                                      user + " security token is: "+token.toString());

                               
            }
        }
        catch (Exception e){
            log.error("Error obtaining token", e);
        }
        return token;
    }
	
    public static void setSecurityContext(byte[] token){
	
        if(token == null){
            System.out.println("Cannot create LoginContext. No token");

            return;
        }
        LoginContext lc = null;
            
        // Create new login context
        try {
            lc = new LoginContext("WSLogin",new WSCallbackHandlerImpl(token));

        } 
        catch (LoginException le) {
            System.out.println("Cannot create LoginContext. " + le.getMessage());
            return;
        } 
        catch(SecurityException se) {

            System.out.println("Cannot create LoginContext." + se.getMessage());
            return;
        }

        // Login with the new context
        try{
            lc.login(); 
        } 
        catch(LoginException le){
            System.out.println("Fails to Login. " + le.getMessage());
            return;
        }

        // Get security subject
        Subject security_subject = lc.getSubject();
		
        // Set security suject
        try{
            WSSubject.setRunAsSubject(security_subject);
        }
        catch (WSSecurityException e){
            System.out.println("Error Setting security credentials. " + e.getMessage());
        }
        catch (Exception e){
            System.out.println("Error Setting security credentials. " + e.getMessage());
        }

    }

    public static void setSecurityContext(String user, String password){
	
        if((user == null) || (password == null)){
            log.warn("Cannot create LoginContext. No token");

            return;
        }
        LoginContext lc = null;
            
        // Create new login context
        try {
            lc = new LoginContext("WSLogin",new WSCallbackHandlerImpl(user, password));

        } 
        catch (Exception se) {

            log.error("Cannot create LoginContext." + se.getMessage(),se);
            throw new RuntimeException("Failed to create login context for user "+user+" message: "+se.getMessage(),se);
        }

        // Login with the new context
        try{
            lc.login(); 
        } 
        catch(LoginException le){
            log.error("Fails to Login. " + le.getMessage(),le);
            throw new RuntimeException("Failed to login user "+user+" message: "+le.getMessage(),le);
        }

        // Get security subject
        Subject security_subject = lc.getSubject();
		
        // Set security suject
        try{
            WSSubject.setRunAsSubject(security_subject);
        }
        catch (Exception e){
            log.error("Error Setting security credentials. " + e.getMessage(),e);
            throw new RuntimeException("Unable to set security credentials for user "+user,e);
        }

    }
} 


      
