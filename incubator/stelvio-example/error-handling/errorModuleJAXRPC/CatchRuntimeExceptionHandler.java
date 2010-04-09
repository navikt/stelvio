import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class CatchRuntimeExceptionHandler extends GenericHandler {
	private Logger logger = LogManager.getLogManager().getLogger(
			getClass().getName());

	@Override
	public QName[] getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean handleFault(MessageContext context) {
		logger.severe("handleFault");
		return true;
	}

	@Override
	public boolean handleRequest(MessageContext context) {
		logger.severe("handleRequest");
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext context) {
		try {
			logger.severe("handleResponse");
			for (Iterator<String> it = context.getPropertyNames(); it.hasNext();) {
				String propertyName = it.next();
				logger.severe(propertyName + "="
						+ context.getProperty(propertyName));
			}
			logger.severe(context.getClass().toString());

			com.ibm.ws.sca.internal.webservice.jaxrpc.AbstractBaseHandler.MessageHolder msgHolder = (com.ibm.ws.sca.internal.webservice.jaxrpc.AbstractBaseHandler.MessageHolder) context
					.getProperty("SCA.MessageContext");
			logger.severe(msgHolder.getMessage().dumpMessage());

			SOAPFactory soapFactory = SOAPFactory.newInstance();

			SOAPMessageContext soapMessageContext = (SOAPMessageContext) context;

			SOAPBody body = soapMessageContext.getMessage().getSOAPBody();

			Source source = new DOMSource(body.extractContentAsDocument());
			StringWriter sw = new StringWriter();
			Result result = new StreamResult(sw);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(source, result);
			logger.severe(sw.toString());

			logger.severe(Boolean.toString(body.hasFault()));
			body.removeContents();
			// SOAPFault fault = body.addFault(new QName(
			// "http://schemas.xmlsoap.org/soap/envelope/", "Client"), t
			// .getMessage());

			SOAPFault fault = body.addFault(new QName(
					"http://www.w3.org/2005/08/addressing/",
					"ActionNotSupported"), "Error Message");
			logger.severe(fault.toString());

			fault.setFaultActor("faultActor");
			Detail faultDetail = fault.addDetail();
			Name infoName = soapFactory.createName("info");
			SOAPElement infoElement = soapFactory.createElement(infoName);
			infoElement.addTextNode("Detail error text");
			faultDetail.addChildElement(infoElement);

			return true;
		} catch (SOAPException e) {
			throw new RuntimeException(e);
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}
}
