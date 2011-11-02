package no.stelvio.ibm.websphere.esb;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.factory.WSDLFactory;
import javax.xml.namespace.QName;

/**
 * Editor framework for modifying WSDL files. Supports:
 * -Setting SOAP action
 * -Modifying endpoint URL for web services with context root (WebSphere)
 * 
 * Note: this class could have been made more generic by removing dependency on WebSphere specific logic
 * consider doing this refactoring job
 *  
 * @author Oystein Gisnas <test@example.com>
 *
 */
public class WSDLEditor {

	private Definition definition;

	public WSDLEditor(Definition def) {
		definition = def;
	}

	@SuppressWarnings("unchecked")
	public void modifySOAPAddresses(String versionedModuleName) throws WSDLException {
		Collection<Service> services = definition.getServices().values();
		for (Service service : services) {
			Collection<Port> ports = service.getPorts().values();
			for (Port port : ports) {
				for (ExtensibilityElement extensibilityElement : (List<ExtensibilityElement>) port.getExtensibilityElements()) {
					if (extensibilityElement instanceof SOAPAddress) {
						SOAPAddress soapAddress = (SOAPAddress) extensibilityElement;
						soapAddress.setLocationURI(createVersionedEndpointAddress(soapAddress.getLocationURI(), versionedModuleName));
					}
				}
			}
		}
	
	}

	private String createVersionedEndpointAddress(String locationURI,
			String versionedModuleName) throws WSDLException {
		URI oldURI;
		try {
			oldURI = new URI(locationURI);
		} catch (URISyntaxException e) {
			throw new WSDLException(WSDLException.OTHER_ERROR, "Invalid URI", e);
		}
		String path = oldURI.getPath();
		String[] pathFragments = path.split("/");
		if (pathFragments.length < 2) {
			throw new WSDLException(WSDLException.OTHER_ERROR, "No context root found in locationURI '" + locationURI + "'");
		}
		pathFragments[1] = versionedModuleName + "Web";
		StringBuilder newPath = new StringBuilder();
		for (String pathFragment : pathFragments) {
			if (pathFragment.length() > 0) {
				newPath.append("/");
				newPath.append(pathFragment);
			}
		}
		try {
			URI newURI = new URI(oldURI.getScheme(), oldURI.getAuthority(), newPath.toString(), oldURI.getQuery(), oldURI.getFragment());
			return newURI.toString();
		} catch (URISyntaxException e) {
			throw new WSDLException(WSDLException.OTHER_ERROR, "Invalid URI", e);
		}
	}

	public void saveChanges() throws FileNotFoundException, WSDLException, URISyntaxException {
		WSDLFactory.newInstance().newWSDLWriter().writeWSDL(definition, new FileOutputStream(new File(URI.create(definition.getDocumentBaseURI().replace(" ", "%20")))));
	}
	
}
