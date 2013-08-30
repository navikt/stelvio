package no.nav.esso;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class UsernameTokenHandler implements SOAPHandler<SOAPMessageContext> {

	private String untUsername;
	private String untPassword;
	
	public UsernameTokenHandler(String untUserName, String untPassword) {
		this.untUsername = untUserName;
		this.untPassword = untPassword;
	}
	
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if(outbound) {
			try {
				SOAPElement wsunt = UsernameTokenHeader.createUsernameToken(untUsername, untPassword);
				context.getMessage().getSOAPPart().getEnvelope().getHeader().addChildElement(wsunt);
			} catch (SOAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;		
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	}
