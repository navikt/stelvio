package no.nav.datapower.config;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
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
	private List<Binding> bindingList;
	private PortType portType;
	private Service service;
	private Port port;
	private SOAPAddress soapAddress;
	private String relativePath;
	private String frontsideURI;
	private WSProxy proxy;

	public WSDLFile(File wsdlFile) {
		this.fileName = wsdlFile.getName();
		this.definition = DPWsdlUtils.getDefinition(wsdlFile.getPath());

		this.bindingList = new ArrayList<Binding>(); 
		Iterator bindingIterator = definition.getAllBindings().values().iterator();
		while (bindingIterator.hasNext())
			bindingList.add((Binding) bindingIterator.next());
		
		this.portType = (PortType) definition.getAllPortTypes().values().iterator().next();
		this.service = (Service) definition.getAllServices().values().iterator().next();		
		this.port = (Port) service.getPorts().values().iterator().next();
		this.soapAddress = (SOAPAddress) port.getExtensibilityElements().get(0);
		this.location = URI.create(soapAddress.getLocationURI());
		this.frontsideURI = getEndpointURI();
	}

	public WSDLFile(File wsdlFile, File root) {
		this(wsdlFile);
		this.relativePath = DPFileUtils.replaceSeparator(DPFileUtils.getRelativePath(wsdlFile, root), '/');
//		System.out.println("WSDLFile.relativePath = " + relativePath);
	}
	
	public String getRelativePath() {
		return relativePath;
	}
	
	public String getProxyName() {
		return portType.getQName().getLocalPart();
	}
	
	public List<String> getPortBinding(){		
		List<String> portBindings = new ArrayList<String>();
		for (Binding binding : bindingList){
			QName portBinding = new QName(binding.getQName().getNamespaceURI(), port.getName());
			portBindings.add(portBinding.toString());
		}		
		return portBindings;
	}
	
	public String getPortType() {
		return portType.getQName().toString();
	}
	
	public String getService() {
		return service.getQName().toString();
	}
	
	public String getServiceName() {
		return service.getQName().getLocalPart();
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getEndpointURI() {
		return location.getPath();
	}
	
	public void setFrontsideURI(String uri) {
		frontsideURI = uri;
	}

	public String getFrontsideURI() {
		if (proxy != null && proxy.hasMultipleWsdls()) {
			String version = getVersion();
			if (!version.equals("") && !frontsideURI.contains(version))
				return frontsideURI + version;
		}
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
	
	public String getVersion() {
		String version = "";
		String namespaceURI = portType.getQName().getNamespaceURI();
//		System.out.println("Namespace URI = " + namespaceURI);
		if ((namespaceURI.length() - namespaceURI.lastIndexOf('/')) == 5) {
			version = namespaceURI.substring(namespaceURI.lastIndexOf('/') + 1);
		}
//		System.out.println("Version = " + version);		
		return version;		
	}
	
	protected static String getVersion(String namespaceURI) {
//		System.out.println("Namespace URI = " + namespaceURI);
		String version = namespaceURI.substring(namespaceURI.lastIndexOf('/') + 1);
//		System.out.println("Version = " + version);		
		return version;
	}

	protected WSProxy getProxy() {
		return proxy;
	}

	protected void setProxy(WSProxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public boolean equals(Object oThat) {
		if (oThat instanceof WSDLFile) {
			WSDLFile that = (WSDLFile) oThat;
			if (that.getRelativePath().equals(this.relativePath)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getRelativePath().hashCode();
	}

	public void rewriteEndpoint() {
		String endpoint = "/elsam/tptilb/" + getProxyName() + getVersion(portType.getQName().getNamespaceURI());
		frontsideURI = endpoint;
	}

}
