import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

public class SOAPFaultExceptionFactory {
	static SOAPFaultException createSoapFaultException(RuntimeException e) {
		try {
			SOAPFactory soapFactory = SOAPFactory.newInstance();
			SOAPFault fault = soapFactory.createFault(e.getMessage(),new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
			fault.setFaultActor("faultActor");
			Detail faultDetail = fault.addDetail();
			Name infoName = soapFactory.createName("info");
			SOAPElement infoElement = soapFactory.createElement(infoName);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			infoElement.addTextNode(sw.toString());
			faultDetail.addChildElement(infoElement);
			return new SOAPFaultException(fault);
		} catch (SOAPException se) {
			throw new RuntimeException(se);
		}
	}
}
