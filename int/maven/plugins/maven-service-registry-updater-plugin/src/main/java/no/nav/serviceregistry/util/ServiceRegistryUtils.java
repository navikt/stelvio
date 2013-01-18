package no.nav.serviceregistry.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import no.nav.serviceregistry.exception.ServiceRegistryException;
import no.nav.serviceregistry.model.ServiceRegistry;

import org.xml.sax.SAXException;

public class ServiceRegistryUtils {
	private static final String SCHEMA_FILE = "service-registry.xsd";

	private static Class getClazz(){
		return ServiceRegistry.class;
	}
	
	public static ServiceRegistry readServiceRegistryFromFile(String serviceRegistryFile) {

		try {
			JAXBContext context = JAXBContext.newInstance(getClazz());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Schema schema;
			try {
				schema = getSchemaFromClassPath();
			} catch (IOException e) {
				throw new ServiceRegistryException(e); 
			}
			if (schema != null) {
				unmarshaller.setSchema(schema);
			} else {
				throw new ServiceRegistryException("Can not validate service-registry.xml because schema is not set!");
			}
			ValidationEventCollector vec = new ValidationEventCollector();
			unmarshaller.setEventHandler(vec);

			return (ServiceRegistry) unmarshaller.unmarshal(new FileInputStream(serviceRegistryFile));
		} catch (JAXBException e) {
			throw new ServiceRegistryException(e.toString());
		} catch (FileNotFoundException e) {
			throw new ServiceRegistryException(e);
		}

	}

	public static void writeToFile(ServiceRegistry serviceRegistry, File file) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(getClazz());
		Marshaller marshaller = context.createMarshaller();
		OutputStream out = new FileOutputStream(file);

		Schema schema = null;
		try {
			schema = getSchemaFromClassPath();
		} catch (IOException e) {
			throw new ServiceRegistryException(e); 
		}
		if (schema != null) {
			marshaller.setSchema(schema);			
		} else {
			throw new ServiceRegistryException("Can not validate new service-registry.xml because schema is not set!");
		}
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		marshaller.marshal(serviceRegistry, out);
	}

	private static Schema getSchemaFromClassPath() throws IOException {
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			URL resource = getClazz().getClassLoader().getResource(SCHEMA_FILE);
			return sf.newSchema(resource);
		} catch (SAXException e) {
			throw new ServiceRegistryException("SAXException", e);
		}

	}
}
