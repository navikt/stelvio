package no.nav.integration.framework.jndi;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import no.nav.integration.framework.service.IntegrationService;
import no.nav.common.framework.FrameworkError;
import no.nav.common.framework.error.SystemException;
import no.nav.common.framework.service.ServiceFailedException;
import no.nav.common.framework.service.ServiceRequest;
import no.nav.common.framework.service.ServiceResponse;


/**
 * DirectoryContextWriteService provides functionality for adding entries to a JNDI compatible 
 * directory services.
 * 
 * @author person5b7fd84b3197, Accenture
 * @version $Revision: 2804 $ $Author: skb2930 $ $Date: 2006-03-01 12:51:34 +0100 (Wed, 01 Mar 2006) $
 */
public class DirectoryContextWriteService extends IntegrationService {

	// Standard JNDI directory properties
	private String providerUrl = null;
	private String initialContextFactory = null;
	private String securityAuthentication = null;
	private String securityPrincipal = null;
	private String securityCredentials = null;

	// Additional LDAP directory properties
	private String ldapConnectionPool = "true";

	// Name of this component
	private String name = "";

	// The context to change
	private String executeOnApp = null;

	// The value to change
	private Map attributeList = null;
	// The search criteria
	private String contextName = null;

	// The execute type type
	private boolean delete = false;

	// The interface for accessing the directory service
	private DirContext context = null;

	/**
	 * Validates that the service is configured correctly and then 
	 * creates the initial directory context.
	 */
	public void init() {

		if (null == attributeList || attributeList.isEmpty()) {
			throw new SystemException(
				FrameworkError.SERVICE_INIT_ERROR,
				new IllegalStateException("The Map of attribute names and values must contain at least one entry"),
				name);
		}

		if (null == contextName) {
			throw new SystemException(
				FrameworkError.SERVICE_INIT_ERROR,
				new IllegalStateException("The directory context name can not be null"),
				name);
		}

		if (null == executeOnApp) {
			throw new SystemException(
				FrameworkError.SERVICE_INIT_ERROR,
				new IllegalStateException("The DN to change can not be null"),
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
			context = new InitialDirContext(env);
		} catch (NamingException ne) {
			throw new SystemException(FrameworkError.SERVICE_INIT_ERROR, ne);
		}
	}

	/**
	 * Execute the configured write opperation using specified arguments. 
	 * <p/>
	 * The service expects to find an object array on the 
	 * ServiceRequest using key named <code>FilterArguments</code>.
	 * <p/>
	 * If <code>delete</code> is configured to true (default is false),
	 * the configured write opperation will be to delete the configured attribute in 
	 * the LDAP context tree. Else the opperatin will be to append the configured
	 * parameter to the context.
	 *  
	 * {@inheritDoc}
	 */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {

		Object[] filterArguments = (Object[]) request.getData("FilterArguments");
		String cn = (String)filterArguments[0];
		String DN = "CN=" + cn + "," + executeOnApp;

		Set attributes = attributeList.keySet();
		ServiceResponse response = new ServiceResponse();
		Iterator keys = attributes.iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String filterFormat = (String) attributeList.get(key);
			String filter = (String) appendFilter(filterFormat, filterArguments);
			if (delete) {
				try {
					removeAttribute(filter, DN, key);
				} catch (OperationNotSupportedException opns) {
					throw new ServiceFailedException(
						FrameworkError.DIRECTORY_ATTRIBUTE_REMOVE_NOT_EXISTING,
						opns,
						new String[] { contextName, filter });
				}catch (NamingException ne) {
					throw new ServiceFailedException(
						FrameworkError.DIRECTORY_ATTRIBUTE_DEL_ERROR,
						ne,
						new String[] { contextName, filter });
				}
			} else {
				try {
					addAttribute(filter, DN, key);
				} catch (NameAlreadyBoundException nabe) {
					throw new ServiceFailedException(
						FrameworkError.DIRECTORY_ATTRIBUTE_ADD_ERROR_NAME_ALREADY_BOUND,
						nabe,
						new String[] { key, DN });
				} catch (NameNotFoundException nnfe) {
					throw new ServiceFailedException(
						FrameworkError.DIRECTORY_ATTRIBUTE_ADD_ERROR_NAME_NOT_FOUND,
						nnfe,
					   new String[] { key, DN });
			   } catch (NamingException ne) {
					throw new ServiceFailedException(
						FrameworkError.DIRECTORY_ATTRIBUTE_ADD_ERROR,
						ne,
						new String[] { key, DN });
				}
			}
		}

		return response;
	}

	/**
	 * This method will append arguments to the parameter text.
	 * The text has to be provided as <code>{#}</code>.
	 * eg. my name is {0}.
	 * 0 referes to the first position in the arguments
	 * @param filterArguments - All arguments
	 * @return the final string/search parameter
	 */
	private String appendFilter(String filterFormat, Object[] filterArguments) {
		SearchControls ctls = new SearchControls();
		ctls.setReturningObjFlag(true);
		ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		MessageFormat template = new MessageFormat(filterFormat);
		return template.format(filterArguments);
	}

	/**
	 * This method will add a attribute value (<code>setDN</code>) on the given <code>onDn</code>
	 * @param setDN - The value to set on the attribute
	 * @param onDN - The context path where to set the value
	 * @param attributeName - The name of the attribute to save
	 * @throws NamingException - If fail to add attribute 
	 */
	private void addAttribute(String setDN, String onDN, String attributeName) throws NamingException {
		ModificationItem[] mods = new ModificationItem[1];
		mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute(attributeName, setDN));

		context.modifyAttributes(onDN, mods);
	}

	/**
	 * This method will delete a attribute value (<code>delDN</code>) on the given <code>onDn</code>
	 * @param delDN - The value to set on the attribute
	 * @param onDN - The context path where to set the value
	 * @param attributeName - The name of the attribute to save
	 * @throws NamingException - If fail to add attribute 
	 */
	private void removeAttribute(String delDN, String onDN, String attributeName) throws NamingException {
		ModificationItem[] mods = new ModificationItem[1];
		mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(attributeName, delDN));

		context.modifyAttributes(onDN, mods);
	}

	/**
	 * Release open resources upon destruction.
	 * 
	 * @see Object#finalize()
	 */
	protected void finalize() throws Throwable {
		super.finalize();
		if (null != context) {
			try {
				context.close();
			} catch (NamingException ne) {
				throw new SystemException(FrameworkError.SERVICE_DESTROY_ERROR, ne);
			} finally {
				context = null;
			}
		}
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
	 * Set if the write opperation is a delete of add.
	 * Default is false, which means add attribute.
	 * 
	 * @param b - true if delete is wanted
	 */
	public void setDelete(boolean b) {
		delete = b;
	}

	/**
	 * Set the distinguishedName for where to find the attribute.
	 * @param string distinguishedName
	 */
	public void setExecuteOnApp(String string) {
		executeOnApp = string;
	}

	/**
	 * Set a Map of attributes and filtervalues to write
	 * in the LDAP.
	 * Example: <code>member=CN={0},CN=Gruppe,OU=Grupper,DC=trygdeetaten2,DC=local</code> 
	 * @param map
	 */
	public void setAttributeList(Map map) {
		attributeList = map;
	}


	/**
	 * Assigns the name of this component. The name is only used in error reporting.
	 * 
	 * @param name the name.
	 */
	public void setName(String name) {
		this.name = name;
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

}
