import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

public class SOAPFaultExceptionFactory {
	static SOAPFaultException createSoapFaultException(RuntimeException e) {
		try {
			SOAPFactory soapFactory = SOAPFactory.newInstance();
			Detail faultDetail = soapFactory.createDetail();
			Name infoName = soapFactory.createName("info");
			SOAPElement infoElement = soapFactory.createElement(infoName);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			infoElement.addTextNode(sw.toString());
			faultDetail.addChildElement(infoElement);
			return new SOAPFaultException(new QName("Client"), e.getMessage(),
					"faultActor", faultDetail);
		} catch (SOAPException se) {
			throw new RuntimeException(se);
		}
	}
}
