package no.trygdeetaten.common.framework.util;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.ejb.LookupHelper;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.service.ServiceNotFoundException;

/**
 * A LookupHelper implementation which can be use to lookup resources such as JMS queues, etc.
 *
 * @author person356941106810, Accenture
 * @version $Id: ResourceLookupHelper.java 2634 2005-11-18 08:56:08Z skb2930 $
 */
public class ResourceLookupHelper implements LookupHelper {

	/**
	 * Looks up the resource.
	 * 
	 * @{inheritDoc}
	 */
	public Object lookup(String name, Hashtable environment) throws ServiceNotFoundException {
		// Create the initial context, look up and narrow the remote home
		Context ctx = null;
		Object resource = null;

		try {
			ctx = new InitialContext(environment);

			try {
				resource = ctx.lookup(name);
			} catch (NamingException ne) {
				throw new ServiceNotFoundException(FrameworkError.SERVICE_LOOKUP_ERROR, ne, name);
			}
		} catch (NamingException e) {
			throw new ServiceNotFoundException(FrameworkError.SERVICE_CONTEXT_CREATION_ERROR, e);
		} finally {
			// Release any open resources
			if (null != ctx) {
				try {
					ctx.close();
				} catch (NamingException ne) {
					throw new SystemException(FrameworkError.SERVICE_CONTEXT_DELETION_ERROR, ne);
				}
			}
		}
		return resource;

	}

}
