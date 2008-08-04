package no.nav.datapower.config;

import java.io.File;
import java.net.URI;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.xml.namespace.QName;

import no.nav.datapower.util.DPWsdlUtils;

public class WSDLFile {

	private Definition definition;
	private String fileName;
	private URI location;
	private Binding binding;
	private PortType portType;
	private Service service;
	private Port port;
	private SOAPAddress soapAddress;

	public WSDLFile(File wsdlFile) {
		this.fileName = wsdlFile.getName();
		this.definition = DPWsdlUtils.getDefinition(wsdlFile.getPath());
		this.binding = (Binding) definition.getAllBindings().values().iterator().next();
		this.portType = (PortType) definition.getAllPortTypes().values().iterator().next();
		this.service = (Service) definition.getAllServices().values().iterator().next();		
		this.port = (Port) service.getPorts().values().iterator().next();
		this.soapAddress = (SOAPAddress) port.getExtensibilityElements().get(0);
		this.location = URI.create(soapAddress.getLocationURI());
	}

	
	
	public String getProxyName() {
//		QName portType = (QName) definition.getAllPortTypes().keySet().iterator().next();
//		return portType.getLocalPart();
		return portType.getQName().getLocalPart();
	}
	
	public String getPortBinding() {
		QName portBinding = new QName(binding.getQName().getNamespaceURI(), port.getName());
		return portBinding.toString();
	}
		
	public String getBinding() {
//		return definition.getAllBindings().keySet().iterator().next().toString();
		return binding.getQName().toString();
	}
	
	public String getPortType() {
//		return definition.getAllPortTypes().keySet().iterator().next().toString();
		return portType.getQName().toString();
	}
	
	public String getService() {
//		return definition.getAllServices().keySet().iterator().next().toString();
		return service.getQName().toString();
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getEndpointURI() {
		return location.getPath();
	}
}
