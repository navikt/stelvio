package no.stelvio.common.security.ws;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The LTPASecurityHandler class extends the GenericHandler class to support LTPA Security.
 * 
 * @author $Author$
 * @version $Id$
 */
public class LTPASecurityHandler extends GenericHandler {

	private static final Log LOG = LogFactory.getLog(LTPASecurityHandler.class);
	
	/**
	 * Username configuration string.
	 */
	public static final String USERNAME_CONFIG_STRING = "userName";

	/**
	 * password configuration string.
	 */
	public static final String PASSWORD_CONFIG_STRING = "password";

	private HandlerInfo info = null;
	private String serviceUsername = null;
	private String servicePassword = null;

	/**
	 * {@inheritDoc}
	 */
	public QName[] getHeaders() {
		return info.getHeaders();
	}

	/**
	 * {@inheritDoc}
	 */
	public void init(HandlerInfo arg) {
		info = arg;
		// System.out.println("LTPASecurityhandler INIT");
		if (arg.getHandlerConfig().get(USERNAME_CONFIG_STRING) != null) {
			serviceUsername = (String) arg.getHandlerConfig().get(USERNAME_CONFIG_STRING);
			// System.out.println("LTPASecurityhandler using username "+serviceUsername);
			servicePassword = (String) arg.getHandlerConfig().get(PASSWORD_CONFIG_STRING);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Initialize LTPASecurityhandler username and password: " + serviceUsername);
			}
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Could not initialize LTPASecurityhandler username and password");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean handleRequest(MessageContext context) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("LTPASecurityhandler request with username and password: " + serviceUsername);
			}
			SOAPMessageContext smc = (SOAPMessageContext) context;
			SOAPEnvelope se = smc.getMessage().getSOAPPart().getEnvelope();
			SOAPHeader sh = se.getHeader();
			SOAPElement ltpaHeader;
			if (serviceUsername != null) {
				ltpaHeader = SecurityHeader.createLTPAHeader(serviceUsername, servicePassword);
			} else {
				ltpaHeader = SecurityHeader.createLTPAHeader();
			}
			sh.addChildElement(ltpaHeader);

		} catch (Exception x) {
			throw new RuntimeException("Error while adding LTPA header " + x.getMessage(), x);
		}
		return true;
	}

}
