package no.stelvio.common.security.acegi;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.vote.*;

import java.util.Iterator;

public class SaksVoter implements AccessDecisionVoter {
    //~ Instance fields ================================================================================================

    private String rolePrefix = "ROLE_";

    //~ Methods ========================================================================================================

    public String getRolePrefix() {
        return rolePrefix;
    }

    /**
     * Allows the default role prefix of <code>ROLE_</code> to be overriden. May be set to an empty value,
     * although this is usually not desireable.
     *
     * @param rolePrefix the new prefix
     */
    public void setRolePrefix(String rolePrefix) {
        this.rolePrefix = rolePrefix;
    }

    public boolean supports(ConfigAttribute attribute) {
        
    	return true;
    	/*
    	if ((attribute.getAttribute() != null) && attribute.getAttribute().startsWith(getRolePrefix())) {
            return true;
        } else {
            return false;
        }*/
    }

    /**
     * This implementation supports any type of class, because it does not query the presented secure object.
     *
     * @param clazz the secure object
     *
     * @return always <code>true</code>
     */
    public boolean supports(Class clazz) {
        return true;
    }

    public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
        int result = ACCESS_ABSTAIN;
        Iterator iter = config.getConfigAttributes();
        System.out.println("********************Inne i SaksVoter!");
       // System.out.println("Objektet er av type: " + object.getClass().toString());
        
        /*while (iter.hasNext()) {
            ConfigAttribute attribute = (ConfigAttribute) iter.next();
            
            //System.out.println("attributtens verdi er: " + attribute.getAttribute());
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;

                // Attempt to find a matching granted authority
                for (int i = 0; i < authentication.getAuthorities().length; i++) {
                	
                	System.out.println("Authorities in object - " + authentication.getAuthorities()[i].getAuthority());
                	
                    if (attribute.getAttribute().equals(authentication.getAuthorities()[i].getAuthority())) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }*/
        
        return ACCESS_GRANTED;
        //return result;
    }
}



