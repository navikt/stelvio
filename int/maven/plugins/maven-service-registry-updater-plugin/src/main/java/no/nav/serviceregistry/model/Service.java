package no.nav.serviceregistry.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;

import no.nav.serviceregistry.model.ServiceVersion;

public class Service {

	private QName name;
	private String application;
	private Set<ServiceVersion> serviceVersions = new HashSet<ServiceVersion>();

	/* For JAXB */
	@SuppressWarnings("unused")
	private Service() {}
	
	public Service(QName serviceName, String application) {
		this.name = serviceName;
		this.application = application;
	}

	@XmlElement(name="serviceVersion")
	public Collection<ServiceVersion> getServiceVersions() {
		return serviceVersions;
	}

	public void setServiceVersions(Set<ServiceVersion> serviceVersion) {
		this.serviceVersions = serviceVersion;
	}

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
	
	@Override
	public int hashCode() {
		return name.hashCode() + application.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass())
			return false;
		Service other = (Service) obj;
		if (application.equals(other.application)) {
			return name.equals(other.name);
		}
		return false;
	}

	public ServiceVersion addServiceVersion(ServiceVersion newServiceVersion) {
		QName bindingName = newServiceVersion.getBindingName();
		serviceVersions.add(newServiceVersion);
		for (ServiceVersion serviceVersion : serviceVersions) {
			if (serviceVersion.getBindingName().toString().equalsIgnoreCase(bindingName.toString())) {
				return serviceVersion;
			}
		}
		return null;
	}	
}
