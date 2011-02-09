package no.stelvio.serviceregistry;

import javax.xml.bind.annotation.XmlAttribute;

public class ServiceOperation {

	@XmlAttribute
	private String name;
	@XmlAttribute
	private String soapAction;
	
	/* For JAXB */
	@SuppressWarnings("unused")
	private ServiceOperation() {}

	public ServiceOperation(String operationName, String soapActionURI) {
		name = operationName;
		// TODO Fix workaround for SOAPAction containing "-character
		soapAction = "\"" + soapActionURI + "\"";
	}

	@Override
	public String toString() {
		return name + "/" + soapAction;
	}
}
