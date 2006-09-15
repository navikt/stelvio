package no.nav.common.framework.ejb;

import java.util.Hashtable;

import no.nav.common.framework.service.ServiceNotFoundException;

/**
 * Interface defining functionality for looking up objects from a registry.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1047 $ $Author: psa2920 $ $Date: 2004-08-17 11:44:29 +0200 (Tue, 17 Aug 2004) $
 */
public interface LookupHelper {

	/**
	 * Lookup the named object.
	 * 
	 * @param name 			the name of the object used to bind it in the registry.
	 * @param environment	the environment used to access the registry.
	 * @return 					an instance of the identified object.
	 * @throws ServiceNotFoundException if the object can not be found.
	 */
	Object lookup(String name, Hashtable environment) throws ServiceNotFoundException;
}
