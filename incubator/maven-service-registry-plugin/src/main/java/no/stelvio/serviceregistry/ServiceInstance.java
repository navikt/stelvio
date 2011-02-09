package no.stelvio.serviceregistry;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ServiceInstance {

	private URL endpoint;
	private URL wsdlAddress;
	@XmlElement(name="environment")
	private Set<String> environments;

	/* For JAXB */
	@SuppressWarnings("unused")
	private ServiceInstance() {}

	public ServiceInstance(String environment, String host, String port, String moduleName, String exportName) {
		try {
			endpoint = new URL("http://" + host + ":" + port + "/" + moduleName + "Web/sca/" + exportName);
			wsdlAddress = new URL(endpoint.toString() + "?wsdl");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		try {
			// Workaround for broken relative paths in WSDL when WSDL location is resolved with HTTP 302
			// Set WSDL location to the redirect target
			HttpURLConnection connection = (HttpURLConnection) wsdlAddress.openConnection();
			connection.setInstanceFollowRedirects(false);
			if (connection.getResponseCode() == 302) {
				wsdlAddress = new URL(connection.getHeaderField("Location"));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		environments = new LinkedHashSet<String>();
		environments.add(environment);
	}

	@XmlAttribute
	public URL getEndpoint() {
		return endpoint;
	}

	@XmlAttribute
	public URL getWsdlAddress() {
		return wsdlAddress;
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
