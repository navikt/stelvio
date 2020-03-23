package com.groundside.jsf.securityresolver;


/**
 * Constants used throughout the Security Extension
 * 
 */
public interface Constants {

    /**
     * The new root scope we are adding to JSF
     */
    public static final String SECURITY_EL_SCOPE = "securityScope";

    /**
     * The context initialization parameter used to define the 
     * class name of the pluggable resolver.
     * The default resolver uses J2EE Container Security
     */
    public static final String SECURITY_EL_RESOLVER = "com.groundside.jsf.SECURITY_EL_RESOLVER";
    
    /**
     * The default resolver class
     */
    public static final String DEFAULT_SECURITY_RESOLVER = "com.groundside.jsf.securityresolver.adapter.J2EEContainerSecurityAttributeResolver";    
    
    
    /**
     * <p>The message bundle used for errors
     */
    public static final String SECURITY_EL_ERRORS = "com.groundside.jsf.securityresolver.SecurityMessages";
    

}
