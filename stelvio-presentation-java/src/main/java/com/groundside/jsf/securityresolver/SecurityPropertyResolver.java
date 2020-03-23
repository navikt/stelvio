package com.groundside.jsf.securityresolver;


import com.groundside.jsf.securityresolver.adapter.AttributeResolver;

import com.groundside.jsf.util.MessageResources;

import java.security.Principal;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.StringTokenizer;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;

/**
 * JSF Property resolver which understands attributes within a special
 * #{securityScope}
 * @author Duncan Mills
 * $Id: SecurityPropertyResolver.java,v 1.6 2005/10/09 10:51:49 drmills Exp $
 */
public class SecurityPropertyResolver extends PropertyResolver {

    private static final String SECURITY_ENABLED  = "securityEnabled";  
    private static final String AUTH_MECHANISM    = "authType";
    private static final String REMOTE_USER       = "remoteUser";
    private static final String USER_IN_ROLE      = "userInRole";
    private static final String USER_IN_ALL_ROLES = "userInAllRoles";

    /**
     * <p>The original <code>PropertyResolver</code>.</p>
     */
    private PropertyResolver _baseResolver = null;

    /**
     * Constructor accepting the default el-resolver to delegate to
     * @param baseVariable
     */
    public SecurityPropertyResolver(PropertyResolver baseVariable) {

        _baseResolver = baseVariable;
    }

    /**
     * Resolve our interesting properties - or throw it back to the base
     * EL resolver for everything else
     * @param baseVariable
     * @param property
     * @return evaluated result
     * @throws EvaluationException
     * @throws PropertyNotFoundException
     */
    public Object getValue(Object baseVariable,
                           Object property) throws EvaluationException,
                                                   PropertyNotFoundException {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extCtx = ctx.getExternalContext();

        if (baseVariable instanceof SecurityVariableMarker) {
            AttributeResolver resolver = ((SecurityVariableMarker)baseVariable).getResolver();
            if (SECURITY_ENABLED.equals(property)){
                return new Boolean(resolver.isSecurityEnabled(ctx)); 
            } else if (REMOTE_USER.equals(property)) {
                String userName = null;
                if (resolver.isSupported(AttributeResolver.PRINCIPAL_NAME)){
                    userName = resolver.getPrincipalName(ctx);
                }
                return userName;
            } else if (AUTH_MECHANISM.equals(property)){
                String authType = null;
                if (resolver.isSupported(AttributeResolver.AUTH_TYPE)){
                    authType = resolver.getAuthenticationType(ctx);
                }
                return authType;                
            } else if (USER_IN_ROLE.equals(property)) {
                Object retObj = null;
                if (resolver.isSupported(AttributeResolver.USER_IN_ROLE)){
                    retObj = new SecurityPropertyMarker(false,resolver);
                }
                else {
                    retObj = new Boolean(false);
                }
                return retObj;
            } else if (USER_IN_ALL_ROLES.equals(property)) {
                Object retObj = null;
                if (resolver.isSupported(AttributeResolver.USER_IN_ALL_ROLES)){
                    retObj = new SecurityPropertyMarker(true,resolver);
                }
                else {
                    retObj = new Boolean(false);
                }
                return retObj;
            } else {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                if (cl == null) {
                    cl = this.getClass().getClassLoader();
                }            
                MessageResources messages = new MessageResources(cl,Constants.SECURITY_EL_ERRORS);
                String errorMsg = messages.getResource("properties.PropertyNotFoundException",
                                                new Object[] {property,Constants.SECURITY_EL_SCOPE });
                throw new PropertyNotFoundException(errorMsg);
            }
        } else if (baseVariable instanceof SecurityPropertyMarker) {
            SecurityPropertyMarker marker = (SecurityPropertyMarker)baseVariable;
            AttributeResolver resolver = marker.getResolver();
            boolean authOk = false;
            
            ArrayList roleList = new ArrayList(5);
            StringTokenizer tok = new StringTokenizer((String)property, ",", false);
            while (tok.hasMoreTokens()) {
                roleList.add(tok.nextToken());
            }
            
            if (marker.isInclusive()) {
                authOk = resolver.isUserInAllRoles(ctx,roleList);
            }
            else{
                authOk = resolver.isUserInRole(ctx,roleList); 
            }
            return new Boolean(authOk);
        } else {
            return _baseResolver.getValue(baseVariable, property);
        }


    }

    /**
     * All of the security attributes are read only so return false for those
     * Otherwise leave it up to the base resolver
     *
     * @param baseVariable Base object to check for ReadOnly
     * @param property Property to be checked
     *
     * @exception EvaluationException
     * @exception PropertyNotFoundException
     */
    public boolean isReadOnly(Object baseVariable,
                              Object property) throws EvaluationException,
                                                                           PropertyNotFoundException {
        if ((baseVariable instanceof SecurityVariableMarker) ||
            (baseVariable instanceof SecurityPropertyMarker)) {
            return false;
        } else {
            return _baseResolver.isReadOnly(baseVariable, property);
        }
    }


    /**
     * Return the Types of the specialised properties that we support
     * otherwise let the base resolver handle it
     *
     * @param baseVariable Base object owning the property
     * @param property Property whose type is to be returned
     *
     * @exception EvaluationException
     * @exception PropertyNotFoundException
     */
    public Class getType(Object baseVariable,
                         Object property) throws EvaluationException,
                                                                      PropertyNotFoundException {
        Class clazz = String.class;
        if (baseVariable instanceof SecurityVariableMarker) {
            if (REMOTE_USER.equals(property)||
                AUTH_MECHANISM.equals(property)) {
                clazz = String.class;
            } else if (SECURITY_ENABLED.equals(property)||
                       USER_IN_ROLE.equals(property)||
                       USER_IN_ALL_ROLES.equals(property)) {
                clazz = Boolean.class;
            } 
            return clazz;
        } else {
            return _baseResolver.getType(baseVariable, property);
        }
    }


    public Object getValue(Object baseVariable, int i) {
        return _baseResolver.getValue(baseVariable, i);
    }

    public void setValue(Object baseVariable, Object property, Object value) {
        _baseResolver.setValue(baseVariable, property, value);
    }


    public void setValue(Object baseVariable, int i, Object value) {
        _baseResolver.setValue(baseVariable, i, value);
    }

    public boolean isReadOnly(Object baseVariable, int i) {
        return _baseResolver.isReadOnly(baseVariable, i);
    }

    public Class getType(Object baseVariable, int i) {
        return _baseResolver.getType(baseVariable, i);
    }

}
