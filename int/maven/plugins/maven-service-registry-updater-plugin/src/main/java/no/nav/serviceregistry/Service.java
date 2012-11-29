package no.nav.serviceregistry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;

public class Service {

	@XmlAttribute
	private QName name;
	@XmlAttribute
	private String application;
	// List of service versions accessed by binding qualified name
	private Map<QName, ServiceVersion> serviceVersions = new LinkedHashMap<QName, ServiceVersion>();

	/* For JAXB */
	@SuppressWarnings("unused")
	private Service() {}
	
	public Service(QName serviceName, String application) {
		this.name = serviceName;
		this.application = application;
	}

	@XmlElement
	public Collection<ServiceVersion> getServiceVersion() {
		return serviceVersions.values();
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder(name.toString() + " {");
		output.append(application + ",");
		for (ServiceVersion serviceVersion : serviceVersions.values()) {
			output.append(serviceVersion + ",");
		}
		output.append("}");
		return output.toString();
	}

	public ServiceVersion addServiceVersion(ServiceVersion newServiceVersion) {
		QName bindingName = newServiceVersion.getBindingName();
		if (!serviceVersions.containsKey(bindingName)) {
			serviceVersions.put(bindingName, newServiceVersion);
		}
		return serviceVersions.get(bindingName);
	}

	public String getApplication() {
		return this.application;
	}
	
}
