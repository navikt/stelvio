package no.nav.maven.plugins.descriptor.websphere.ext;

import java.util.Iterator;

import no.nav.maven.plugins.descriptor.websphere.IbmWebServiceDescriptorEditor;
import no.nav.maven.plugins.descriptor.websphere.WebSphereFactories;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;

import com.ibm.etools.webservice.wscbnd.ClientBinding;
import com.ibm.etools.webservice.wscbnd.WscbndFactory;
import com.ibm.etools.webservice.wscext.ClientServiceConfig;
import com.ibm.etools.webservice.wscext.ComponentScopedRefs;
import com.ibm.etools.webservice.wscext.PortQnameBinding;
import com.ibm.etools.webservice.wscext.SecurityRequestGeneratorServiceConfig;
import com.ibm.etools.webservice.wscext.ServiceRef;
import com.ibm.etools.webservice.wscext.WsClientExtension;
import com.ibm.etools.webservice.wscext.WscextFactory;
import com.ibm.etools.webservice.wscommonext.SecurityToken;
import com.ibm.etools.webservice.wscommonext.WscommonextFactory;


public class IbmWebServiceClientExtEditor extends IbmWebServiceDescriptorEditor<WsClientExtension> {
	
    private static final String WEBSERVICECLIENT_EXT_FILENAME = "ibm-webservicesclient-ext.xmi";
    private static final String LTPA_URI = "http://www.ibm.com/websphere/appserver/tokentype/5.0.2";
    private WsClientExtension clientExtension;
    
    public IbmWebServiceClientExtEditor(Archive archive) {    	
    	super(archive,WEBSERVICECLIENT_EXT_FILENAME);
        clientExtension = getDescriptor();
    }
    
    protected WsClientExtension createDescriptorContent(){		
		return WscextFactory.eINSTANCE.createWsClientExtension();
	}
    
    public void addRequestGeneratorLTPA(String partRef) {
    	Iterator iter = getServiceRefs();
    	while(iter.hasNext()){
    		ServiceRef serviceRef = (ServiceRef)iter.next();
    		addRequestGeneratorLTPA(partRef, serviceRef);
    	}
    }
    private void addRequestGeneratorLTPA(String partRef, ServiceRef serviceRef) {
    	ClientServiceConfig clientSrvCfg = getClientServiceConfig(getPortQnameBinding(serviceRef));
    	System.out.println("ClientServiceConfig: " + clientSrvCfg);
    	SecurityRequestGeneratorServiceConfig requestGenCfg = clientSrvCfg.getSecurityRequestGeneratorServiceConfig();
    	if (requestGenCfg == null) {
    		requestGenCfg = WebSphereFactories.getWscextFactory().createSecurityRequestGeneratorServiceConfig();
    		clientSrvCfg.setSecurityRequestGeneratorServiceConfig(requestGenCfg);
    	}
    	EList securityTokens = requestGenCfg.getSecurityToken();
    	if(!isLTPASecurityTokenPresent(securityTokens)){
    		SecurityToken ltpaToken = createLTPASecurityToken(partRef);
    		securityTokens.add(ltpaToken);
    	}
    }
    
    private PortQnameBinding getPortQnameBinding(ServiceRef serviceRef) {
		return (PortQnameBinding) serviceRef.getPortQnameBindings().get(0);   	
    }
    
    private Iterator getServiceRefs() {
		ComponentScopedRefs compScopedRefs = (ComponentScopedRefs)getComponentScopedRefs().get(0);
		return compScopedRefs.getServiceRefs().iterator();	
    }
    
    
    private EList getComponentScopedRefs() {
    	return clientExtension.getComponentScopedRefs();
    }
    
    
    private ClientServiceConfig getClientServiceConfig(PortQnameBinding portQNbnd){
    	ClientServiceConfig clientSrvCfg = portQNbnd.getClientServiceConfig();
    	if(clientSrvCfg == null){
    		clientSrvCfg = WebSphereFactories.getWscextFactory().createClientServiceConfig();
    		portQNbnd.setClientServiceConfig(clientSrvCfg);
    	}
    	return portQNbnd.getClientServiceConfig();
    }
    
    private SecurityToken createLTPASecurityToken(String partRef){
    	SecurityToken ltpaToken = WebSphereFactories.getWscommonextFactory().createSecurityToken();
		ltpaToken.setLocalName("LTPA");
		ltpaToken.setName(partRef);
		ltpaToken.setUri(LTPA_URI);
		return ltpaToken;
    }
    
    private boolean isLTPASecurityTokenPresent(EList securityTokens){
    	Iterator securityTokensIter = securityTokens.iterator();
    	while (securityTokensIter.hasNext()) {
    		SecurityToken securityToken = (SecurityToken) securityTokensIter.next();
    		if (securityToken.getUri().equals(LTPA_URI)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    public void addRequestGeneratorUsername() {
    	
    }
}
