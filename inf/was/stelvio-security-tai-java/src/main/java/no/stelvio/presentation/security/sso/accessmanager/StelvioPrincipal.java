package no.stelvio.presentation.security.sso.accessmanager;

import java.util.List;

/**
 * Represents a userprincipal containing userid, authentication level and ldap groups.
 * 
 * @author persondab2f89862d3
 */
public interface StelvioPrincipal {
    /**
     * Gets the userid
     * 
     * @return the userid
     */
    String getUserId();

    /**
     * Returns the username which the current principal is authorized to act on behalf of.
     * 
     * @return the username
     */
    String getAuthorizedAs();
    
    /**
     * Returns the SSO token used for logging in this principal. Used only when OpenAm is accessmanager
     * @return the ssotoken
     */
    String getSsoToken();

    /**
     * 
     * @return the
     */
    List<String> getGroupIds();
}
