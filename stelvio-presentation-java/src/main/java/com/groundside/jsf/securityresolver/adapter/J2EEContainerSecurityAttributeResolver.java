package com.groundside.jsf.securityresolver.adapter;

import java.util.Iterator;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * Implementation of the security resolver that hooks into
 * J2EE Container Managed Security
 * @author Duncan Mills
 * $Id: J2EEContainerSecurityAttributeResolver.java,v 1.4 2005/10/04 00:49:09 drmills Exp $
 */
public class J2EEContainerSecurityAttributeResolver extends AbstractAttributeResolver{
    public J2EEContainerSecurityAttributeResolver() {
    }


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
            case AUTH_TYPE: {
                    supported = true;
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
     * @param ctx FacesContext
     */
    public boolean isSecurityEnabled(FacesContext ctx) {
        return (ctx.getExternalContext().getRemoteUser()!=null);
    }

    /**
     * Get the remote user from the Faces External Context
     * @param ctx FacesContext
     * @return user name string
     */
    public String getPrincipalName(FacesContext ctx) {
        return ctx.getExternalContext().getRemoteUser();
    }
    
    /**
     * Return the authorization type 
     * @param ctx FacesContext
     */
    public String getAuthenticationType(FacesContext ctx){
        return ctx.getExternalContext().getAuthType();
    }    

    public boolean isUserInAllRoles(FacesContext ctx, List roleDefinitions) {
        return matchUserRoles(ctx,roleDefinitions,true);
    }

    public boolean isUserInRole(FacesContext ctx, List roleDefinitions) {
        return matchUserRoles(ctx,roleDefinitions,false);
    }

    /*
     * Internal function to check if the current user is in one or all roles listed
     */
    private boolean matchUserRoles(FacesContext fctx, List roleDefinitions, boolean inclusive) {
        boolean authOk = false;
        ExternalContext ctx = fctx.getExternalContext();
        
        Iterator iter = roleDefinitions.iterator();

        while (iter.hasNext()) {
            authOk = ctx.isUserInRole((String)iter.next());
            if ((inclusive && !authOk) || (!inclusive && authOk)) {
                break;
            }
        }
        return authOk;
    }
}
