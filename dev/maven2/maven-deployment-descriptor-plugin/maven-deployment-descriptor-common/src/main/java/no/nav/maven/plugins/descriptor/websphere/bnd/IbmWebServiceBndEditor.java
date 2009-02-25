package no.nav.maven.plugins.descriptor.websphere.bnd;

import java.net.URI;
import java.net.URL;
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
import com.ibm.etools.webservice.wsbnd.PCBinding;
import com.ibm.etools.webservice.wsbnd.SecurityRequestConsumerBindingConfig;
import com.ibm.etools.webservice.wsbnd.WSBinding;
import com.ibm.etools.webservice.wsbnd.WSDescBinding;
import com.ibm.etools.webservice.wsbnd.WsbndFactory;
import com.ibm.etools.webservice.wscbnd.ComponentScopedRefs;
import com.ibm.etools.webservice.wscbnd.PortQnameBinding;
import com.ibm.etools.webservice.wscbnd.SSLConfig;
import com.ibm.etools.webservice.wscbnd.SecurityRequestGeneratorBindingConfig;
import com.ibm.etools.webservice.wscbnd.ServiceRef;
import com.ibm.etools.webservice.wscbnd.WscbndFactory;
import com.ibm.etools.webservice.wscommonbnd.PartReference;
import com.ibm.etools.webservice.wscommonbnd.TokenConsumer;
import com.ibm.etools.webservice.wscommonbnd.TokenGenerator;
import com.ibm.etools.webservice.wscommonbnd.ValueType;
import com.ibm.etools.webservice.wsext.PcBinding;

public class IbmWebServiceBndEditor extends IbmWebServiceDescriptorEditor<WSBinding> {

    private static final String WEBSERVICE_BND_FILENAME = "ibm-webservices-bnd.xmi";
    private static final String LTPA_URI = "http://www.ibm.com/websphere/appserver/tokentype/5.0.2";
    private static final String USERNAME_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken";
    private WSBinding wsBinding;
    
    public IbmWebServiceBndEditor(Archive archive) {
    	super(archive, WEBSERVICE_BND_FILENAME);
        wsBinding = getDescriptor();
    }
     
	protected WSBinding createDescriptorContent(){		
		return WsbndFactory.eINSTANCE.createWSBinding();
	}
    
	private Iterator getWsdescBindings(){
		return wsBinding.getWsdescBindings().iterator();
	}
	
	public void addLTPATokenConsumer(String partRef){
		Iterator iter = getWsdescBindings();
		while (iter.hasNext()){
			WSDescBinding wsDescBinding = (WSDescBinding) iter.next();
			TokenConsumer ltpaTokenConsumer = TokenType.createLTPATokenConsumer(partRef);
			addTokenConsumer(wsDescBinding, ltpaTokenConsumer);			
		}
	}
	
	public void addUsernameTokenConsumer(String partRef){
		Iterator iter = getWsdescBindings();
		while (iter.hasNext()){
			WSDescBinding wsDescBinding = (WSDescBinding) iter.next();
			TokenConsumer usernameTokenConsumer = TokenType.createUsernameTokenConsumer(partRef);			
			addTokenConsumer(wsDescBinding, usernameTokenConsumer);			
		}
	}
		
	public void deleteExistingConfig(){
		Iterator iter = getWsdescBindings();
		while (iter.hasNext()){
			WSDescBinding wsDescBinding = (WSDescBinding) iter.next();
			PCBinding pcBinding = (PCBinding) wsDescBinding.getPcBindings().get(0);
			if (pcBinding.getSecurityRequestConsumerBindingConfig() != null)
				System.out.println("Overwriting existing SecurityRequestConsumberBindingConfig.");
			pcBinding.setSecurityRequestConsumerBindingConfig(null);		
		}
	}
	
	public final boolean isExposingWebServices() {
		return (wsBinding.getWsdescBindings() != null && wsBinding.getWsdescBindings().size() > 0) ? true : false;
	}
	
	private void addTokenConsumer(WSDescBinding wsDescBinding, TokenConsumer tokenConsumer){
		PCBinding pcBinding = (PCBinding) wsDescBinding.getPcBindings().get(0);
		SecurityRequestConsumerBindingConfig secRecCfg = 
			pcBinding.getSecurityRequestConsumerBindingConfig();		
		if (secRecCfg == null){			
			secRecCfg = WebSphereFactories.getWsbndFactory().createSecurityRequestConsumerBindingConfig();				
			pcBinding.setSecurityRequestConsumerBindingConfig(secRecCfg);
		}
		
		EList tokenConsumers = secRecCfg.getTokenConsumer();
		Iterator tokenConsIter = tokenConsumers.iterator();
		if(!isTokenConsumerPresent(tokenConsumers, tokenConsumer)){			
			tokenConsumers.add(tokenConsumer);
		}		
	}

	private boolean isTokenConsumerPresent(EList tokenConsumers, TokenConsumer tokenConsumer){
		Iterator iter = tokenConsumers.iterator();
		while (iter.hasNext()){
			TokenConsumer tc = (TokenConsumer) iter.next();
			if(tc != null && tc.getClassname().equals(tokenConsumer.getClassname()))
				return true;			    					    			
		}		
		return false;
	}	
   
}
