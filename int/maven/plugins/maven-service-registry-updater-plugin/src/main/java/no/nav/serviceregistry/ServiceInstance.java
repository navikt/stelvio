package no.nav.serviceregistry;

import java.net.URL;

import javax.xml.bind.annotation.XmlAttribute;

public class ServiceInstance {

	private URL endpoint;
	private URL wsdlAddress;
	
	/* For JAXB */
	@SuppressWarnings("unused")
	private ServiceInstance() {}

	public ServiceInstance(URL endpoint, URL wsdlAddress) {
		this.endpoint = endpoint;
		this.wsdlAddress = wsdlAddress;
		
		System.out.println("Endpoint: " + this.endpoint + ", wsdladdress: " + this.wsdlAddress);
	}
	
	@XmlAttribute
	public URL getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(URL endpoint) {
		this.endpoint = endpoint;
	}

	@XmlAttribute
	public URL getWsdlAddress() {
		return wsdlAddress;
	}

	public void setWsdlAddress(URL wsdlAddress) {
		this.wsdlAddress = wsdlAddress;
	}

	@Override
	public int hashCode() {
		System.out.println("endpoint: " + endpoint);
		return endpoint.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass())
			return false;
		ServiceInstance other = (ServiceInstance) obj;
		return endpoint.equals(other.endpoint);
	}

}
