package no.trygdeetaten.common.framework.ejb;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.ejb.EJBHome;
import javax.naming.Context;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.config.Config;
import no.trygdeetaten.common.framework.config.ConfigurationException;
import no.trygdeetaten.common.framework.service.Service;
import no.trygdeetaten.common.framework.service.ServiceLocator;
import no.trygdeetaten.common.framework.service.ServiceNotFoundException;

/**
 * RemoteServiceLocator is a <i>service locator</i> implementation for looking up 
 * remote services configured in <u>distributed-services.xml</u>.
 * 
 * @author person7553f5959484
 * @version $Revision: 2331 $ $Author: psa2920 $ $Date: 2005-06-09 13:57:17 +0200 (Thu, 09 Jun 2005) $
 */
public class RemoteServiceLocator implements ServiceLocator {

	// Remote service configurations
	protected Config configurations = null;

	// Lookup helper for looking up the EJB home objects
	private LookupHelper lookupHelper = null;

	// Local cache of remote services
	private Map cache = new HashMap();

	/**
	 * Constructs a default remote service locator.
	 */
	public RemoteServiceLocator() {
		configurations = Config.getConfig(Config.DISTRIBUTED_SERVICES);
	}

	/**
	 * Assigns a helper class for looking up EJB home objects.
	 * 
	 * @param lookupHelper the helper to use.
	 */
	public void setLookupHelper(LookupHelper lookupHelper) {
		this.lookupHelper = lookupHelper;
	}

	/**
	 * Lookup the remote service specified.
	 * <p/>
	 * Remote Service home objects are cached locally unless specified otherwise. The remote services
	 * are configured in the file specified in the protected constructor. Each remote service 
	 * is specified using a {@link RemoteServiceDescription}.
	 * 
	 * {@inheritDoc}
	 * @see no.trygdeetaten.common.framework.service.ServiceLocator#lookup(java.lang.String)
	 */
	public Service lookup(String name) throws ServiceNotFoundException {

		if (null == name || name.length() == 0) {
			throw new ServiceNotFoundException(FrameworkError.SERVICE_NAME_MISSING);
		}

		// Search for the EJB home object in the local cache first,
		// then lookup the object in JNDI services if not found locally
		EJBHome home = (EJBHome) cache.get(name);
		if (null == home) {

			// Search for service description using the configuration services
			RemoteServiceDescription description = null;
			try {
				description = (RemoteServiceDescription) configurations.getBean(name);
			} catch (ConfigurationException e) {
				throw new ServiceNotFoundException(FrameworkError.SERVICE_NAME_NOT_FOUND, e, name);
			}

			if (null == description) {
				throw new ServiceNotFoundException(FrameworkError.SERVICE_NAME_NOT_FOUND, null, name);
			}
			if (null == description.getJndiName() || description.getJndiName().length() == 0) {
				throw new ServiceNotFoundException(FrameworkError.SERVICE_CONFIG_ERROR);
			}

			// Setup environment for creation of the initial context
			Hashtable env = new Hashtable();
			if (null != description.getProviderUrl()) {
				env.put(Context.PROVIDER_URL, description.getProviderUrl());
			}
			if (null != description.getInitialContextFactory()) {
				env.put(Context.INITIAL_CONTEXT_FACTORY, description.getInitialContextFactory());
			}
			if (null != description.getUrlPkgPrefixes()) {
				env.put(Context.URL_PKG_PREFIXES, description.getUrlPkgPrefixes());
			}
			if (null != description.getSecurityPrincipal()) {
				env.put(Context.SECURITY_PRINCIPAL, description.getSecurityPrincipal());
			}
			if (null != description.getSecurityCredentials()) {
				env.put(Context.SECURITY_CREDENTIALS, description.getSecurityCredentials());
			}

			home = (EJBHome) lookupHelper.lookup(description.getJndiName(), env);

			// Add cacheable home objects to the local cache
			if (description.isCacheable()) {
				cache.put(name, home);
			}
		}

		// Create the remote object using reflection
		try {
			RemoteService service = (RemoteService) home.getClass().getDeclaredMethod("create", null).invoke(home, null);
			return service;
		} catch (IllegalArgumentException e) {
			throw new ServiceNotFoundException(FrameworkError.SERVICE_CREATION_ERROR, e);
		} catch (SecurityException e) {
			throw new ServiceNotFoundException(FrameworkError.SERVICE_CREATION_ERROR, e);
		} catch (IllegalAccessException e) {
			throw new ServiceNotFoundException(FrameworkError.SERVICE_CREATION_ERROR, e);
		} catch (InvocationTargetException e) {
			throw new ServiceNotFoundException(FrameworkError.SERVICE_CREATION_ERROR, e);
		} catch (NoSuchMethodException e) {
			throw new ServiceNotFoundException(FrameworkError.SERVICE_CREATION_ERROR, e);
		}
	}

}
