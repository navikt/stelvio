package no.nav.integration.framework.jndi;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.nav.integration.framework.service.IntegrationService;
import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.performance.MonitorKey;
import no.trygdeetaten.common.framework.performance.PerformanceMonitor;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;


/**
 * DirectoryContextService provides functionality for searching JNDI compatible 
 * directory services.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2420 $ $Author: tma2920 $ $Date: 2005-08-17 13:15:27 +0200 (Wed, 17 Aug 2005) $
 */
public class DirectoryContextService extends IntegrationService {

	// Name of this component
	private String name = "";

	/** The performance monitoring key */
	protected static final MonitorKey MONITOR_KEY = new MonitorKey("DirectoryService", MonitorKey.RESOURCE);

	// Standard JNDI directory properties
	private String providerUrl = null;
	private String initialContextFactory = null;
	private String securityAuthentication = null;
	private String securityPrincipal = null;
	private String securityCredentials = null;

	// Additional LDAP directory properties
	private String ldapConnectionPool = "true";

	// The search criteria
	private String contextName = null;
	private String filterFormat = null;

	// The expected result type
	private boolean expectMultipleEntries = false;
	private boolean expectMultipleValueAttributes = false;

	// The attributes of interest
	private List attributeList = null;

	// The interface for accessing the directory service
	private DirContext dirContext = null;

	// The log
	private final Log log = LogFactory.getLog(this.getClass());

	/**
	 * Validates that the service is configured correctly and then 
	 * creates the initial directory context.
	 */
	public void init() {

		if (null == this.attributeList || this.attributeList.size() < 1) {
			throw new SystemException(
				FrameworkError.SERVICE_INIT_ERROR,
				new IllegalStateException("The list of attribute names must contain at least one entry"),
				name);
		}

		if (null == this.contextName) {
			throw new SystemException(
				FrameworkError.SERVICE_INIT_ERROR,
				new IllegalStateException("The directory context name can not be null"),
				name);
		}

		if (null == this.filterFormat) {
			throw new SystemException(
				FrameworkError.SERVICE_INIT_ERROR,
				new IllegalStateException("The search filter format can not be null"),
				name);
		}

		Hashtable env = new Hashtable(11);
		// Assign standard JNDI directory properties
		env.put(Context.PROVIDER_URL, providerUrl);
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
		env.put(Context.SECURITY_AUTHENTICATION, securityAuthentication);
		env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
		env.put(Context.SECURITY_CREDENTIALS, securityCredentials);
		// Assign additional LDAP directory properties
		env.put("com.sun.jndi.ldap.connect.pool", ldapConnectionPool);

		// Create the initial context
		try {
			dirContext = new InitialDirContext(env);
		} catch (NamingException ne) {
			throw new SystemException(FrameworkError.SERVICE_INIT_ERROR, ne);
		}
	}

	/**
	 * Execute the configured search using specified arguments. 
	 * <p/>
	 * The service expects to find an object array on the 
	 * ServiceRequest using key named <code>FilterArguments</code>.
	 * <p/>
	 * If <code>expectMultipleEntries</code> is configured to true (default is false),
	 * a <code>java.util.List</code> of entries will be sent with the ServiceResponse 
	 * using key named <code>entries</code>, otherwise the attributes will be sent with
	 * the ServiceResponse using the names specified in the <code>attributeList</code>. 
	 * See <code>enterprise-services.xml</code>.
	 * <p/>
	 * If <code>expectMultipleValueAttributes</code> is configured to true (default is false),
	 * the attributes will be added to a <code>java.util.List</code>, otherwise each attribute 
	 * will be directly accessible.
	 * 
	 * {@inheritDoc}
	 * @see no.nav.integration.framework.service.IntegrationService#doExecute(no.trygdeetaten.common.framework.service.ServiceRequest)
	 */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {

		Object[] filterArguments = (Object[]) request.getData("FilterArguments");

		SearchControls ctls = new SearchControls();
		ctls.setReturningObjFlag(true);
		ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		MessageFormat template = new MessageFormat(filterFormat);
		String filter = template.format(filterArguments);

		NamingEnumeration ldapEntries = null;
		try {
			PerformanceMonitor.start(MONITOR_KEY, request.getServiceName());
			ldapEntries = dirContext.search(contextName, filter, ctls);
			PerformanceMonitor.end(MONITOR_KEY);
		} catch (NamingException ne) {
			PerformanceMonitor.fail(MONITOR_KEY);

			// Re-initialize and then try again
			this.init();

			try {
				PerformanceMonitor.start(MONITOR_KEY, request.getServiceName());
				ldapEntries = dirContext.search(contextName, filter, ctls);
				PerformanceMonitor.end(MONITOR_KEY);
			} catch (NamingException e) {
				PerformanceMonitor.fail(MONITOR_KEY);
				throw new ServiceFailedException(
					FrameworkError.DIRECTORY_SEARCH_FAILED,
					ne,
					new String[] { contextName, filter });
			} catch (RuntimeException re) {
				PerformanceMonitor.fail(MONITOR_KEY);
				throw re;
			}
		}

		ServiceResponse response = new ServiceResponse();
		if (null != ldapEntries) {
			if (expectMultipleEntries) {
				addMultipleEntriesToResponse(response, ldapEntries);
			} else {
				addSingleEntryToResponse(response, ldapEntries);
			}
		}
		return response;
	}

	/**
	 * Add the multiple LDAP entries to the service response.
	 * 
	 * @param response the ServiceResponse where entries should be added
	 * @param ldapEntries the NamingEnumeration holding the LDAP entries
	 */
	private void addMultipleEntriesToResponse(ServiceResponse response, NamingEnumeration ldapEntries) {

		// The list of entries to return on the response
		List entryList = new ArrayList();
		while (ldapEntries.hasMoreElements()) {

			SearchResult searchResult = null;
			try {
				searchResult = (SearchResult) ldapEntries.next();
			} catch (NamingException ne) {
				// This will never occur because of the 
				// hasMoreElements() test above
				if (log.isWarnEnabled()) {
					log.warn(
						"NamingEnumeration.next() threw NamingException even though "
							+ "we tested for NamingEnumeration.hasMoreElements()",
						ne);
				}
			}

			// Only retrive attributes from the entry if configured to do so
			if (null != searchResult && null != attributeList) {

				// Retrieve the attribute for this entry
				Attributes attributes = searchResult.getAttributes();
				int numOfConfiguredAttributes = attributeList.size();
				if (attributes != null && 0 < numOfConfiguredAttributes) {

					// Only retrive the configured attributes
					Map attributeMap = new HashMap(numOfConfiguredAttributes);
					for (int i = 0; i < numOfConfiguredAttributes; i++) {

						// Retrieve the named attribute 
						String attributeName = (String) attributeList.get(i);
						Attribute ldapAttribute = attributes.get(attributeName);

						// This might be a multi-value attribute, retrieve all values
						NamingEnumeration ldapAttributeValues = null;
						try {
							ldapAttributeValues = ldapAttribute.getAll();
						} catch (NamingException e) {
							throw new SystemException(
								FrameworkError.DIRECTORY_ATTRIBUTE_VALUE_ERROR,
								e,
								new String[] { attributeName });
						}

						if (expectMultipleValueAttributes) {
							attributeMap.put(attributeName, getMultiValueAttribute(ldapAttributeValues));
						} else {
							attributeMap.put(attributeName, getSingleValueAttribute(ldapAttributeValues));
						}

					}
					// Append the entry to the response
					entryList.add(attributeMap);
				}
			}

		}
		response.setData("entries", entryList);
	}

	/**
	 * Add the single LDAP entry to the service response.
	 * 
	 * @param response the ServiceResponse where entries should be added
	 * @param ldapEntries the NamingEnumeration holding the single LDAP entry
	 */
	private void addSingleEntryToResponse(ServiceResponse response, NamingEnumeration ldapEntries) {

		if (ldapEntries.hasMoreElements()) {

			SearchResult searchResult = null;
			try {
				searchResult = (SearchResult) ldapEntries.next();
			} catch (NamingException ne) {
				// This will never occur because of the 
				// hasMoreElements() test above
				if (log.isWarnEnabled()) {
					log.warn(
						"NamingEnumeration.next() threw NamingException even though "
							+ "we tested for NamingEnumeration.hasMoreElements()",
						ne);
				}
			}

			// Only retrive attributes from the entry if configured to do so
			if (null != searchResult && null != attributeList) {

				// Retrieve the attribute for this entry
				Attributes attributes = searchResult.getAttributes();
				int numOfConfiguredAttributes = attributeList.size();
				if (attributes != null && 0 < numOfConfiguredAttributes) {

					// Only retrive the configured attributes
					for (int i = 0; i < numOfConfiguredAttributes; i++) {

						// Retrieve the named attribute 
						String attributeName = (String) attributeList.get(i);
						Attribute ldapAttribute = attributes.get(attributeName);

						// This might be a multi-value attribute, retrieve all values
						NamingEnumeration ldapAttributeValues = null;
						if (ldapAttribute != null) {
							try {
								ldapAttributeValues = ldapAttribute.getAll();
							} catch (NamingException e) {
								throw new SystemException(
									FrameworkError.DIRECTORY_ATTRIBUTE_VALUE_ERROR,
									e,
									new String[] { attributeName });
							}
						}

						if (expectMultipleValueAttributes) {
							response.setData(attributeName, getMultiValueAttribute(ldapAttributeValues));
						} else {
							response.setData(attributeName, getSingleValueAttribute(ldapAttributeValues));
						}
					}
				}
			}

		}
	}

	/**
	 * Retrieves all attribute values from the input as a List.
	 * @param ldapAttributeValues the attribute values to convert to a list.
	 * @return a list of all attribute values.
	 */
	private List getMultiValueAttribute(NamingEnumeration ldapAttributeValues) {

		if (null == ldapAttributeValues) {
			return null;
		} else {
			List values = new ArrayList();
			while (ldapAttributeValues.hasMoreElements()) {
				try {
					values.add(ldapAttributeValues.next());
				} catch (NamingException ne) {
					// This will never occur because of the 
					// hasMoreElements() test above
					if (log.isWarnEnabled()) {
						log.warn(
							"NamingEnumeration.next() threw NamingException even though "
								+ "we tested for NamingEnumeration.hasMoreElements()",
							ne);
					}
				}
			}
			return values;
		}
	}

	/**
	 * Retrieves the first attribute value from the input.
	 * @param ldapAttributeValues the list to retrieve the input from.
	 * @return the first attribute value in the input.
	 */
	private Object getSingleValueAttribute(NamingEnumeration ldapAttributeValues) {
		if (null == ldapAttributeValues) {
			return null;
		} else {
			Object value = null;
			if (ldapAttributeValues.hasMoreElements()) {
				try {
					value = ldapAttributeValues.next();
				} catch (NamingException ne) {
					// This will never occur because of the 
					// hasMoreElements() test above
					if (log.isWarnEnabled()) {
						log.warn(
							"NamingEnumeration.next() threw NamingException even though "
								+ "we tested for NamingEnumeration.hasMoreElements()",
							ne);
					}
				}
			}
			return value;
		}
	}

	/**
	 * Release open resources upon destruction.
	 * 
	 * {@inheritDoc}
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		super.finalize();
		if (null != dirContext) {
			try {
				dirContext.close();
			} catch (NamingException ne) {
				throw new SystemException(FrameworkError.SERVICE_DESTROY_ERROR, ne);
			} finally {
				dirContext = null;
			}
		}
	}

	/**
	 * Assigns a list of attributes of interest. Only attributes specified 
	 * in this list will be returned by the service. The list must at least 
	 * contain one attribute.
	 * 
	 * @param attributeList the names of the attributes to return
	 */
	public void setAttributeList(List attributeList) {
		this.attributeList = attributeList;
	}

	/**
	 * Assigns the name of the directory context to search.
	 * <p/>
	 * Example: <code>acf2admingrp=lids,host=TWAS</code>
	 * 
	 * @param contextName the directory context
	 */
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	/**
	 * Decides whether to expect search result to contain multiple entries or not.
	 * 
	 * @param expectMultipleEntries true if you expect multiple entries, false otherwise
	 */
	public void setExpectMultipleEntries(boolean expectMultipleEntries) {
		this.expectMultipleEntries = expectMultipleEntries;
	}

	/**
	 * Assigns the template to merge with an array of arguments to format the filter
	 * to be used as search criteria.
	 * <p/>
	 * Example: <code>(&(acf2lid={0})(objectclass=acf2lid))</code>
	 * 
	 * @param filterFormat the search criteria filter template
	 */
	public void setFilterFormat(String filterFormat) {
		this.filterFormat = filterFormat;
	}

	/**
	 * Assigns the class name of the initial context factory to be used 
	 * for looking up and accessing the directory service.
	 * <p/>
	 * Example: <code>com.sun.jndi.ldap.LdapCtxFactory</code>
	 * 
	 * @param initialContextFactory the initial context factory class name.
	 */
	public void setInitialContextFactory(String initialContextFactory) {
		this.initialContextFactory = initialContextFactory;
	}

	/**
	 * Deceides whether to use connection pooling or not when connecting to
	 * a LDAP based directory service.
	 * 
	 * @param ldapConnectionPool true to use pooling, false otherwise
	 */
	public void setLdapConnectionPool(String ldapConnectionPool) {
		this.ldapConnectionPool = ldapConnectionPool;
	}

	/**
	 * Assigns the URL to the provider where the directory service is located.
	 * <p/>
	 * Example: <code>ldap://IP-ADDRESS:TCP-PORT</code>
	 * 
	 * @param providerUrl the URL.
	 */
	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}

	/**
	 * Assigns the type of authentication to use when connecting to the directory service.
	 * <p/>
	 * Example: <code>simple</code>
	 * 
	 * @param securityAuthentication the authentication type
	 */
	public void setSecurityAuthentication(String securityAuthentication) {
		this.securityAuthentication = securityAuthentication;
	}

	/**
	 * Set the security credentials to be used for accessing the directory service.
	 * <p/>
	 * Example: <code>PASSWORD</code>
	 * 
	 * @param securityCredentials the security credentials.
	 */
	public void setSecurityCredentials(String securityCredentials) {
		this.securityCredentials = securityCredentials;
	}

	/**
	 * Set the security principal to be used for accessing the directory service.
	 * <p/>
	 * Example: <code>acf2lid=USERNAME</code>
	 * 
	 * @param securityPrincipal the security principal.
	 */
	public void setSecurityPrincipal(String securityPrincipal) {
		this.securityPrincipal = securityPrincipal;
	}

	/**
	 * Decides whether to expect attributes to contain multiple values or not.
	 * 
	 * @param expectMultipleValueAttributes true if you expect multiple values, false otherwise
	 */
	public void setExpectMultipleValueAttributes(boolean expectMultipleValueAttributes) {
		this.expectMultipleValueAttributes = expectMultipleValueAttributes;
	}

	/**
	 * Assigns the name of this component. The name is only used in error reporting.
	 * 
	 * @param name the name.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
