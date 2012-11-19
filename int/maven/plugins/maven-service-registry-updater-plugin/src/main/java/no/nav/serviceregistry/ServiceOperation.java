package no.nav.serviceregistry;

import javax.xml.bind.annotation.XmlAttribute;

public class ServiceOperation {

	@XmlAttribute
	private String name;
	@XmlAttribute
	private String action;
	
	/* For JAXB */
	@SuppressWarnings("unused")
	private ServiceOperation() {}

	public ServiceOperation(String operationName, String soapActionURI) {
		name = operationName;
		action = soapActionURI;
	}

	@Override
	public String toString() {
		return name + "/" + action;
	}
}
