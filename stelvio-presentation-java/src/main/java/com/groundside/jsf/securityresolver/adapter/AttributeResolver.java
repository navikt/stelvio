package com.groundside.jsf.securityresolver.adapter;

import java.util.List;

import javax.faces.context.FacesContext;

/**
 * Interface defining the methods that need to be implemented
 * by a security resolver implementation
 * @author Duncan Mills
 * $Id: AttributeResolver.java,v 1.3 2005/10/04 00:49:09 drmills Exp $
 */
public interface AttributeResolver {


    /* The following constants are used to define
     * which operations are available in a particular
     * Implementation
     */

    public static final int SECURED = 0;

    public static final int PRINCIPAL_NAME = 1;

    public static final int AUTH_TYPE = 2;

    public static final int USER_IN_ROLE = 3;

    public static final int USER_IN_ALL_ROLES = 4;

    /**
     * Given a particular operation return
     * a status indicating if it is supported
     * @param operation constant value indicating the operation to check
     */
    public boolean isSupported(int operation);

    /**
     * Return a flag indicating if security is currently switched on
     * @param ctx FacesContext
     */
    public boolean isSecurityEnabled(FacesContext ctx);

    /**
     * Return the user name of the logged in user
     * @param ctx FacesContext
     */
    public String getPrincipalName(FacesContext ctx);

    /**
     * Return the authorization type
     * @param ctx FacesContext
     */
    public String getAuthenticationType(FacesContext ctx);

    /**
     * Indicate if the current user is in at least one of the
     * supplied roles
     * @param ctx FacesContext
     * @param roleDefinitions a List containing the roles to check against 
     */
    public boolean isUserInRole(FacesContext ctx, List roleDefinitions);

    /**
     * Indicate if the current user is in all of the
     * supplied roles.
     * @param ctx FacesContext
     * @param roleDefinitions a List containing the roles to check against 
     */
    public boolean isUserInAllRoles(FacesContext ctx, List roleDefinitions);
}
