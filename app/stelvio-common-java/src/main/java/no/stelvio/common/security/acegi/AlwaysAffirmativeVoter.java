package no.stelvio.common.security.acegi;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.vote.*;

public class AlwaysAffirmativeVoter implements AccessDecisionVoter {
    //~ Instance fields ================================================================================================

    public boolean supports(ConfigAttribute attribute) {
    	return true;
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

    public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) 
    {    
    	return ACCESS_GRANTED;
    }   
}

