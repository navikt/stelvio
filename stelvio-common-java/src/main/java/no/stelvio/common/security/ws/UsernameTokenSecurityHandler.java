package no.stelvio.common.security.ws;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;

/**
 * The UsernameSecurityHandler class extends the GenericHandler class to support Username token Security.
 * 
 * @author $Author$
 * @version $Id$
 */
public class UsernameTokenSecurityHandler extends GenericHandler {

	/** Username configuration string. */
	public static final String USERNAME_CONFIG_STRING = "userName";

	/** Password configuration string. */
	public static final String PASSWORD_CONFIG_STRING = "password";

	private HandlerInfo info = null;
	private String serviceUsername = null;
	private String servicePassword = null;

	@Override
	public QName[] getHeaders() {
		return info.getHeaders();
	}

	@Override
	public void init(HandlerInfo arg) {
		info = arg;
		System.out.println("UsernameSecurityHandler INIT");
		if (arg.getHandlerConfig().get(USERNAME_CONFIG_STRING) != null) {
			serviceUsername = (String) arg.getHandlerConfig().get(USERNAME_CONFIG_STRING);
			System.out.println("UsernameSecurityHandler using username " + serviceUsername);
			servicePassword = (String) arg.getHandlerConfig().get(PASSWORD_CONFIG_STRING);
		}
	}

	@Override
	public boolean handleRequest(MessageContext context) {
		try {
			SOAPMessageContext smc = (SOAPMessageContext) context;
			SOAPEnvelope se = smc.getMessage().getSOAPPart().getEnvelope();
			SOAPHeader sh = se.getHeader();
			SOAPElement usernameHeader = SecurityHeader.createBasicAuth(serviceUsername, servicePassword);
			if (usernameHeader != null) {
				sh.addChildElement(usernameHeader);
			}
		} catch (Exception x) {
			throw new RuntimeException("Error while adding Username Token header " + x.getMessage(), x);
		}
		return true;
	}
}