package no.stelvio.consumer;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;

public class LTPASecurityHandler extends GenericHandler {

	protected HandlerInfo info = null;

	/* (non-Javadoc)
	 * @see javax.xml.rpc.handler.Handler#getHeaders()
	 */
	public QName[] getHeaders() {
		return info.getHeaders();
	}
	public void init(HandlerInfo arg) {
		info = arg;
	}

	public boolean handleRequest(MessageContext context) {
		try {
			  SOAPMessageContext smc = (SOAPMessageContext)context;
			  SOAPEnvelope se = smc.getMessage().getSOAPPart().getEnvelope();
			  SOAPHeader sh = se.getHeader();
			  sh.addChildElement(SecurityHeader.createLTPAHeader("srvpensjon","Test1234"));
			  
		}catch (Exception x) {
			// insert error handling here
			x.printStackTrace();
		}
		return true;
	}
	
	
	
	@Override
	public boolean handleFault(MessageContext arg0) {
		// TODO Auto-generated method stub
		return super.handleFault(arg0);
	}
	@Override
	public boolean handleResponse(MessageContext arg0) {
		// TODO Auto-generated method stub
		return super.handleResponse(arg0);
	}
	

}
