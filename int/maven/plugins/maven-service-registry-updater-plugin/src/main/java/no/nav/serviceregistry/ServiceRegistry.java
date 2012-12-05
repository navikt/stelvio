package no.nav.serviceregistry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import no.nav.datapower.util.DPWsdlUtils;

import org.w3c.dom.Document;

@XmlRootElement
public class ServiceRegistry {

//	private Map<QName, Service> services = new LinkedHashMap<QName, Service>();
	private Set<Service> services = new HashSet<Service>();

	@XmlElement(name="service")
	public Set<Service> getServices() {
		return services;
	}

	public void setServices(Set<Service> services) {
		this.services = services;
	}
	
//	@XmlElement(name="service")
//	public Collection<Service> getServices() {
//		System.out.println("getservices i serviceregistry");
//		return services.values();
//	}
//	
//	public void setServices(Collection<Service> services) {
//		this.services = new LinkedHashMap<QName, Service>();
//		for (Service service : services) {
//			this.services.put(service.getName(), service);
//		}
//	}
	
	public void replaceApplicationBlock(String endpoint, File wsdlDir, String application){
		this.removeServices(application);
		
		for (File f: wsdlDir.listFiles()) {
			this.addServiceInstance(application, endpoint, f.getPath());
		}
		
		
	}

	private void removeServices(String application) {
		Collection<Service> services = this.getServices();
		Set<Service> toBeRemoved = new HashSet<Service>();
		for (Service service : services) {
			if (service.getApplication().equals(application)) {
				toBeRemoved.add(service);
			}
		}
		services.removeAll(toBeRemoved);
	}

	public void addServiceInstance(String application, String endpoint, String pathToWsdl) {
		System.out.println("HER");
		Definition definition = DPWsdlUtils.getDefinition(pathToWsdl);
		//url må fikses!!
		System.out.println("mitt endpoint " + endpoint);
		URL serviceEndpoint;
		URL wsdlAddress;
		try {
			serviceEndpoint = new URL(endpoint + "moduleName" + "Web/sca/" + "exportName");
			wsdlAddress = new URL(serviceEndpoint.toString() + "?wsdl");

		} catch (MalformedURLException e) {
			throw new RuntimeException("The endpoint provided is not valid (check input from envconfig), details: " + e);
		}
		ServiceInstance instance = new ServiceInstance(serviceEndpoint, wsdlAddress);

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
							} else {
								String soapAction = definition.getTargetNamespace() + "/" + port.getName() + "/" + operation.getName();
								operations.add(new ServiceOperation(operation.getName(), soapAction));
							}
						}
					}
				}
				if (operations.size() > 0) {
					addServiceInstance(service.getQName(), application, binding.getQName(), instance, operations);
				}
			}
		}
	}

	public void addServiceInstance(QName serviceName, String applicationName, QName bindingName, ServiceInstance serviceInstance, List<ServiceOperation> operations) {
		ServiceVersion serviceVersion = new ServiceVersion(bindingName, operations);
//		Service service = services.get(serviceName);
		Service service = null;
		for (Service service1 : services) {
			if (service1.getName().toString().equalsIgnoreCase(serviceName.toString())) {
				service = service1;
			}
		}
		if (service == null) {
			service = new Service(serviceName, applicationName);
			services.add(service);
		}
		serviceVersion = service.addServiceVersion(serviceVersion);
		serviceVersion.addServiceInstance(serviceInstance);
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder("Service Registry(size=" + services.size() + ")=[\n");
		for (Service service : services) {
			output.append(service + ",\n");
		}
		output.append("]");
		return output.toString();
	}

	/**
	 * Return an XML representation of the object and relations according to JAXB schema
	 * 
	 * @return XML document as a string
	 * @throws JAXBException
	 */
	public String toXml() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(getClass());
		Marshaller marshaller = context.createMarshaller();
		OutputStream out = new ByteArrayOutputStream();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		marshaller.marshal(this, out);

		return out.toString();
	}

	public Document toDocument() throws JAXBException, ParserConfigurationException {
		JAXBContext context = JAXBContext.newInstance(getClass());
		Marshaller marshaller = context.createMarshaller();

		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.newDocument();

		marshaller.marshal(this, doc);

		return doc;
	}
	
	public void writeToFile(File file) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(getClass());
		Marshaller marshaller = context.createMarshaller();
		OutputStream out = new FileOutputStream(file);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		marshaller.marshal(this, out);

	}
	
	//skal denne returnere et objekt eller jobber den på det eksisterende??
	public ServiceRegistry readServiceRegistry(String serviceRegistryFile) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(getClass());
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setSchema(null);//sett riktig xsd
		System.out.println("Nå skal vi unmarshalle!!");
		ServiceRegistry sr = (ServiceRegistry) unmarshaller.unmarshal(new FileInputStream(serviceRegistryFile));
		return sr;
	}




}