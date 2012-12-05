package no.nav.serviceregistry;

import javax.xml.bind.annotation.XmlAttribute;

public class ServiceOperation {

	private String action;
	private String name;
	
	/* For JAXB */
	@SuppressWarnings("unused")
	private ServiceOperation() {}

	public ServiceOperation(String operationName, String soapActionURI) {
		name = operationName;
		action = soapActionURI;
		System.out.println("OPERATION!!");
	}
	
	@XmlAttribute
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name + "/" + action;
	}
}
