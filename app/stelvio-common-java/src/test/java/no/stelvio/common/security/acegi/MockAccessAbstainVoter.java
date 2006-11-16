package no.stelvio.common.security.acegi;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.vote.AccessDecisionVoter;

public class MockAccessAbstainVoter implements AccessDecisionVoter{
	
	public boolean supports(ConfigAttribute attribute){
		return true;
	}
	public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
        return ACCESS_ABSTAIN;
    }
	public boolean supports(Class clazz) {
              return true;      
    }
	
}