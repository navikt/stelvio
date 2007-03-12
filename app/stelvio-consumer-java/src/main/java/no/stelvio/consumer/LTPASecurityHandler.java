package no.stelvio.consumer;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;

public class LTPASecurityHandler extends GenericHandler {

	static final String userNameConfigString="userName";
	static final String passwordConfigString="password";
	
	protected HandlerInfo info = null;
	String serviceUsername=null;
	String servicePassword=null;

	/* (non-Javadoc)
	 * @see javax.xml.rpc.handler.Handler#getHeaders()
	 */
	public QName[] getHeaders() {
		return info.getHeaders();
	}
	public void init(HandlerInfo arg) {
		info = arg;
		//System.out.println("LTPASecurityhandler INIT");
		if (arg.getHandlerConfig().get(userNameConfigString)!=null){			
			serviceUsername=(String)arg.getHandlerConfig().get(userNameConfigString);
			//System.out.println("LTPASecurityhandler using username "+serviceUsername);
			servicePassword=(String)arg.getHandlerConfig().get(passwordConfigString);
		}
	}

	public boolean handleRequest(MessageContext context) {
		try {
			  SOAPMessageContext smc = (SOAPMessageContext)context;
			  SOAPEnvelope se = smc.getMessage().getSOAPPart().getEnvelope();
			  SOAPHeader sh = se.getHeader();
			  SOAPElement ltpaHeader;
			  if (serviceUsername!=null){
				  ltpaHeader=SecurityHeader.createLTPAHeader(serviceUsername, servicePassword);
			  }else{
				  ltpaHeader=SecurityHeader.createLTPAHeader();
			  }
			  sh.addChildElement(ltpaHeader);
			  
		}catch (Exception x) {
			throw new RuntimeException("Error while adding LTPA header "+x.getMessage(),x);
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
