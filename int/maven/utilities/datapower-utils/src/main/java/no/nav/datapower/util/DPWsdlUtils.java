package no.nav.datapower.util;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

public class DPWsdlUtils {
	public static WSDLFactory getWSDLFactory() {
		WSDLFactory factory = null;
		try {
			factory = WSDLFactory.newInstance();
		} catch (WSDLException e) {
			throw new IllegalStateException(e);
		}
		return factory;
	}

	public static WSDLReader getWSDLReader(boolean verbose) {
		WSDLReader reader = getWSDLFactory().newWSDLReader();
		reader.setFeature("javax.wsdl.verbose", verbose);
		return reader;
	}

	public static Definition getDefinition(String wsdlFile) {
		Definition def = null;
		try {
			def = getWSDLReader(false).readWSDL(wsdlFile);
		} catch (WSDLException e) {
			throw new IllegalArgumentException(e);
		}
		return def;
	}
}
