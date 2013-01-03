package no.nav.serviceregistry;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;

public class ServiceVersion {

	private QName bindingName;
	private List<ServiceOperation> operations;
//	private Set<ServiceInstance> serviceInstances = new LinkedHashSet<ServiceInstance>();//skal kun ha en!!
	private ServiceInstance serviceInstance;

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

//	public void addServiceInstance(ServiceInstance newServiceInstance) {
//		serviceInstances.add(newServiceInstance);
//	}
	public void setServiceInstance(ServiceInstance serviceInstance) {
		this.serviceInstance = serviceInstance;
	}


	@XmlAttribute
	public QName getBindingName() {
		return bindingName;
	}
	
	public void setBindingName(QName bindingName) {
		this.bindingName = bindingName;
	}
	
	@XmlElement(name="operation", required=true)
	public List<ServiceOperation> getOperations() {
		return operations;
	}

	public void setOperations(List<ServiceOperation> operations) {
		this.operations = operations;
	}

	@Override
	public int hashCode() {
		return bindingName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass())
			return false;
		ServiceVersion other = (ServiceVersion) obj;
		return bindingName.equals(other.bindingName);
	}

//	@XmlElement(name="serviceInstance")
//	public Set<ServiceInstance> getServiceInstances() {
//		return serviceInstances;
//	}
	@XmlElement(name="serviceInstance")
	public ServiceInstance getServiceInstance() {
		return serviceInstance;
	}

//	public void setServiceInstances(Set<ServiceInstance> serviceInstances) {
//		this.serviceInstances = serviceInstances;
//	}
}
