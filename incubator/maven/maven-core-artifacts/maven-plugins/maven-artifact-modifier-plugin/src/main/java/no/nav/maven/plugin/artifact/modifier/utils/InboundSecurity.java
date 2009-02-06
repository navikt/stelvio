package no.nav.maven.plugin.artifact.modifier.utils;

import java.io.IOException;

import no.nav.busconfiguration.constants.Constants;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmWebServiceBndEditor;
import no.nav.maven.plugins.descriptor.websphere.ext.IbmWebServiceExtEditor;
import no.nav.pensjonsprogrammet.wpsconfiguration.TokenType;
import no.nav.pensjonsprogrammet.wpsconfiguration.TokensType;

import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;

public class InboundSecurity {

	public static void injectTokens(TokensType tokens, Archive archive) {
		if(tokens.getTokenList() == null || tokens.getTokenList().size() == 0) {
			return;
		}
		
		if(tokens.getTokenList().size()>1) {
			setupBothTokenConsumer(archive);
		}
		
		if(tokens.getTokenList().get(0).equals(Constants.AUTH_WSS_LTPATOKEN)) {
			setupLtpaTokenConsumer(archive);
		} else if(tokens.getTokenList().get(0).equals(Constants.AUTH_WSS_USERNAMETOKEN)) {
			setupUsernameTokenConsumer(archive);
		}
	}
	
	public static void setupLtpaTokenConsumer(Archive archive) {
		try{
			String tokenPartReference = "RequiredLTPAToken";
			boolean usageRequired = true; 
			IbmWebServiceExtEditor wsExt = new IbmWebServiceExtEditor(archive);
			wsExt.deleteExistingConfig();			
			wsExt.addRequestConsumerLTPAToken(usageRequired, tokenPartReference); 
			wsExt.save();
			
			IbmWebServiceBndEditor wsBnd = new IbmWebServiceBndEditor(archive);
			wsBnd.deleteExistingConfig();
			wsBnd.addLTPATokenConsumer(tokenPartReference);
			wsBnd.save();
		}catch (IOException e){
			throw new RuntimeException("Caught IOException while setting up LTPATokenConsumer", e);
		}	
	}
	
	public static void setupUsernameTokenConsumer(Archive archive) {
		try{
			String tokenPartReference = "RequiredUsernameToken";
			boolean usageRequired = true;
			IbmWebServiceExtEditor wsExt = new IbmWebServiceExtEditor(archive);
			wsExt.deleteExistingConfig();			
			wsExt.addRequestConsumerUsernameToken(usageRequired, tokenPartReference); 
			wsExt.save();
			
			IbmWebServiceBndEditor wsBnd = new IbmWebServiceBndEditor(archive);
			wsBnd.deleteExistingConfig();
			wsBnd.addUsernameTokenConsumer(tokenPartReference);			
			wsBnd.save();
		}catch (IOException e){
			throw new RuntimeException("Caught IOException while setting up UsernameTokenConsumer", e);
		}	
	}
	
	private static void setupBothTokenConsumer(Archive archive) {
		try{
			String usernamePartRef = "OptionalUsernameToken";
			String ltpaPartRef = "OptionalLTPAToken";
			boolean usageRequired = false; 
			IbmWebServiceExtEditor wsExt = new IbmWebServiceExtEditor(archive);			
			wsExt.deleteExistingConfig();
			wsExt.addRequestConsumerLTPAToken(usageRequired, ltpaPartRef);
			wsExt.addRequestConsumerUsernameToken(usageRequired, usernamePartRef); 
			wsExt.save();
			
			IbmWebServiceBndEditor wsBnd = new IbmWebServiceBndEditor(archive);
			wsBnd.deleteExistingConfig();
			wsBnd.addLTPATokenConsumer(ltpaPartRef);
			wsBnd.addUsernameTokenConsumer(usernamePartRef);			
			wsBnd.save();
		}catch (IOException e){
			throw new RuntimeException("Caught IOException while setting up optional username and ltpa token consumers", e);
		}	
	}
}
