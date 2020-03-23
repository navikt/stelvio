package com.groundside.jsf.securityresolver;


import com.groundside.jsf.securityresolver.adapter.AttributeResolver;

import com.groundside.jsf.securityresolver.adapter.J2EEContainerSecurityAttributeResolver;

import com.groundside.jsf.util.MessageResources;

import java.util.HashMap;

import java.util.Locale;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

/**
 * Plug-in variable resolver for JSF that handles securityScope 
 * based expressions. All other expressions are passed back to the
 * default impl.
 * The resolver that is used will be the J2EE Container security 
 * resolver by default. Otherwise another resolver can be plugged in
 * using the context parameter com.groundside.jsf.SECURITY_EL_RESOLVER
 * in web.xml. A plug-in resolver class name specified here should 
 * extend <code>AbstractAttributeResolver</code>
 * @see com.groundside.jsf.securityresolver.adapter.AbstractAttributeResolver
 * @author Duncan Mills 
 * $Id: SecurityVariableResolver.java,v 1.3 2005/10/09 10:22:21 drmills Exp $
 */
public class SecurityVariableResolver extends VariableResolver {
 
    /**
     * The original <code>VariableResolver</code>.
     */
    private VariableResolver _baseResolver = null;

    
    /**
     * Message access
     */
     MessageResources messages = null;

    /**
     * Constructor for the resolver that accepts the default impl
     * to save away.
     * @param base origional variable resolver
     */
    public SecurityVariableResolver(VariableResolver base) {
        this._baseResolver = base;
    }

    /**
     * If the securityScope expression is returned - resolve that
     * otherwise pass the resolution back to the base resolver.
     * Note that if security is not currently active any expressions
     * just get resolved to null
     *
     * @param expr Expression to be resolved
     */
    public Object resolveVariable(FacesContext context,
                                  String expr) throws EvaluationException {
                                  
        Object returnObj = null;                                  

        if (Constants.SECURITY_EL_SCOPE.equals(expr)) {
            ExternalContext extCtx =
                FacesContext.getCurrentInstance().getExternalContext();
            AttributeResolver resolver = null;
            try  {
                resolver = getAttributeResolver(context); 
                if (resolver != null){
                    returnObj =  new SecurityVariableMarker(resolver);    
                }                    
            } catch (ServletException ex)  {
                throw new EvaluationException(ex);
            } 
        } else {
            returnObj =  _baseResolver.resolveVariable(context, expr);
        }
        return returnObj;
    }
    
    
    /*
     * Check the application context for an existing resolver, if not get 
     * the relevant class and load one up. 
     */
    private AttributeResolver getAttributeResolver(FacesContext ctx) throws ServletException {
        HttpSession session = (HttpSession)ctx.getExternalContext().getSession(false);
        
        String pluginClass = session.getServletContext().getInitParameter(Constants.SECURITY_EL_RESOLVER);
        if (pluginClass == null) {
            pluginClass = Constants.DEFAULT_SECURITY_RESOLVER;
        }
        
        Map attrs = (Map)ctx.getExternalContext().getApplicationMap();
        
        AttributeResolver selectedResolver = (AttributeResolver)attrs.get(Constants.SECURITY_EL_RESOLVER);
        if (selectedResolver == null) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = this.getClass().getClassLoader();
            }
            messages = new MessageResources(cl,Constants.SECURITY_EL_ERRORS);
            try {
                Class clazz = cl.loadClass(pluginClass);
                selectedResolver = (AttributeResolver)clazz.newInstance();
            } catch (ClassCastException e) {
                throw new ServletException
                  (messages.getResource("load.ClassCastException", new Object[] { pluginClass }));
            } catch (ClassNotFoundException e) {
                throw new ServletException
                (messages.getResource("load.ClassNotFoundException", new Object[] { pluginClass }));
            } catch (IllegalAccessException e) {
                throw new ServletException
                (messages.getResource("load.IllegalAccessException", new Object[] { pluginClass }));
            } catch (InstantiationException e) {
                throw new ServletException
                (messages.getResource("load.InstanciationException", new Object[] { pluginClass }));
            }
            attrs.put(Constants.SECURITY_EL_RESOLVER,selectedResolver);
    }
    return selectedResolver;
    }
}
