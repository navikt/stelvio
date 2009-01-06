package no.nav.maven.plugins.descriptor.websphere;

import com.ibm.etools.webservice.wsbnd.WsbndFactory;
import com.ibm.etools.webservice.wscbnd.WscbndFactory;
import com.ibm.etools.webservice.wscext.WscextFactory;
import com.ibm.etools.webservice.wscommonbnd.WscommonbndFactory;
import com.ibm.etools.webservice.wscommonext.WscommonextFactory;
import com.ibm.etools.webservice.wsext.WsextFactory;
import com.ibm.etools.webservice.wsext.util.WsextAdapterFactory;

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
	
	public static WsextFactory getWsextFactory(){
		return WsextFactory.eINSTANCE;
	}
	
	public static WsbndFactory getWsbndFactory(){
		return WsbndFactory.eINSTANCE;
	}
	
	
}
