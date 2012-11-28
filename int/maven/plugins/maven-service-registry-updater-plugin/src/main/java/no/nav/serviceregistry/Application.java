package no.nav.serviceregistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;

import no.nav.datapower.util.DPWsdlUtils;
import no.nav.serviceregistry.Service;

public class Application {
	private QName name;
	private String endpoint;
	private Map<QName, Service> services = new LinkedHashMap<QName, Service>();
	
	@XmlElement(name="service")
	public Collection<Service> getServices() {
		return services.values();
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void addServiceInstance(ServiceInstance endpoint) {
		Definition definition = DPWsdlUtils.getDefinition(endpoint.getWsdlAddress().toString());
		for (Iterator iterator = definition.getAllServices().values().iterator(); iterator.hasNext();) {
			javax.wsdl.Service service = (javax.wsdl.Service) iterator.next();
			for (Iterator iterator2 = service.getPorts().values().iterator(); iterator2
					.hasNext();) {
				Port port = (Port) iterator2.next();
				Binding binding = port.getBinding();
				List<ServiceOperation> operations = new ArrayList<ServiceOperation>();
				for (Object op : binding.getBindingOperations()) {
					BindingOperation operation = (BindingOperation) op;
					for (Object obj : operation.getExtensibilityElements()) {
						if (obj instanceof SOAPOperation) {
							SOAPOperation soapOperation = (SOAPOperation) obj;
							String soapActionURI = soapOperation.getSoapActionURI(); 
							if (soapActionURI != null && !soapActionURI.isEmpty()) {
								operations.add(new ServiceOperation(operation.getName(), soapActionURI));
							}
						}
					}
				}
				if (operations.size() > 0) {
					addServiceInstance(service.getQName(), binding.getQName(), endpoint, operations);
				}
			}
		}
	}

	public void addServiceInstance(QName serviceName, QName bindingName, ServiceInstance serviceInstance, List<ServiceOperation> operations) {
		ServiceVersion serviceVersion = new ServiceVersion(bindingName, operations);
		Service service = services.get(serviceName);
		if (service == null) {
			service = new Service(serviceName, null);
			services.put(serviceName, service);
		}
		serviceVersion = service.addServiceVersion(serviceVersion);
		serviceVersion.addServiceInstance(serviceInstance);
	}
	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder("Service Registry(size=" + services.size() + ")=[\n");
		for (Service service : services.values()) {
			output.append(service + ",\n");
		}
		output.append("]");
		return output.toString();
	}
}
