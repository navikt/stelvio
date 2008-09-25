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
 
    private EList getComponentScopedRefs() {
    	return clientExtension.getComponentScopedRefs();
    }
    
    private PortQnameBinding getPortQnameBinding() {
    	ComponentScopedRefs compScopedRefs = (ComponentScopedRefs)getComponentScopedRefs().get(0);
    	EList serviceRefs = compScopedRefs.getServiceRefs();
    	ServiceRef serviceRef = (ServiceRef)serviceRefs.get(0);
    	return (PortQnameBinding)serviceRef.getPortQnameBindings().get(0);
    }
    
    public void addRequestGeneratorLTPA(String partRef) {
    	ClientServiceConfig clientSrvCfg = getPortQnameBinding().getClientServiceConfig();
    	System.out.println("ClientServiceConfig: " + clientSrvCfg);
    	SecurityRequestGeneratorServiceConfig requestGenCfg = clientSrvCfg.getSecurityRequestGeneratorServiceConfig();
    	if (requestGenCfg == null) {
    		requestGenCfg = WebSphereFactories.getWscextFactory().createSecurityRequestGeneratorServiceConfig();
    		clientSrvCfg.setSecurityRequestGeneratorServiceConfig(requestGenCfg);
    	}
    	EList securityTokens = requestGenCfg.getSecurityToken();
    	Iterator securityTokensIter = securityTokens.iterator();
    	while (securityTokensIter.hasNext()) {
    		SecurityToken securityToken = (SecurityToken) securityTokensIter.next();
    		System.out.println("SecurityToken: " + securityToken);
    		if (securityToken.getUri().equals(LTPA_URI)) {
    			securityTokens.remove(securityToken);
    			SecurityToken ltpaToken = WebSphereFactories.getWscommonextFactory().createSecurityToken();
    			securityTokens.add(ltpaToken);
    			ltpaToken.setLocalName("LTPA");
    			ltpaToken.setName(partRef);
    			ltpaToken.setUri(LTPA_URI);
    		}
    	}
    }
    
    public void addRequestGeneratorUsername() {
    	
    }
}
