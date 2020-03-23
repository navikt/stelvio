package com.groundside.jsf.securityresolver;

import com.groundside.jsf.securityresolver.adapter.AttributeResolver;

/**
 * This class is used by the VariableResolver as a marker class
 * to indicate to the property resolver that the property being 
 * passed to it should be resolved by the security resolver
 * @author Duncan Mills
 * $Id: SecurityVariableMarker.java,v 1.2 2005/10/04 00:49:11 drmills Exp $
 */
public class SecurityVariableMarker {
    private AttributeResolver _resolver = null;
    
    /**
     * Constructor for the marker class. The current resolver is
     * retrieved from applciation scope and passed in here to save
     * time later
     * @param resolver pluggable resolver for security
     */
    public SecurityVariableMarker(AttributeResolver resolver) {
      _resolver = resolver;
    }

    /**
     * Getter for the current security resolver
     * @return the current resolver
     */
    public AttributeResolver getResolver() {
        return _resolver;
    }
}
