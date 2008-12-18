package no.nav.maven.plugins.descriptor.websphere;

import com.ibm.etools.webservice.wscbnd.WscbndFactory;
import com.ibm.etools.webservice.wscext.WscextFactory;
import com.ibm.etools.webservice.wscommonbnd.WscommonbndFactory;
import com.ibm.etools.webservice.wscommonext.WscommonextFactory;

public class WebSphereFactories {

	private WebSphereFactories() {}
	
	public static WscbndFactory getWscbndFactory() {
		return WscbndFactory.eINSTANCE;
	}
	
	public static WscextFactory getWscextFactory() {
		return WscextFactory.eINSTANCE;
	}
	
	public static WscommonbndFactory getWscommonbndFactory() {
		return WscommonbndFactory.eINSTANCE;
	}
	
	public static WscommonextFactory getWscommonextFactory() {
		return WscommonextFactory.eINSTANCE;
	}
}
