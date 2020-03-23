package no.stelvio.common.security.authorization.method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AfterInvocationProvider;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.intercept.AfterInvocationManager;
import org.springframework.security.core.Authentication;

/**
 * Provider-based implementation of <code>AfterInvocationManager</code>.
 * <p>
 * Handles configuration of a bean context defined list of <code>AfterInvocationProvider</code>s. The list of
 * <code>AfterInvocationProvider</code>s is retrieved from a <code>ConfigAttributeDefinition</code> which is derived from an
 * <code>ObjectDefinitionSource</code>. The <code>ObjectDefinitionSource</code> is defined in a bean context and injected into a
 * security interceptor.
 * </p>
 * <p>
 * Every <code>AfterInvocationProvider</code> will be polled when the <code>decide(...)</code> method is called. The
 * <code>Object</code> returned from each provider will be presented to the successive provider for processing. This means each
 * provider <b>must</b> ensure they return the <code>Object</code>, even if they are not interested in the "after invocation"
 * decision.
 * </p>
 * 
 * @version $Id$
 */
public class AfterInvocationProviderManager implements AfterInvocationManager {

	private static final Log LOGGER = LogFactory.getLog(AfterInvocationProviderManager.class);

	private List<AfterInvocationProvider> providers;

	/**
	 * Iterates through the attributes in a ConfigAttributeDefinition and creates a list of <code>AfterInvocationProvider</code>
	 * s based on these.
	 * 
	 * @throws AfterInvocationProviderNotFoundException
	 *             if one of the attributes do not represent a class.
	 */
	public void addProviders( Collection<ConfigAttribute> configAttributes) throws AfterInvocationProviderNotFoundException {

		this.providers = new ArrayList<>();
		Iterator<?> iterator = configAttributes.iterator();
		ConfigAttribute configAttribute = null;
		try {
			while (iterator.hasNext()) {
				configAttribute = (ConfigAttribute) iterator.next();
				Class clazz = Class.forName(configAttribute.getAttribute(), true, Thread.currentThread()
						.getContextClassLoader());

				if (isAfterInvocationProvider(clazz)) {
					AfterInvocationProvider provider = (AfterInvocationProvider) clazz.newInstance();
					this.providers.add(provider);
				}
			}
		} catch (ClassNotFoundException ex) {
			throw new AfterInvocationProviderNotFoundException("Class '" + configAttribute + "' not found", ex);

		} catch (InstantiationException ine) {
			throw new AfterInvocationProviderNotFoundException("Class '" + configAttribute + "' cannot be instantiated. It"
					+ " is either an interface or abstract class.", ine);

		} catch (IllegalAccessException ile) {
			throw new AfterInvocationProviderNotFoundException("Could not create an instance of class '" + configAttribute
					+ "'", ile);

		}
	}

	/**
	 * Given the details of a secure object invocation including its returned <code>Object</code>, make an access control
	 * decision or optionally modify the returned <code>Object</code>.
	 */
    @Override
    public Object decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes, Object returnedObject) throws AccessDeniedException {

		// populate the provider list with respect to the config attributes
		addProviders(configAttributes);
		Iterator<AfterInvocationProvider> iter = this.providers.iterator();
		Object result = returnedObject;

		while (iter.hasNext()) {
			AfterInvocationProvider provider = iter.next();
			result = provider.decide(authentication, object, configAttributes, result);
		}
		return result;
	}

	/**
	 * Returns the list of <code>AfterInvocationProvider</code>s.
	 * 
	 * @return the provider list.
	 */
	public List<AfterInvocationProvider> getProviders() {
		return this.providers;
	}

	/**
	 * Checks if the presented class is an <code>AfterInvocationProvider</code> implementation.
	 * 
	 * @param clazz
	 *            the class to check
	 * @return <code>true</code> if the class is an <code>AfterInvocationProvider</code>, <code>false</code> otherwise.
	 */
	public boolean isAfterInvocationProvider(Class clazz) {
		return AfterInvocationProvider.class.isAssignableFrom(clazz);
	}

	/**
	 * Iterates through all <code>AfterInvocationProvider</code>s and ensures each can support the presented class.
	 * <p>
	 * If one or more providers cannot support the presented class, <code>false</code> is returned.
	 * </p>
	 * 
	 * @param clazz
	 *            the secure object class being queries
	 * 
	 * @return if the <code>AfterInvocationProviderManager</code> can support the secure object class, which requires every one
	 *         of its <code>AfterInvocationProvider</code>s to support the secure object class
	 */
	@Override
	public boolean supports(Class clazz) {
		boolean canSupport = true;
		if (this.providers != null) {
			Iterator<AfterInvocationProvider> iter = this.providers.iterator();
			while (iter.hasNext()) {
				AfterInvocationProvider provider = iter.next();
				if (!provider.supports(clazz)) {
					canSupport = false;
					break;
				}
			}
		}
		return canSupport;
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		boolean canSupport = false;
		if (this.providers != null) {
			Iterator<AfterInvocationProvider> iter = this.providers.iterator();

			while (iter.hasNext()) {
				AfterInvocationProvider provider = iter.next();

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Evaluating " + attribute + " against " + provider);
				}

				if (provider.supports(attribute)) {
					canSupport = true;
					break;
				}
			}
		} else {
			canSupport = true;
		}
		return canSupport;
	}

}
