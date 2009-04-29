package no.nav.maven.plugins.descriptor.jee;

import no.nav.maven.plugins.descriptor.common.DeploymentDescriptorEditor;

import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.ejb.EjbFactory;
import org.eclipse.jst.j2ee.ejb.EjbPackage;
import org.eclipse.jst.j2ee.webservice.wsdd.Handler;
import org.eclipse.jst.j2ee.webservice.wsdd.PortComponent;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServiceDescription;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServices;


public class WebServicesEditor extends DeploymentDescriptorEditor<WebServices>{

	protected WebServices webServices;	
	private static final String WEBSERVICES_FILE_NAME = "webservices.xml";
	
	public WebServicesEditor(Archive archive){
		super(archive, WEBSERVICES_FILE_NAME);
		webServices = getDescriptor();
	}
	
	protected EjbFactory getEjbFactory(){
		return EjbPackage.eINSTANCE.getEjbFactory();
	}

	public void addHandler(final String name, final String clazz){
		for(Object o : webServices.getWebServiceDescriptions()) {
			WebServiceDescription wsd = (WebServiceDescription)o;
			for( Object o2 : wsd.getPortComponents()) {
				PortComponent pc = (PortComponent)o2;
				Handler nyHandler = getWsddFactory().createHandler();
				nyHandler.setHandlerName(name);
				nyHandler.setHandlerClass(clazz);
				pc.getHandlers().add(nyHandler);
			}
		}	
	}
	
	@Override
	protected WebServices createDescriptorContent() {
		return webServices;
	}

}
