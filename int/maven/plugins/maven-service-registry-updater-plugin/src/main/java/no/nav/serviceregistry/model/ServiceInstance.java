package no.nav.serviceregistry.model;

import java.net.URL;
import javax.xml.bind.annotation.XmlAttribute;

public class ServiceInstance {

	private URL endpoint;
	
	/* For JAXB */
	@SuppressWarnings("unused")
	private ServiceInstance() {}

	public ServiceInstance(URL endpoint) {
		this.endpoint = endpoint;
	}
	
	@XmlAttribute
	public URL getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(URL endpoint) {
		this.endpoint = endpoint;
	}
	
	@Override
	public int hashCode() {
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
