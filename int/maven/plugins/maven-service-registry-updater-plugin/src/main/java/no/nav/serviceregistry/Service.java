package no.nav.serviceregistry;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.namespace.QName;

public class Service {

	private QName name;
	private String application;
	// List of service versions accessed by binding qualified name
	private Set<ServiceVersion> serviceVersions = new HashSet<ServiceVersion>();

	/* For JAXB */
	@SuppressWarnings("unused")
	private Service() {}
	
	public Service(QName serviceName, String application) {
		this.name = serviceName;
		this.application = application;
		System.out.println("Vi lager service! Navn " + this.name + ", app " + this.application);
	}

//	@XmlElement(name="serviceVersion")
//	public Collection<ServiceVersion> getServiceVersions() {
//		return serviceVersions.values();
//	}

	@XmlElement(name="serviceVersion")
	public Collection<ServiceVersion> getServiceVersions() {
		return serviceVersions;
	}

//	@XmlElement
	public void setServiceVersions(Collection<ServiceVersion> serviceVersion) {
		System.out.println("Heidu");
	}

//	public Map<QName, ServiceVersion> getServiceVersions() {
//		return serviceVersions;
//	}
//
	@XmlAttribute
	public QName getName() {
		return name;
	}

	public void setName(QName name) {
		this.name = name;
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
		for (ServiceVersion serviceVersion : serviceVersions) {
			output.append(serviceVersion + ",");
		}
		output.append("}");
		return output.toString();
	}

	public ServiceVersion addServiceVersion(ServiceVersion newServiceVersion) {
		QName bindingName = newServiceVersion.getBindingName();
		serviceVersions.add(newServiceVersion);
//		if (!serviceVersions.containsKey(bindingName)) {
//			serviceVersions.put(bindingName, newServiceVersion);
//		}
		for (ServiceVersion serviceVersion : serviceVersions) {
			if (serviceVersion.getBindingName().toString().equalsIgnoreCase(bindingName.toString())) {
				return serviceVersion;
			}
		}
		return null;
	}	
}
