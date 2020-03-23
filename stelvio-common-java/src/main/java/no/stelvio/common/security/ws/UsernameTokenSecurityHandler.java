package no.stelvio.common.security.ws;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The UsernameSecurityHandler class extends the SOAPHandler class to support Username token Security.
 */
public class UsernameTokenSecurityHandler implements SOAPHandler<SOAPMessageContext> {
	private static final Log LOG = LogFactory.getLog(UsernameTokenSecurityHandler.class);

	private static final Set<QName> PROCESSED_HEADERS_QNAME = Collections.unmodifiableSet(
			Collections.singleton(new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security")));

	private final String serviceUsername;
	private final String servicePassword;

	public UsernameTokenSecurityHandler(String serviceUsername, String servicePassword) {
		this.serviceUsername = serviceUsername;
		this.servicePassword = servicePassword;
	}

	@Override
	public Set<QName> getHeaders() {
		return PROCESSED_HEADERS_QNAME;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		try {
			SOAPEnvelope se = smc.getMessage().getSOAPPart().getEnvelope();
			SOAPHeader sh = se.getHeader();
			SOAPElement usernameHeader = SecurityHeader.createBasicAuth(serviceUsername, servicePassword);
			if (usernameHeader != null) {
				sh.addChildElement(usernameHeader);
			}
		} catch (SOAPException x) {
			LOG.error("Error creating basic auth SOAP header");
			throw new RuntimeException("Error while adding Username Token header " + x.getMessage(), x);
		}
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext smc) {
		return false;
	}

	@Override
	public void close(MessageContext context) {
		// NOOP
	}
}