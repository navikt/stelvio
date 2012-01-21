package no.stelvio.wsdl.addressing;

import java.io.PrintWriter;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.ExtensionSerializer;
import javax.xml.namespace.QName;

import com.ibm.wsdl.util.xml.DOMUtils;

public class UsingAddressingSerializer implements ExtensionSerializer {

	public void marshall(Class parentType, QName elementType,
			ExtensibilityElement extension, PrintWriter pw, Definition def,
			ExtensionRegistry extReg) throws WSDLException {
		if (extension != null) {
			String tagName = DOMUtils.getQualifiedValue(UsingAddressingImpl.NS_WSAW, UsingAddressingImpl.ELEMENT_NAME, def);
			pw.print("    <" + tagName);
			Boolean required = extension.getRequired();
			if (required != null) {
				DOMUtils.printQualifiedAttribute(com.ibm.wsdl.Constants.Q_ATTR_REQUIRED, required.toString(), def, pw);
			}
			pw.println("/>");
		}
	}

}
