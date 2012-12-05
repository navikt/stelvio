package no.nav.serviceregistry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;

public class Service {

	private QName name;
	private String application;
	// List of service versions accessed by binding qualified name
	private Map<QName, ServiceVersion> serviceVersions = new LinkedHashMap<QName, ServiceVersion>();

	/* For JAXB */
	@SuppressWarnings("unused")
	private Service() {}
	
	public Service(QName serviceName, String application) {
		this.name = serviceName;
		this.application = application;
		System.out.println("Vi lager service! Navn " + this.name + ", app " + this.application);
	}

	@XmlElement
	public Collection<ServiceVersion> getServiceVersion() {
		return serviceVersions.values();
	}

	public void setServiceVersions(ServiceVersion newServiceVersion) {
		this.addServiceVersion(newServiceVersion);
	}
	
	@XmlAttribute
	public QName getName() {
		return name;
	}

	public void setName(QName name) {
		this.name = name;
	}

	public Map<QName, ServiceVersion> getServiceVersions() {
		return serviceVersions;
	}

	@XmlElement
	public String getApplication() {
		return this.application;
	}

	public void setApplication(String application) {
		this.application = application;
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
}
