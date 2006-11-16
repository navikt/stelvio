package no.stelvio.common.security.acegi;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;

public class MockAfterInvocationProviderNoFiltering implements AfterInvocationProvider{

	public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config,
		    Object returnedObject) throws AccessDeniedException
		    {
				return returnedObject;
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
