package no.nav.datapower.config;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.xml.namespace.QName;

import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPFileUtils;
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
	private String relativePath;
	private String frontsideURI;

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

	public WSDLFile(File wsdlFile, File root) {
		this(wsdlFile);
		this.relativePath = DPFileUtils.replaceSeparator(DPFileUtils.getRelativePath(wsdlFile, root), '/');
	}
	
	public String getRelativePath() {
		return relativePath;
	}
	
	public String getProxyName() {
		return portType.getQName().getLocalPart();
	}
	
	public String getPortBinding() {
		QName portBinding = new QName(binding.getQName().getNamespaceURI(), port.getName());
		return portBinding.toString();
	}
		
	public String getBinding() {
		return binding.getQName().toString();
	}
	
	public String getPortType() {
		return portType.getQName().toString();
	}
	
	public String getService() {
		return service.getQName().toString();
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getEndpointURI() {
		return location.getPath();
	}

	public String getFrontsideURI() {
		return frontsideURI;
	}
	
	public static List<WSDLFile> getWsdlFiles(File directory) throws IOException {
		List<WSDLFile> wsdlFiles = DPCollectionUtils.newArrayList();
		File wsdlDirectory = DPFileUtils.findDirectory(directory, "wsdl");
		for (File wsdlFile : wsdlDirectory.listFiles()) {
			if (wsdlFile.isFile() && wsdlFile.getName().endsWith(".wsdl")) {
				wsdlFiles.add(new WSDLFile(wsdlFile));
			}
		}
		return wsdlFiles;
	}
	
	public static WSDLFile createMappedUri(File wsdl, File root, String endpointPrefix) {
		WSDLFile wsdlFile = new WSDLFile(wsdl, root);
		//String version = 
		StringBuilder str = new StringBuilder();
		str.append(endpointPrefix);
		if (!endpointPrefix.endsWith("/"))
			str.append('/');
		str.append(wsdlFile.getProxyName());
		str.append(getVersion(wsdlFile.portType.getQName().getNamespaceURI()));		
		wsdlFile.frontsideURI = str.toString();
		return wsdlFile;
	}
	
	protected static String getVersion(String namespaceURI) {
		System.out.println("Namespace URI = " + namespaceURI);
		String version = namespaceURI.substring(namespaceURI.lastIndexOf('/') + 1);
		System.out.println("Version = " + version);		
		return version;
	}
}
