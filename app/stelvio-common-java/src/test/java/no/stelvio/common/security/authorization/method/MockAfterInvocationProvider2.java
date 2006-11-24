package no.stelvio.common.security.authorization.method;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;

public class MockAfterInvocationProvider2 implements AfterInvocationProvider{

	public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config,
		    Object returnedObject) throws AccessDeniedException
		    {
				String filteredValue ="Filtered2";
				return filteredValue;
		    }
	public boolean supports(ConfigAttribute attribute)
	{
		return true;
	}
	public boolean supports(Class clazz)
	{
		return true;
	}
}
