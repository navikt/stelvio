package no.nav.serviceregistry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import no.nav.datapower.util.DPWsdlUtils;
import no.nav.serviceregistry.exception.BadUrlException;
import no.nav.serviceregistry.util.ExposedService;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@XmlRootElement
public class ServiceRegistry {
	
	private Set<Service> services = new HashSet<Service>();
	private String schemaFile = "service-registry.xsd";
	private static File pathToResourceDir;

	public ServiceRegistry() {
	}
	
	public ServiceRegistry(File pathToResourceDir) {
		ServiceRegistry.pathToResourceDir = pathToResourceDir;
	}

	@XmlElement(name="service")
	public Set<Service> getServices() {
		return services;
	}

	public void setServices(Set<Service> services) {
		this.services = services;
	}
	
	public void replaceApplicationBlock(String application, String hostname, Collection<ExposedService> exposedServices){
		this.removeServices(application);
		
		for (ExposedService exposedService : exposedServices) {
			//TODO: protokoll og port vil antageligvis komme fra envconfig, men er ikke klart
			String urlString = "https://" + hostname + ":443/" + exposedService.getPath();
			URL endpoint;
			try {
				endpoint = new URL(urlString);
			} catch (MalformedURLException e) {
				throw new BadUrlException("URL er ikke på riktig format: "+urlString);
			}

			File wsdlDir = exposedService.getWsdlDir();
			String[] extensions = {"wsdl"};
			Collection<File> wsdlFiles = FileUtils.listFiles(wsdlDir, extensions, true);
			
			for (File file : wsdlFiles) {
				this.addServiceInstance(application, endpoint, file.getPath());
			}
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
		this.services.removeAll(toBeRemoved);
	}

	public void addServiceInstance(String application, URL serviceEndpoint, String pathToWsdl) {
		Definition definition = DPWsdlUtils.getDefinition(pathToWsdl);
		URL wsdlAddress;
		try {
			//TODO: vil wsdlAddress alltid se slik ut?
			wsdlAddress = new URL(serviceEndpoint.toString() + "?wsdl");
		} catch (MalformedURLException e) {
			throw new BadUrlException("The endpoint provided is not valid (check input from envconfig)!", e);
		}
		
		ServiceInstance instance = new ServiceInstance(serviceEndpoint, wsdlAddress);

		for (Iterator iterator = definition.getAllServices().values().iterator(); iterator.hasNext();) {
			javax.wsdl.Service service = (javax.wsdl.Service) iterator.next();
			for (Iterator iterator2 = service.getPorts().values().iterator(); iterator2.hasNext();) {
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
								String soapAction = definition.getTargetNamespace() + binding.getPortType().getQName().getLocalPart() + "/" + operation.getName();
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
		Service service = null;
		for (Service service1 : services) {
			if (service1.getName().toString().equalsIgnoreCase(serviceName.toString()) && service1.getApplication().equalsIgnoreCase(applicationName)) {
				service = service1;
			}
		}
		if (service == null) {
			service = new Service(serviceName, applicationName);
			services.add(service);
		}
		serviceVersion = service.addServiceVersion(serviceVersion);
		serviceVersion.setServiceInstance(serviceInstance);
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
		
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		File schemaXsd = new File(pathToResourceDir, "/" + schemaFile);
		Schema schema = null;
		try {
			schema = sf.newSchema(schemaXsd);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
		if (schema != null) {
			marshaller.setSchema(schema);			
		} else {
			throw new RuntimeException("Can not validate new service-registry.xml because schema is not set!");
		}
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		marshaller.marshal(this, out);
	}
	
	public void readServiceRegistry(String serviceRegistryFile) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(getClass());
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Schema schema;
		try {
			schema = this.getSchemaFromClassPath(schemaFile);
		} catch (IOException e) {
			throw new RuntimeException(e); 
		}
		if (schema != null) {
			unmarshaller.setSchema(schema);
		} else {
			throw new RuntimeException("Can not validate service-registry.xml because schema is not set!");
		}
		ValidationEventCollector vec = new ValidationEventCollector();
		unmarshaller.setEventHandler(vec);
		
		ServiceRegistry sr = (ServiceRegistry) unmarshaller.unmarshal(new FileInputStream(serviceRegistryFile));
		this.services = sr.getServices();
	}
	
	private Schema getSchemaFromClassPath(String filename) throws IOException {
		InputStream in = getClass().getResourceAsStream("/" + filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		File outFile = new File(pathToResourceDir, "/" + filename);
		OutputStream out = new FileOutputStream(outFile);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
		
		String line;
		while ((line = br.readLine()) != null) {
			bw.write(line);
			bw.newLine();
		}

		br.close();
		bw.close();

		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		try {
			schema = sf.newSchema(outFile);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
		
		return schema;
	}
}