package no.stelvio.common.security.acegi;
import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.afterinvocation.*;

//import stelvio.dao.PersonDO;

/**
 * Indicates a class is responsible for participating in an {@link
 * AfterInvocationProviderManager} decision.
 *
*/
public class EnhetsProvider implements AfterInvocationProvider 
{
			
			public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config,
	        Object returnedObject) throws AccessDeniedException
	        {
				System.out.println("---------------- After invocation ----------------------");
				//PersonDO person;
				if(returnedObject instanceof Object)
				{
					//person = (PersonDO)returnedObject;
					System.out.println("Objektet er et PersonDO objekt.");
					//System.out.println("Personens fornavn er: " + person.getFornavn());
					
				}	
				System.out.println("---------------- End After invocation ----------------------");
				return returnedObject;
	        }

	    /**
	     * Indicates whether this <code>AfterInvocationProvider</code> is able to participate in a decision
	     * involving the passed <code>ConfigAttribute</code>.<p>This allows the
	     * <code>AbstractSecurityInterceptor</code> to check every configuration attribute can be consumed by the
	     * configured <code>AccessDecisionManager</code> and/or <code>RunAsManager</code> and/or
	     * <code>AccessDecisionManager</code>.</p>
	     *
	     * @param attribute a configuration attribute that has been configured against the
	     *        <code>AbstractSecurityInterceptor</code>
	     *
	     * @return true if this <code>AfterInvocationProvider</code> can support the passed configuration attribute
	     */
	    public boolean supports(ConfigAttribute attribute)
	    {
	    	return true;
	    }

	    /**
	     * Indicates whether the <code>AfterInvocationProvider</code> is able to provide "after invocation"
	     * processing for the indicated secured object type.
	     *
	     * @param clazz the class of secure object that is being queried
	     *
	     * @return true if the implementation can process the indicated class
	     */
	    public boolean supports(Class clazz)
	    {
	    	return true;
	    }

}
