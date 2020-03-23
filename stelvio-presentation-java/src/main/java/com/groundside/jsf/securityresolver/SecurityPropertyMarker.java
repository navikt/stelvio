package com.groundside.jsf.securityresolver;

import com.groundside.jsf.securityresolver.adapter.AttributeResolver;


/**
 * This class is used by the PropertyResolver as a marker class
 * to indicate to the property resolver (recursively) that the associated 
 * property is a set of roles that need to be checked against
 * @author Duncan Mills
 * $Id: SecurityPropertyMarker.java,v 1.2 2005/10/04 00:49:11 drmills Exp $
 */
public class SecurityPropertyMarker {

    private boolean _inclusive = false;
    private AttributeResolver _resolver = null;

    /**
     * Constructor for the marker class. The current resolver is
     * passed on from the variable marker along with a flag indicating
     * if this is a test for all or just one role
     * @param inclusive flag to indicate how the role list that will be resolved next 
     *        should be handled.
     * @param resolver pluggable resolver for security
     */
    public SecurityPropertyMarker(boolean inclusive, AttributeResolver resolver) {
        _inclusive = inclusive;
        _resolver = resolver;
    }

    /**
     * Getter for the role comparison type
     * @return flag indicating if this is an AND or OR check
     */
    public boolean isInclusive() {
        return _inclusive;
    }
    
    /**
     * Getter for the current security resolver
     * @return the current resolver
     */
    public AttributeResolver getResolver() {
        return _resolver;
    }
}
