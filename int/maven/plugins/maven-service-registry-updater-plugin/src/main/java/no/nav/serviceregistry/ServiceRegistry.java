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

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@XmlRootElement
public class ServiceRegistry {
	
	private Set<Service> services = new HashSet<Service>();

	@XmlElement(name="service")
	public Set<Service> getServices() {
		return services;
	}

	public void setServices(Set<Service> services) {
		this.services = services;
	}
	
	public void replaceApplicationBlock(String endpoint, File wsdlDir, String application){
		this.removeServices(application);
		//for alle tjenester i wsdl-dir:
		for (File f: wsdlDir.listFiles()) {
			if (f.isDirectory()) {
				for (File f2: f.listFiles()) {
					if (f2.isFile()) {
						this.addServiceInstance(application, endpoint, f2.getPath());
					}
					
				}
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
		services.removeAll(toBeRemoved);
	}

	public void addServiceInstance(String application, String endpoint, String pathToWsdl) {
		System.out.println("HER, path: " + pathToWsdl);
		Definition definition = DPWsdlUtils.getDefinition(pathToWsdl);
		//url m� fikses!!
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
		//valider output!
		marshaller.marshal(this, out);

	}
	
	public ServiceRegistry readServiceRegistry(String serviceRegistryFile) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(getClass());
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Schema schema;
		try {
			schema = this.getSchemaFromClassPath("service-registry.xsd");
		} catch (IOException e) {
			throw new RuntimeException(e); 
		}
		System.out.println("schema " + schema.toString());
		unmarshaller.setSchema(schema);
		ValidationEventCollector vec = new ValidationEventCollector();
		unmarshaller.setEventHandler(vec);
		
		System.out.println("N� skal vi unmarshalle!!");
		ServiceRegistry sr = (ServiceRegistry) unmarshaller.unmarshal(new FileInputStream(serviceRegistryFile));
		return sr;
	}
	
	private Schema getSchemaFromClassPath(String filename) throws IOException {

		File outFile = new File("/" + filename);

		InputStream in = getClass().getResourceAsStream("/" + filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
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