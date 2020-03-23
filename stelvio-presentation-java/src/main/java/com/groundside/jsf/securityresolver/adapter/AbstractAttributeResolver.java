package com.groundside.jsf.securityresolver.adapter;

import java.util.List;

import javax.faces.context.FacesContext;

/**
 * Base implementation of the security resolver
 * Implements default behavour for each of the functions
 * @author Duncan Mills
 * $Id: AbstractAttributeResolver.java,v 1.3 2005/10/04 00:49:09 drmills Exp $
 */
public abstract class AbstractAttributeResolver implements AttributeResolver {
    public AbstractAttributeResolver() {
    }
    
    /**
     * Return a flag indicating if security is currently switched on
     * @param ctx FacesContext
     */
    public boolean isSecurityEnabled(FacesContext ctx) {
        return false;
    }

    /**
     * Default implementation of principalName returns null
     * @param ctx FacesContext
     * @return null value 
     */
    public String getPrincipalName(FacesContext ctx) {
        return null;
    }
    
    /**
     * Return the authorization type null in this abstract 
     * impl
     * @param ctx FacesContext
     */
    public String getAuthenticationType(FacesContext ctx){
        return null;
    }

    /**
     * Default impl of the user in roles check, hardcoded to false
     * @param ctx FacesContext
     * @param roleDefinitions list of roles to check
     * @return false
     */
    public boolean isUserInRole(FacesContext ctx, List roleDefinitions) {
        return false;
    }

    /**
     * Default impl of the user in all roles check, hardcoded to false
     * @param ctx FacesContext
     * @param roleDefinitions list of roles to check
     * @return false
     */
    public boolean isUserInAllRoles(FacesContext ctx, List roleDefinitions){
        return false;
    }

}
