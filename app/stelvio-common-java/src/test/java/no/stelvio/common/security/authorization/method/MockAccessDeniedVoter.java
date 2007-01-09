package no.stelvio.common.security.authorization.method;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.vote.AccessDecisionVoter;

public class MockAccessDeniedVoter implements AccessDecisionVoter{
	
	public boolean supports(ConfigAttribute attribute){
		return true;
	}
	public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
        return ACCESS_DENIED;
    }
	public boolean supports(Class clazz) {
              return true;      
    }
	
}
