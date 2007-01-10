package no.stelvio.common.security.authorization.method;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;

public class MockAfterInvocationProviderDeniesAccess implements AfterInvocationProvider{

	public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config,
		    Object returnedObject) throws MethodAccessDeniedException
		    {
				throw new MethodAccessDeniedException("Access is denied");
																		
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
