import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class CatchRuntimeExceptionHandler implements
		SOAPHandler<SOAPMessageContext> {
	private Logger logger = LogManager.getLogManager().getLogger(
			getClass().getName());

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	@Override
	public void close(javax.xml.ws.handler.MessageContext context) {
		logger.severe("close");
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		try {
			logger.severe("handleFault");

			Throwable t = (Throwable) context
					.get("jaxws.outbound.response.webmethod.exception");

			SOAPFactory soapFactory = SOAPFactory.newInstance();

			SOAPBody body = context.getMessage().getSOAPBody();
			logger.severe(Boolean.toString(body.hasFault()));
			body.removeContents();
//			SOAPFault fault = body.addFault(new QName(
//					"http://schemas.xmlsoap.org/soap/envelope/", "Client"), t
//					.getMessage());
			
			SOAPFault fault = body.addFault(new QName("http://www.w3.org/2005/08/addressing/", "ActionNotSupported"), t.getMessage());
			logger.severe(fault.toString());

			fault.setFaultActor("faultActor");
			Detail faultDetail = fault.addDetail();
			Name infoName = soapFactory.createName("info");
			SOAPElement infoElement = soapFactory.createElement(infoName);
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			infoElement.addTextNode(sw.toString());
			faultDetail.addChildElement(infoElement);

			return true;
		} catch (SOAPException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		logger.severe("handleMessage");
		return true;
	}
}
