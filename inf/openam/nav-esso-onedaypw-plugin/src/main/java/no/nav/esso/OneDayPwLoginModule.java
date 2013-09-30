package no.nav.esso;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;

import com.sun.identity.authentication.spi.AMLoginModule;
import com.sun.identity.authentication.spi.AuthLoginException;
import com.sun.identity.authentication.util.ISAuthConstants;
import com.sun.identity.shared.debug.Debug;

public class OneDayPwLoginModule extends AMLoginModule {
	
	private final static String MODULE_NAME = "OneDayPasswordLoginModule";
	protected final static Debug logger = Debug.getInstance(MODULE_NAME);		
	
	private final static int AUTH_ERROR = 2;
	
	private String userId;
	private String authenticationUrl;
	private AuthValidator validator;
	
	@Override
	public void init(Subject subject, Map sharedState, Map options) {
		if (subject != null) logger.message("Subject:" + subject.toString());
		if (sharedState != null) logger.message("SharedState:" + sharedState.toString());
		if (options != null) logger.message("Options:" + options.toString());
		initPasswordValidator(options);
	}

	private void initPasswordValidator(Map options) {
		String serviceUsername = getMapAttribute(options, getServiceUsernameKey());
        String servicePassword = getMapAttribute(options, getServicePassword());
        authenticationUrl = getMapAttribute(options, getAuthenticationUrlKey());
        validator = new OneDayPwValidator(serviceUsername, servicePassword, authenticationUrl);
	}

	@Override
	public Principal getPrincipal() {		
		try {
			setUserSessionProperty("uid", userId);
		} catch (AuthLoginException e) {
			logger.error("Error setting property uid on UserSession: " + e.getMessage());
			return null;
		}
		return new OneDayPwPrincipal("anonymous");
	}


	@Override
	public int process(Callback[] callbacks, int state) throws LoginException {

		String userName = ((NameCallback) callbacks[0]).getName();
		logger.message("Username: " + userName);
        char[] passwordCharArray = ((PasswordCallback) callbacks[1]).getPassword();
        String password = new String(passwordCharArray);

        if ((userName.length() == 0)) {
            throw new AuthLoginException("Username must not be empty.");
        }
        
		ValidationResult result = validator.validate(userName, password);
		if (result.isValid()) {
			userId = result.getUserId();
			// set AuthLevel based on service response
			if( result.getServiceSecLevel().equals("L")) {
				// set AuthLevel which gives SecLevel 1
				setAuthLevel(1);
				logger.message("AuthLevel set to 1");
			}else if( result.getServiceSecLevel().equals("M")) {
				// set AuthLevel which gives SecLevel 2 or 3
				setAuthLevel(2);				
				logger.message("AuthLevel set to 2");
			}			
			return ISAuthConstants.LOGIN_SUCCEED;
        } else {
        	// redirect to gotoOnFail + the errorcode
        	logger.message("Error from validator: " + result.getError());
        	String gotoOnFail = getHttpServletRequest().getParameter("gotoOnFail");
        	if(gotoOnFail == null) {
        		throw new AuthLoginException("Missing gotoOnFail parameter");
        	}
        	gotoOnFail = gotoOnFail.concat("?authResult=" + result.getError().getCode());        	
        	setLoginFailureURL(gotoOnFail);
        	HttpServletResponse response = getHttpServletResponse();
        	response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        	
        	return AUTH_ERROR;        	
        }
	}
	
	protected String getAuthenticationUrlKey() {
        return "iplanet-am-auth-onedaypwloginmodule-server-specification";
    }

    protected String getServiceUsernameKey() {
        return "iplanet-am-auth-onedaypwloginmodule-username";
    }

    protected String getServicePassword() {
        return "iplanet-am-auth-onedaypwloginmodule-password";
    }
    
    private String getMapAttribute(Map map, String name) {
        if( map != null) {
	    	Set set = (Set) map.get(name);
	        if (set == null || set.isEmpty()) {
	            return null;
	        } else {
	            String value = (String) set.iterator().next();
	            if (value != null) {
	                return value.trim();
	            } else {
	                return null;
	            }
	        }
        } else {
        	return null;
        }
    }

}
