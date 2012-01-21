package no.stelvio.wsdl.addressing;

import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.xml.namespace.QName;

public class UsingAddressingImpl extends UnknownExtensibilityElement {

	private static final long serialVersionUID = 1L;
	public static final String NS_WSAW = "http://www.w3.org/2006/05/addressing/wsdl";
	public static final String NS_PREFIX_WSAW = "wsaw";
	public static final String ELEMENT_NAME = "UsingAddressing";
	public static final QName Q_ELEMENT = new QName(NS_WSAW, ELEMENT_NAME);

	protected QName elementType = Q_ELEMENT;

}
