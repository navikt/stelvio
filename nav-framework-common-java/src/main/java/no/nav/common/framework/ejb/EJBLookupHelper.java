package no.nav.common.framework.ejb;

import java.util.Hashtable;

import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import no.nav.common.framework.FrameworkError;
import no.nav.common.framework.error.SystemException;
import no.nav.common.framework.service.ServiceNotFoundException;

/**
 * Implementation of LookupHelper used to look up and narrow EJB Home objects
 * using JNDI.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2331 $ $Author: psa2920 $ $Date: 2005-06-09 13:57:17 +0200 (Thu, 09 Jun 2005) $
 */
public class EJBLookupHelper implements LookupHelper {

	/**
	 * Lookup an EJBHome object from JNDI using specified name and environment to construct the initial context.
	 * 
	 * {@inheritDoc}
	 * @see no.nav.common.framework.ejb.LookupHelper#lookup(java.lang.String, java.util.Hashtable)
	 */
	public Object lookup(String name, Hashtable environment) throws ServiceNotFoundException {

		// Create the initial context, look up and narrow the remote home
		Context ctx = null;
		EJBHome home = null;

		try {
			// Create initial context
			ctx = new InitialContext(environment);
			try {
				// Lookup remote object
				Object o = ctx.lookup(name);
				// Narrow remote object to EJBHome
				home = (EJBHome) PortableRemoteObject.narrow(o, EJBHome.class);

			} catch (NamingException ne) {
				throw new ServiceNotFoundException(FrameworkError.SERVICE_LOOKUP_ERROR, ne, name);
			} catch (ClassCastException cce) {
				throw new ServiceNotFoundException(FrameworkError.SERVICE_TYPE_ERROR, cce);
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
		return home;
	}
}
