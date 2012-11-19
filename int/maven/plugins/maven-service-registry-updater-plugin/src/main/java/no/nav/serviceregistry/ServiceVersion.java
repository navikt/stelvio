package no.nav.serviceregistry;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;

public class ServiceVersion {

	private QName bindingName;
	@XmlElement(name="operation", required=true)
	private List<ServiceOperation> operations;
	@XmlElement(name="serviceInstance")
	private Set<ServiceInstance> serviceInstances = new LinkedHashSet<ServiceInstance>();

	/* For JAXB */
	@SuppressWarnings("unused")
	private ServiceVersion() {}

	public ServiceVersion(QName bindingName, List<ServiceOperation> operations) {
		this.bindingName = bindingName;
		this.operations = operations;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder(bindingName + " {");
		for (ServiceOperation operation : operations) {
			output.append(operation + ",");
		}
		output.append("}");
		return output.toString();
	}

	public void addServiceInstance(ServiceInstance newServiceInstance) {
		serviceInstances.add(newServiceInstance);
	}

	@XmlAttribute
	public QName getBindingName() {
		return bindingName;
	}
}
