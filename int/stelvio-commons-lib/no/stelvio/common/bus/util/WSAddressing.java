/*
 * Created on Mar 12, 2008
 *
 */
package no.stelvio.common.bus.util;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.logging.Logger;

import com.ibm.websphere.bo.BOFactory;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.addressing.EndpointReference;
import com.ibm.websphere.sibx.smobo.SOAPHeaderType;
import com.ibm.websphere.sibx.smobo.ServiceMessageObjectFactory;
import com.ibm.wsspi.sca.addressing.AttributedURI;

/**
 * Utility class for adding WSAddressing SOAP headers which can used in a custom mediation node
 * 
 * @author Torbjørn Staff accenture, persona2c5e3b49756 Schnell IBM SWG
 * 
 */
public class WSAddressing {

	// Logger - Sample - log.logp(Level.FINEST, className, <yourMethod>, <yourText>);
	private final static String className = WSAddressing.class.getName();
	@SuppressWarnings("unused")
	private final Logger log = Logger.getLogger(className);

	public static final URI DEFAULT_NAMESPACEURI = URI.create("http://schemas.xmlsoap.org/ws/2004/08/addressing");
	public static final URI DEFAULT_ACTIONURI = URI.create("");
	public static final String DEFAULT_NAMESPACEPREFIX = "wsa";

	/**
	 * Default WSAddressing.Factory instance using default namespace and prefix
	 */
	// public static final Factory FACTORY = new Factory(DEFAULT_NAMESPACEURI, DEFAULT_NAMESPACEPREFIX);
	/**
	 * Unused private constructor
	 */
	private WSAddressing() {
	}

	/**
	 * Type-safe enum representing WS-Addressing SOAP Headers
	 */
	public static class Header {
		private final String name;
		private final Class type;
		public static final Header TO = new Header("To", AttributedURI.class);
		public static final Header ACTION = new Header("Action", AttributedURI.class);
		public static final Header MESSAGEID = new Header("MessageID", AttributedURI.class);
		public static final Header RELATESTO = new Header("RelatesTo", AttributedURI.class);
		public static final Header FROM = new Header("From", EndpointReference.class);
		public static final Header REPLYTO = new Header("ReplyTo", EndpointReference.class);
		public static final Header FAULTTO = new Header("FaultTo", EndpointReference.class);

		/**
		 * @param name
		 * @param type
		 */
		private Header(final String name, final Class type) {
			this.name = name;
			this.type = type;
		}

		/**
		 * @return
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return
		 */
		public Class getType() {
			return type;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return name;
		}

		/**
		 * @param name
		 * @return
		 * @throws IllegalAccessException
		 */
		public static Header fromString(String name) throws IllegalAccessException {
			Field[] fields = Header.class.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (field.getType() == Header.class) {
					Header header = (Header) field.get(null);
					if (header.name.equals(name))
						return header;
				}
			}
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Factory class providing methods to create WSAddressing types av SOAP Headers as defined for the SCA runtime
	 */
	public static class Factory {

		private BOFactory boFactory;
		private final String wsaNamespaceURI;
		private final String wsaNamespacePrefix;

		/**
		 * 
		 */
		public Factory() {
			this(DEFAULT_NAMESPACEURI, DEFAULT_NAMESPACEPREFIX);
		}

		/**
		 * @param wsaURI
		 * @param namespacePrefix
		 */
		public Factory(URI wsaURI, String namespacePrefix) {
			this(wsaURI, namespacePrefix, (BOFactory) ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOFactory"));
		}

		/**
		 * @param wsaURI
		 * @param namespacePrefix
		 * @param boFactory
		 */
		public Factory(URI wsaURI, String namespacePrefix, BOFactory boFactory) {
			this.boFactory = boFactory;
			this.wsaNamespaceURI = wsaURI.toString();
			this.wsaNamespacePrefix = namespacePrefix;
		}

		/**
		 * @return
		 */
		public AttributedURI createAttributedURI() {
			return (AttributedURI) boFactory.create(wsaNamespaceURI, "AttributedURI");
		}

		/**
		 * @return
		 */
		public SOAPHeaderType createWSAHeader() {
			SOAPHeaderType wsaSH = ServiceMessageObjectFactory.eINSTANCE.createSOAPHeaderType();
			wsaSH.setNameSpace(wsaNamespaceURI);
			wsaSH.setPrefix(wsaNamespacePrefix);
			return wsaSH;
		}

		/**
		 * @param name
		 * @return
		 */
		public SOAPHeaderType createWSAHeader(Header name) {
			SOAPHeaderType wsaSH = createWSAHeader();
			wsaSH.setName(name.toString());
			return wsaSH;
		}

		/**
		 * @param name
		 * @param value
		 * @return
		 */
		public SOAPHeaderType createWSAAttributedURIHeader(Header name, URI value) {
			SOAPHeaderType wsaHeader = createWSAHeader(name);
			AttributedURI wsaType = createAttributedURI();
			wsaType.setValue(value.toString());
			wsaHeader.setValue(wsaType);
			return wsaHeader;
		}

		/**
		 * @param endpointURI
		 * @return
		 */
		public SOAPHeaderType createWSAToHeader(String endpointURI) {
			return createWSAAttributedURIHeader(Header.TO, URI.create(endpointURI));
		}

		/**
		 * @param actionURI
		 * @return
		 */
		public SOAPHeaderType createWSAActionHeader(String actionURI) {
			return createWSAAttributedURIHeader(Header.ACTION, URI.create(actionURI));
		}

		/**
		 * @param uuid
		 * @return
		 */
		public SOAPHeaderType createWSAMessageIDHeader(String uuid) {
			return createWSAAttributedURIHeader(Header.MESSAGEID, URI.create(uuid));
		}
	}
}
