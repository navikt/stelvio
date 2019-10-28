package no.stelvio.consumer.ws;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;

import com.ibm.ws.webservices.multiprotocol.AgnosticService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.security.ws.LTPASecurityHandler;
import no.stelvio.common.security.ws.UsernameTokenSecurityHandler;

/**
 * The Consumer Facade Base class.
 * 
 * @author $Author$
 * @version $Id$
 * @param <T> The service type
 *
 * @deprecated Use plain CXF, Spring or the like instead (non-IBM specific).
 */
@Deprecated
public abstract class ConsumerFacadeBase<T> {

	private Class<? extends AgnosticService> serviceLocatorClass;
	private AgnosticService serviceLocator;
	/** Service. */
	protected T service = null;
	/** Logger. */
	protected final Log log = LogFactory.getLog(this.getClass());

	private String portServerAddress = null;
	private String portFullAddress = null;

	private String serviceUsername = null;
	private String servicePassword = null;

	private boolean contextEnabled = true;
	private boolean securityEnabled = false;

	private boolean isUseUsernameToken = false;

	/**
	 * Constructor for ConsumerFacadeBase.
	 * 
	 * @param serviceLocatorClass
	 *            AgnosticService class
	 */
	public ConsumerFacadeBase(Class<? extends AgnosticService> serviceLocatorClass) {
		this.serviceLocatorClass = serviceLocatorClass;
		if (serviceLocatorClass == null) {
			IllegalArgumentException exception = new IllegalArgumentException(
					"serviceLocatorClass must be specified, null is not an acceptable value here.");
			log.error(exception.getMessage(), exception);
			throw exception;
		}
	}

	/**
	 * Sets the service.
	 * 
	 * Available in case of a later need to inject the generated interface.
	 * 
	 * @param serviceIf
	 *            the service
	 */
	public void setService(T serviceIf) {
		this.service = serviceIf;
	}

	/**
	 * Gets the service.
	 * 
	 * @return res the service
	 */
	@SuppressWarnings("unchecked")
	public final T getService() {
		if (this.service != null) {
			return this.service;
		}

		if (serviceLocator == null) {
			this.initServiceLocator();
		}

		String serviceLocatorName = serviceLocatorClass.getSimpleName();
		if (!serviceLocatorName.endsWith("ServiceLocator")) {
			throw new RuntimeException("Class " + serviceLocatorName
					+ " does not have the 'ServiceLocator' suffix, so its not usable for reflecting purposes");
		}
		String serviceName = serviceLocatorName.substring(0, serviceLocatorName.length() - 14); // Length of "ServiceLocator"+1

		if (portFullAddress == null) {
			String portAddress;
			try {
				Method getPortAddressMethod = serviceLocatorClass.getMethod("get" + serviceName + "PortAddress", new Class[0]);
				portAddress = (String) getPortAddressMethod.invoke(serviceLocator, new Object[0]);
			} catch (Exception e) {
				throw new RuntimeException("Unable to retrieve a port address using the service locator class "
						+ serviceLocatorClass.getName(), e);
			}
			if (!portAddress.startsWith("http://")) {
				throw new RuntimeException("The port address '" + portAddress
						+ "' does not start with 'http://'. Can not interpret this.");
			}

			int urlSeparator = portAddress.indexOf("/", 8);
			if (urlSeparator == -1) {
				throw new RuntimeException("Did not find additional occurences of / separator (after http://..) "
						+ "in the string '" + portAddress + "'. Unable to continue");
			}

			String defaultLocalURL = portAddress.substring(urlSeparator + 1, portAddress.length());
			if (portServerAddress == null) {
				throw new RuntimeException("portServerAddress or portFullAddress not injected");
			}
			this.portFullAddress = portServerAddress + defaultLocalURL;
		}
		log.debug("Service " + serviceLocator.getServiceName() + " using URL " + portFullAddress);

		try {
			Method getServicePortMethod = serviceLocatorClass
					.getMethod("get" + serviceName + "Port", new Class[] { URL.class });
			T res = (T) getServicePortMethod.invoke(serviceLocator, new Object[] { new URL(portFullAddress) });
			if (res == null) {
				throw new NullPointerException("Empty result from getServicePortMethod");
			}
			return res;
		} catch (Exception e) {
			throw new RuntimeException("Error while getting service port using service locator class "
					+ serviceLocatorClass.getName() + " and port address " + portFullAddress, e);
		}
	}

	/**
	 * Initialises the service locator. The method handler list based on the
	 * consumer context, security and port settings.
	 * 
	 */
	private void initServiceLocator() {
		try {
			serviceLocator = serviceLocatorClass.newInstance();

			List<HandlerInfo> handlerList = new ArrayList<>();
			if (isContextEnabled()) {
				HandlerInfo navContextHInfo = new HandlerInfo(ConsumerContextHandler.class, null, null);
				handlerList.add(navContextHInfo);
			}
			if (isSecurityEnabled()) {
				HandlerInfo securityHInfo = createSecurityHandlerInfo();
				handlerList.add(securityHInfo);
			}

			Iterator portIterator = serviceLocator.getPorts();
			while (portIterator.hasNext()) {
				QName portName = ((QName) portIterator.next());
				serviceLocator.getHandlerRegistry().setHandlerChain(portName, handlerList); 
				// An improvement could be to check if already present
			}
		} catch (Exception e) {
			log.error("Error while setting up handlerchain for outgoing service:" + serviceLocatorClass.getName(), e);
		}
	}

	/**
	 * Creates a security handler info.
	 * 
	 * @return the handler info
	 */
	protected HandlerInfo createSecurityHandlerInfo() {
		if (log.isDebugEnabled()) {
			log.debug("Creates handlerinfo in " + serviceLocatorClass.getSimpleName() + " with username and password: " + serviceUsername);
		}
		if (isUseUsernameToken()) {
			return createUsernameTokenSecurityHandler();
		} else {
			return createLTPASecurityHandler();
		}
	}

	/**
	 * Creates the TPA security handler.
	 * 
	 * @return security handler
	 */
	protected HandlerInfo createLTPASecurityHandler() {
		Map<String, String> config = new HashMap<>();
		config.put(LTPASecurityHandler.USERNAME_CONFIG_STRING, this.serviceUsername);
		config.put(LTPASecurityHandler.PASSWORD_CONFIG_STRING, this.servicePassword);
		HandlerInfo securityHInfo = new HandlerInfo(getLTPASecurityHandlerClass(), config, null);
		if (log.isDebugEnabled()) {
			log.debug("Config for LTPASecurityHandler contains username and password: " + config.get(LTPASecurityHandler.USERNAME_CONFIG_STRING));
		}
		return securityHInfo;
	}

	/**
	 * Override this to provide more specific handler subclass
	 */
	protected Class<? extends GenericHandler> getLTPASecurityHandlerClass() {
		return LTPASecurityHandler.class;
	}

	/**
	 * Creates the username security handler.
	 * 
	 * @return security handler
	 */
	protected HandlerInfo createUsernameTokenSecurityHandler() {
		Map<String, String> config = new HashMap<>();
		config.put(UsernameTokenSecurityHandler.USERNAME_CONFIG_STRING, this.serviceUsername);
		config.put(UsernameTokenSecurityHandler.PASSWORD_CONFIG_STRING, this.servicePassword);
		return new HandlerInfo(getUsernameTokenSecurityHandlerClass(), config, null);
	}

	/**
	 * Override this to provide more specific handler subclass
	 */
	protected Class<? extends GenericHandler> getUsernameTokenSecurityHandlerClass() {
		return UsernameTokenSecurityHandler.class;
	}

	/**
	 * Sets the portServerAddress.
	 * 
	 * @param serverAddress
	 *            the PortServerAddress
	 */
	public void setPortServerAddress(String serverAddress) {
		this.portServerAddress = serverAddress;
	}

	/**
	 * Sets the portFullAddress.
	 * 
	 * @param portFullAddress
	 *            the PortFullAddress
	 */
	public void setPortFullAddress(String portFullAddress) {
		this.portFullAddress = portFullAddress;
	}

	/**
	 * Returns the enabled state of the security.
	 * 
	 * @return securityEnabled enabled state of the security
	 */
	public boolean isSecurityEnabled() {
		return securityEnabled;
	}

	/**
	 * Sets the enabled state of the security.
	 * 
	 * @param securityEnabled
	 *            the enabled state of the security
	 */
	public void setSecurityEnabled(boolean securityEnabled) {
		this.securityEnabled = securityEnabled;
	}

	/**
	 * Returns the enabled state of the context.
	 * 
	 * @return contextEnabled enabled state of the contact
	 */
	public boolean isContextEnabled() {
		return contextEnabled;
	}

	/**
	 * Sets the enabled state of the context.
	 * 
	 * @param enableContext
	 *            the new enabled state of the context
	 */
	public void setContextEnabled(boolean enableContext) {
		this.contextEnabled = enableContext;
	}

	/**
	 * Returns the service password.
	 * 
	 * @return servicePassword the service password
	 */
	public String getServicePassword() {
		return servicePassword;
	}

	/**
	 * Sets the service password.
	 * 
	 * @param servicePassword
	 *            the service password
	 */
	public void setServicePassword(String servicePassword) {
		this.servicePassword = servicePassword;
	}

	/**
	 * Returns the service username.
	 * 
	 * @return serviceUsername the service username
	 */
	public String getServiceUsername() {
		return serviceUsername;
	}

	/**
	 * Sets the service username.
	 * 
	 * @param serviceUsername
	 *            the service username
	 */
	public void setServiceUsername(String serviceUsername) {
		if (log.isDebugEnabled()) {
			log.debug("Sets the service username: " + serviceUsername);
		}
		this.serviceUsername = serviceUsername;

	}

	/**
	 * Sets the use username token.
	 * 
	 * @param isUseUsernameToken the useUsernameToken
	 */
	public void setUseUsernameToken(boolean isUseUsernameToken) {
		this.isUseUsernameToken = isUseUsernameToken;
	}

	/**
	 * Return the use username token.
	 * 
	 * @return the useUsernameToken
	 */
	public boolean isUseUsernameToken() {
		return isUseUsernameToken;
	}
}
