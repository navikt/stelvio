package no.nav.serviceregistry.util;

import java.io.File;

public class ServiceWrapper {
	
	private String name;
	private String path;
	private File wsdlDir;
	
	@SuppressWarnings("unused")
	private ServiceWrapper () {}
	
	public ServiceWrapper(String name, String path, File wsdlDir) {
		this.setName(name);
		this.setPath(path);
		this.setWsdlDir(wsdlDir);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setWsdlDir(File wsdlDir) {
		this.wsdlDir = wsdlDir;
	}

	public File getWsdlDir() {
		return wsdlDir;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass())
			return false;
		ServiceWrapper other = (ServiceWrapper) obj;
		return name.equals(other.name);
	}
}
