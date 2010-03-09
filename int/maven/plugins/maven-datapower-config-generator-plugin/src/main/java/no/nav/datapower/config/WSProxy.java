package no.nav.datapower.config;

import java.util.List;

import no.nav.datapower.util.DPCollectionUtils;


public class WSProxy {

	private String name;
	private List<WSDLFile> wsdls;
	public WSProxy(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public List<WSDLFile> getWsdls() {
//		System.out.println("getWsdls(), proxyName = " + name);
		if (wsdls == null)
			wsdls = DPCollectionUtils.newArrayList();
		return wsdls;
	}
	
	public void setWsdls(List<WSDLFile> wsdls) {
		this.wsdls = wsdls;
	}

	public void addWsdl(WSDLFile wsdl) {
		getWsdls().add(wsdl);
		wsdl.setProxy(this);
	}
	
	public boolean hasMultipleWsdls() {
		return wsdls.size() > 1;
	}
}
