package no.trygdeetaten.common.framework.service;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.config.Config;
import no.trygdeetaten.common.framework.config.ConfigurationException;

/**
 * LocalServiceLocator is the base class that should be extended by 
 * service locator implementations locating local services.
 * 
 * @author person7553f5959484
 * @version $Revision: 2827 $ $Author: skb2930 $ $Date: 2006-03-05 10:37:16 +0100 (Sun, 05 Mar 2006) $
 */
public class LocalServiceLocator implements ServiceLocator {

	// Localservice configurations
	private Config configurations = null;

	/**
	 * Constructs a service locator that locates local services using
	 * configurations persisted in specified file.
	 * 
	 * @param filename the name of the config file where services are described
	 */
	public LocalServiceLocator(String filename) {
		configurations = Config.getConfig(filename);
	}

	/**
	 * Lookup the local service specified.
	 * <p/>
	 * The local services are configured in the file specified in the protected constructor. 
	 * 
	 * {@inheritDoc}
	 */
	public Service lookup(String name) throws ServiceNotFoundException {
		if (null == name || name.length() == 0) {
			throw new ServiceNotFoundException(FrameworkError.SERVICE_NAME_MISSING);
		}
		try {
			return (LocalService) configurations.getBean(name);
		} catch (ConfigurationException e) {
			throw new ServiceNotFoundException(FrameworkError.SERVICE_CREATION_ERROR, e);
		}
	}
}
