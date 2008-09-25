package no.nav.maven.plugins.descriptor.websphere.bnd;

import java.util.Iterator;

import no.nav.maven.plugins.descriptor.jee.EjbJarEditor;
import no.nav.maven.plugins.descriptor.websphere.IbmWebServiceDescriptorEditor;
import no.nav.maven.plugins.descriptor.websphere.TokenType;
import no.nav.maven.plugins.descriptor.websphere.ValueTypes;
import no.nav.maven.plugins.descriptor.websphere.WebSphereFactories;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;

import com.ibm.etools.webservice.wscbnd.BasicAuth;
import com.ibm.etools.webservice.wscbnd.ClientBinding;
import com.ibm.etools.webservice.wscbnd.ComponentScopedRefs;
import com.ibm.etools.webservice.wscbnd.PortQnameBinding;
import com.ibm.etools.webservice.wscbnd.SSLConfig;
import com.ibm.etools.webservice.wscbnd.SecurityRequestGeneratorBindingConfig;
import com.ibm.etools.webservice.wscbnd.ServiceRef;
import com.ibm.etools.webservice.wscbnd.WscbndFactory;
import com.ibm.etools.webservice.wscommonbnd.PartReference;
import com.ibm.etools.webservice.wscommonbnd.TokenGenerator;

public class IbmWebServiceClientBndEditor extends IbmWebServiceDescriptorEditor<ClientBinding> {

    private static final String WEBSERVICECLIENT_BND_FILENAME = "ibm-webservicesclient-bnd.xmi";
    private ClientBinding clientBinding;
    
    public IbmWebServiceClientBndEditor(Archive archive) {
    	super(archive, WEBSERVICECLIENT_BND_FILENAME);
        clientBinding = getDescriptor();
    }
     
	protected ClientBinding createDescriptorContent(){		
		return WscbndFactory.eINSTANCE.createClientBinding();
	}
    
    private EList getComponentScopedRefs() {
    	return clientBinding.getComponentScopedRefs();
    }
    
    public void setSslConfig(String name) {
		PortQnameBinding portQNBnd = getPortQnameBinding();
		SSLConfig sslConfig = portQNBnd.getSslConfig();
		sslConfig.setName(name);		
    }
    
    private PortQnameBinding getPortQnameBinding() {
		ComponentScopedRefs compScopedRefs = (ComponentScopedRefs)getComponentScopedRefs().get(0);
		ServiceRef serviceRef = (ServiceRef) compScopedRefs.getServiceRefs().get(0);
		return (PortQnameBinding) serviceRef.getPortQnameBindings().get(0);   	
    }
    
    private SecurityRequestGeneratorBindingConfig getSecurityRequestGeneratorBindingConfig() {
    	return getPortQnameBinding().getSecurityRequestGeneratorBindingConfig();
    }
       
    public void setEndpointUri(String uri) {
		PortQnameBinding portQNBnd = getPortQnameBinding();
		portQNBnd.setOverriddenEndpointURI(uri);
    }
    
    public void addRequestTokenGeneratorLTPA(String partRef) {
		TokenType tokenType = TokenType.createLTPA();
		addRequestTokenGenerator(tokenType, partRef);
    }
    
    public void addRequestTokenGenerator(TokenType tokenType, String partRef) {
		SecurityRequestGeneratorBindingConfig requestGenBnd = getSecurityRequestGeneratorBindingConfig();
		EList tokenGenerators = requestGenBnd.getTokenGenerator();
		Iterator tokenGenIter = tokenGenerators.iterator();
		while (tokenGenIter.hasNext()) {
			TokenGenerator tokenGen = (TokenGenerator) tokenGenIter.next();
			System.out.println("TokenGenerator: " + tokenGen);
			if (tokenGen != null && !ValueTypes.equals(tokenGen.getValueType(), tokenType.valueType())) {
				System.out.println("Adding TokenGenerator: " + tokenType.tokenGenerator());
				TokenGenerator newTokenGen = tokenType.tokenGenerator();
				PartReference partReference = WebSphereFactories.getWscommonbndFactory().createPartReference();
				partReference.setPart(partRef);
				newTokenGen.setPartReference(partReference);
				tokenGenerators.add(newTokenGen);
			}
			System.out.println("ValueType: " + tokenGen.getValueType());
		}
    }
    
    public void addBasicAuth(String username, String password) {
		PortQnameBinding portQNBnd = getPortQnameBinding();
		BasicAuth basicAuth = WebSphereFactories.getWscbndFactory().createBasicAuth();
		basicAuth.setUserid(username);
		basicAuth.setPassword(password);
		portQNBnd.setBasicAuth(basicAuth);
    }
}
