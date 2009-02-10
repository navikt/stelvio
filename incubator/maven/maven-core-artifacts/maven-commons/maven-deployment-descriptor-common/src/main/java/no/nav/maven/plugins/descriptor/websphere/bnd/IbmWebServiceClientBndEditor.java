package no.nav.maven.plugins.descriptor.websphere.bnd;

import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

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
    
   
    public void setSslConfig(String name) {
    	Iterator iter = getServiceRefs();
    	while(iter.hasNext()){
    		ServiceRef serviceRef = (ServiceRef)iter.next();
			setSslConfig(name, serviceRef);
		}
    }
    
    private void setSslConfig(String name, ServiceRef serviceRef){
    	PortQnameBinding portQNBnd = getPortQnameBinding(serviceRef);
    	SSLConfig sslConfig = portQNBnd.getSslConfig();
		sslConfig.setName(name);		
    }
    
    public void setEndpointUri(String newDefaultPortServerAddress, Properties myEndpoints) {
    	Iterator iter = getServiceRefs();
    	while(iter.hasNext()){
    		ServiceRef serviceRef = (ServiceRef)iter.next();
    		
    		/*
    		 * Det vanligste er at en modul vil koble alle web servicesene sine til en host.
    		 * Men noen moduler, spesielt arena vil koble seg opp mot flere forskjellige baksystemer.
    		 */
    		if (myEndpoints != null){
    			String newPortServerAddress = myEndpoints.getProperty(serviceRef.getServiceRefLink());    			
        		if (newDefaultPortServerAddress != null)
        			setEndpointUri(newPortServerAddress, serviceRef);
        		else
        			setEndpointUri(newDefaultPortServerAddress, serviceRef);	
    		}
    		else 
    			setEndpointUri(newDefaultPortServerAddress, serviceRef);
    	}
    }
    private void setEndpointUri(String newPortServerAddress, ServiceRef serviceRef) {
		PortQnameBinding portQNBnd = getPortQnameBinding(serviceRef);
		String endpoint = portQNBnd.getOverriddenEndpointURI();
		if(endpoint != null){
			if(newPortServerAddress != null){
				portQNBnd.setOverriddenEndpointURI(createNewEndpointURI(endpoint, newPortServerAddress));
			}
		}
    }
    
    private String createNewEndpointURI(String fullUrl, String newPortServerAddress){
    	URI url = null;
      	boolean noRelativePath = false;
      	
    	try {
    		url = URI.create(fullUrl);
    	} catch (Exception e) {
    		noRelativePath = true;
		}

		newPortServerAddress = newPortServerAddress.endsWith("/") ? 
				newPortServerAddress.substring(0, newPortServerAddress.length() - 1) :
					newPortServerAddress;
		
		String endpointURI = null;
		if(!noRelativePath) {
			String uri = url.getPath();
			endpointURI = newPortServerAddress + uri;
		} else {
			endpointURI = newPortServerAddress;
		}
		
		return endpointURI;
    }
    
    public void addBasicAuth(String username, String password) {
    	Iterator iter = getServiceRefs();
    	while(iter.hasNext()){
    		ServiceRef serviceRef = (ServiceRef)iter.next();
			addBasicAuth(username, password, serviceRef);
    	}
    }
    
    private void addBasicAuth(String username, String password, ServiceRef serviceRef) {
		PortQnameBinding portQNBnd = getPortQnameBinding(serviceRef);
		BasicAuth basicAuth = WebSphereFactories.getWscbndFactory().createBasicAuth();
		basicAuth.setUserid(username);
		basicAuth.setPassword(password);
		portQNBnd.setBasicAuth(basicAuth);
    }
    
    public void addRequestTokenGeneratorLTPA(String partRef) {
    	Iterator iter = getServiceRefs();
    	while(iter.hasNext()){
    		ServiceRef serviceRef = (ServiceRef)iter.next();
			addRequestTokenGeneratorLTPA(partRef, serviceRef);
    	}
    }
    
    private void addRequestTokenGeneratorLTPA(String partRef, ServiceRef serviceRef) {
		TokenType tokenType = TokenType.createLTPA();
		addRequestTokenGenerator(tokenType, partRef, serviceRef);
    }
    
    private void addRequestTokenGenerator(TokenType tokenType, String partRef, ServiceRef serviceRef) {
		SecurityRequestGeneratorBindingConfig requestGenBnd = 
			getSecurityRequestGeneratorBindingConfig(getPortQnameBinding(serviceRef));
		EList tokenGenerators = requestGenBnd.getTokenGenerator();
		Iterator tokenGenIter = tokenGenerators.iterator();
		if(!isTokenGeneratorPresent(tokenGenerators, tokenType)){
			TokenGenerator newTokenGen = createTokenGenerator(tokenType, partRef);
			System.out.println("TokenGenerator is not present. Adding new generator with TokenType:" + newTokenGen.getValueType());
			tokenGenerators.add(newTokenGen);
		}
    }
    
    private EList getComponentScopedRefs() {
    	return clientBinding.getComponentScopedRefs();
    }
    
    private PortQnameBinding getPortQnameBinding(ServiceRef serviceRef) {
		return (PortQnameBinding) serviceRef.getPortQnameBindings().get(0);   	
    }
    
    private Iterator getServiceRefs() {
		ComponentScopedRefs compScopedRefs = (ComponentScopedRefs)getComponentScopedRefs().get(0);
		return compScopedRefs.getServiceRefs().iterator();	
    }
    
    private SecurityRequestGeneratorBindingConfig getSecurityRequestGeneratorBindingConfig(PortQnameBinding portQNBnd) {  	
    	SecurityRequestGeneratorBindingConfig cfg = portQNBnd.getSecurityRequestGeneratorBindingConfig();
    	if(cfg == null){
    		cfg = WebSphereFactories.getWscbndFactory().createSecurityRequestGeneratorBindingConfig();
    		portQNBnd.setSecurityRequestGeneratorBindingConfig(cfg);
    	}
    	return portQNBnd.getSecurityRequestGeneratorBindingConfig();
    }
       
 
    
    private boolean isTokenGeneratorPresent(EList tokenGenerators, TokenType tokenType){
    	Iterator tokenGenIter = tokenGenerators.iterator();
    	while (tokenGenIter.hasNext()) {
			TokenGenerator tokenGen = (TokenGenerator) tokenGenIter.next();
			if (tokenGen != null && ValueTypes.equals(tokenGen.getValueType(), tokenType.valueType())) {
				return true;
			}
		}
    	return false;
    }
    
    private TokenGenerator createTokenGenerator(TokenType tokenType, String partRef){
    	TokenGenerator newTokenGen = tokenType.tokenGenerator();
		PartReference partReference = WebSphereFactories.getWscommonbndFactory().createPartReference();
		partReference.setPart(partRef);
		newTokenGen.setPartReference(partReference);
		return newTokenGen;
    }
    
   
}
