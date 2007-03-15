package no.stelvio.presentation.security.resolver;

import java.util.List;

import javax.faces.context.FacesContext;

import no.stelvio.common.security.SecurityContextHolder;

import com.groundside.jsf.securityresolver.adapter.AbstractAttributeResolver;

/**
 * Implementation of a security resolver that hooks into
 * JEE Container Managed Security using the Stelvio SecurityContext. Used in conjunction with the
 * JSF security EL-extensions (com.groundside) package.
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * @see no.stelvio.common.security.SecurityContext
 */
public class SecurityAttributeResolver extends AbstractAttributeResolver{
	
	/**
     * Indicate the list of supported functions
     * @param function to check for support as defined by a constant in the <code>AttributeResolver</code>
     * @return true if this implementation supports this function
     */
    public boolean isSupported(int function) {
        boolean supported = false;
   
        switch (function)  {
            case SECURED: {
                    supported = true;
                }
            	break;
            case AUTH_TYPE: {
                    supported = false;
                }
                break;
            case PRINCIPAL_NAME: {
                    supported = true;
                }
                break;
            case USER_IN_ROLE: {
                    supported = true;
                }
                break;
            case USER_IN_ALL_ROLES: {
                    supported = true;
                }
                break;
            default: {
                    supported=false;
                }
                break;
        }
        return supported;
    }

    /**
     * Return a flag indicating if security is currently switched on
     * @param ctx FacesContext, currently not used but is required by the <code>AttributeResolver</code> interface. 
     */
    public boolean isSecurityEnabled(FacesContext ctx) {
        return (SecurityContextHolder.currentSecurityContext().getUserId() != null);
    }

    /**
     * Get the remote user from the Faces External Context
     * @param ctx FacesContext, currently not used but is required by the <code>AttributeResolver</code> interface.
     * @return userid as string
     */
    public String getPrincipalName(FacesContext ctx) {
        return SecurityContextHolder.currentSecurityContext().getUserId();
    }
    
    /**
     * Return the authorization type 
     * @param ctx FacesContext, currently not used but is required by the <code>AttributeResolver</code> interface.
     */
    public String getAuthenticationType(FacesContext ctx){
        return "Not supported.";
    }    
    /**
     * Indicates wheter or not the user is a member of <b>all</b> the supplied roles.
     * @param ctx FacesContext, currently not used but is required by the <code>AttributeResolver</code> interface.
     * @param roleDefinitions the list of roles to check.
     * @return <code>true</code> if the user is a member of all the supplied roles, <code>false</code> otherwise.
     */
    @SuppressWarnings(value={"unchecked"})
    public boolean isUserInAllRoles(FacesContext ctx, List roleDefinitions) {
    	return SecurityContextHolder.currentSecurityContext().isUserInAllRoles(roleDefinitions) ;
    }     
    /**
     * Indicates wheter or not the user is a member of <b>one</b> of the supplied roles.
     * @param ctx FacesContext, currently not used but is required by the <code>AttributeResolver</code> interface.
     * @param roleDefinitions the list of roles to check.
     * @return <code>true</code> if the user is a member of all the supplied roles, <code>false</code> otherwise.
     */
    @SuppressWarnings(value={"unchecked"})
    public boolean isUserInRole(FacesContext ctx, List roleDefinitions) {
    	return SecurityContextHolder.currentSecurityContext().isUserInRoles(roleDefinitions) ;
    }   
}