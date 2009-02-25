package no.nav.maven.plugins.descriptor.websphere.ext;

import java.io.FileNotFoundException;
import java.util.Iterator;

import no.nav.maven.plugins.descriptor.websphere.IbmWebServiceDescriptorEditor;
import no.nav.maven.plugins.descriptor.websphere.TokenType;
import no.nav.maven.plugins.descriptor.websphere.ValueTypes;
import no.nav.maven.plugins.descriptor.websphere.WebSphereFactories;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.ReopenException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.SaveFailureException;

import com.ibm.etools.webservice.wscbnd.ClientBinding;
import com.ibm.etools.webservice.wsbnd.WSBinding;

import com.ibm.etools.webservice.wscbnd.WscbndFactory;
import com.ibm.etools.webservice.wsbnd.WsbndFactory;

import com.ibm.etools.webservice.wscext.ClientServiceConfig;
import com.ibm.etools.webservice.wsext.PcBinding;
import com.ibm.etools.webservice.wsext.SecurityRequestConsumerServiceConfig;
import com.ibm.etools.webservice.wsext.ServerServiceConfig;
import com.ibm.etools.webservice.wsext.WsDescExt;

import com.ibm.etools.webservice.wscext.ComponentScopedRefs;
//import com.ibm.etools.webservice.wsext.

import com.ibm.etools.webservice.wscext.PortQnameBinding;
//import com.ibm.etools.webservice.wsext.
import com.ibm.etools.webservice.wscext.SecurityRequestGeneratorServiceConfig;
import com.ibm.etools.webservice.wscext.ServiceRef;
import com.ibm.etools.webservice.wscext.WsClientExtension;
import com.ibm.etools.webservice.wscext.WscextFactory;
import com.ibm.etools.webservice.wsext.WsextFactory;
import com.ibm.etools.webservice.wscommonbnd.TokenConsumer;
import com.ibm.etools.webservice.wscommonbnd.TokenGenerator;
import com.ibm.etools.webservice.wscommonext.Caller;
import com.ibm.etools.webservice.wscommonext.RequiredSecurityToken;
import com.ibm.etools.webservice.wscommonext.SecurityToken;
import com.ibm.etools.webservice.wscommonext.UsageType;
import com.ibm.etools.webservice.wscommonext.WscommonextFactory;
import com.ibm.etools.webservice.wscommonext.impl.RequiredSecurityTokenImpl;
import com.ibm.etools.webservice.wsext.WsExtension;
import com.ibm.etools.webservice.wsext.impl.PcBindingImpl;

public class IbmWebServiceExtEditor extends
		IbmWebServiceDescriptorEditor<WsExtension> {

	private static final String WEBSERVICE_EXT_FILENAME = "ibm-webservices-ext.xmi";

	private static final String LTPA_URI = "http://www.ibm.com/websphere/appserver/tokentype/5.0.2";
	private static final String USERNAME_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken";

	private WsExtension wsExtension;

	public IbmWebServiceExtEditor(Archive archive) {
		super(archive, WEBSERVICE_EXT_FILENAME);
		wsExtension = getDescriptor();

	}

	protected WsExtension createDescriptorContent() {
		return WsextFactory.eINSTANCE.createWsExtension();
	}

	private EList getWsDescExtensions() {
		return wsExtension.getWsDescExt();
	}
	
	public void deleteExistingConfig(){
		Iterator iter = getWsDescExtensions().iterator();
		while (iter.hasNext()){
			WsDescExt wsDescExt = (WsDescExt) iter.next();
			PcBinding pcBinding = (PcBinding) wsDescExt.getPcBinding().get(0);
			if (pcBinding.getServerServiceConfig() != null)
				System.out.println("Overwriting existing ServerServiceConfig.");
			pcBinding.setServerServiceConfig(null);
		}
	}

	private ServerServiceConfig getServerServiceConfig(PcBinding pcBinding){		
		return pcBinding.getServerServiceConfig();
	}

	private boolean isLTPASecurityTokenPresent(EList requiredSecurityTokens) {
		Iterator iter = requiredSecurityTokens.iterator();
		while (iter.hasNext()) {
			RequiredSecurityToken securityToken = (RequiredSecurityToken) iter.next();
			if (securityToken.getUri().equals(LTPA_URI)) 
				return true;			
		}
		return false;
	}

	private boolean isUsernameSecurityTokenPresent(EList requiredSecurityTokens){
		Iterator iter = requiredSecurityTokens.iterator();
		while (iter.hasNext()) {
			RequiredSecurityToken securityToken = (RequiredSecurityToken) iter.next();
			if (USERNAME_URI.equals(securityToken.getLocalName()))
					return true;			
		}
		return false;
	}
	
	private boolean isLTPACallerPresent(EList callers) {
		Iterator callersIter = callers.iterator();
		while (callersIter.hasNext()) {
			Caller caller = (Caller) callersIter.next();			
			if (caller != null && LTPA_URI.equals(caller.getUri())) 
				return true;			
		}
		return false;
	}
	
	private boolean isUsernameCallerPresent(EList callers) {
		Iterator callersIter = callers.iterator();
		while (callersIter.hasNext()) {
			Caller caller = (Caller) callersIter.next();			
			if (caller != null && USERNAME_URI.equals(caller.getLocalName())) 
				return true;			
		}
		return false;
	}

	    
	public void addRequestConsumerLTPAToken(boolean usageRequired, String tokenPartReference) {
		Iterator iter = getWsDescExtensions().iterator();
		while (iter.hasNext()) {
			SecurityRequestConsumerServiceConfig secRecConfig = 
				getSecRecConfig((WsDescExt) iter.next());
						
			EList securityTokens = secRecConfig.getRequiredSecurityToken();			
			if (!isLTPASecurityTokenPresent(securityTokens)) {
				securityTokens.add(createLTPARequiredSecurityToken(usageRequired, tokenPartReference)); 								
			}
			
			EList callers = secRecConfig.getCaller();
			if (!isLTPACallerPresent(callers)) {
				callers.add(createLTPACaller());
			}
		}
	}

	public void addRequestConsumerUsernameToken(boolean usageRequired, String tokenPartReference) {		
		Iterator iter = getWsDescExtensions().iterator();
		while (iter.hasNext()) {
			SecurityRequestConsumerServiceConfig secRecConfig = 
				getSecRecConfig((WsDescExt) iter.next());
			
			EList securityTokens = secRecConfig.getRequiredSecurityToken();			
			if (!isUsernameSecurityTokenPresent(securityTokens)) {
				securityTokens.add(createUsernameRequiredSecurityToken(usageRequired, tokenPartReference)); //TODO								
			}
			
			EList callers = secRecConfig.getCaller();
			if (!isUsernameCallerPresent(callers)) {
				callers.add(createUsernameCaller());
			}
		}		
	}
	
	private SecurityRequestConsumerServiceConfig getSecRecConfig(WsDescExt wsDescExt){		
		PcBinding pcBinding = (PcBinding) wsDescExt.getPcBinding().get(0);
		ServerServiceConfig serviceCfg = getServerServiceConfig(pcBinding);
		if (serviceCfg == null){
			serviceCfg = WebSphereFactories.getWsextFactory().createServerServiceConfig();				 
			pcBinding.setServerServiceConfig(serviceCfg);
		}
		SecurityRequestConsumerServiceConfig secRecConfig = serviceCfg.getSecurityRequestConsumerServiceConfig();			

		if (secRecConfig == null){				
			secRecConfig = WebSphereFactories.getWsextFactory().createSecurityRequestConsumerServiceConfig();
			serviceCfg.setSecurityRequestConsumerServiceConfig(secRecConfig);
		}		
		return secRecConfig;
	}
		
    private RequiredSecurityToken createLTPARequiredSecurityToken(boolean usageRequired, String tokenPartReference){
    	RequiredSecurityToken ltpaToken = WebSphereFactories.getWscommonextFactory().createRequiredSecurityToken();
		ltpaToken.setLocalName("LTPA");
		ltpaToken.setName(tokenPartReference);
		ltpaToken.setUri(LTPA_URI);
		if (usageRequired)
			ltpaToken.setUsage(UsageType.REQUIRED_LITERAL);		
		return ltpaToken;
    }
    
    private Caller createLTPACaller(){
    	Caller ltpaCaller = WebSphereFactories.getWscommonextFactory().createCaller();
    	ltpaCaller.setName("LTPA_Caller");
    	ltpaCaller.setUri(LTPA_URI);    	
    	ltpaCaller.setLocalName("LTPA");
    	return ltpaCaller;    	
    }
    
    private RequiredSecurityToken createUsernameRequiredSecurityToken(boolean usageRequired, String tokenPartReference){
    	RequiredSecurityToken usernameToken = WebSphereFactories.getWscommonextFactory().createRequiredSecurityToken();
		usernameToken.setLocalName(USERNAME_URI);
		usernameToken.setName(tokenPartReference);
		if (usageRequired)
			usernameToken.setUsage(UsageType.REQUIRED_LITERAL);
		return usernameToken;
    }
    
    private Caller createUsernameCaller(){
    	Caller usernameCaller = WebSphereFactories.getWscommonextFactory().createCaller();
    	usernameCaller.setName("UsernameTokenCaller");
    	usernameCaller.setLocalName(USERNAME_URI);    	
    	return usernameCaller;    	
    }        
}
