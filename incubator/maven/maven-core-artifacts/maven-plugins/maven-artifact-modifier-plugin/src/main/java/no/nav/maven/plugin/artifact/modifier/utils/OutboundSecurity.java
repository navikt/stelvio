package no.nav.maven.plugin.artifact.modifier.utils;

import java.io.IOException;

import no.nav.busconfiguration.constants.Constants;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmWebServiceBndEditor;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmWebServiceClientBndEditor;
import no.nav.maven.plugins.descriptor.websphere.ext.IbmWebServiceClientExtEditor;
import no.nav.maven.plugins.descriptor.websphere.ext.IbmWebServiceExtEditor;
import no.nav.pensjonsprogrammet.wpsconfiguration.TokenType;
import no.nav.pensjonsprogrammet.wpsconfiguration.TokensType;

import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;

public class OutboundSecurity {

	public static void injectTokens(TokensType tokens, Archive archive) {
		if(tokens.getTokenList() == null || tokens.getTokenList().size() == 0) {
			return;
		}
		
		for(TokenType t : tokens.getTokenList()) {
			if(tokens.getTokenList().get(0).equals(Constants.AUTH_WSS_LTPATOKEN)) {
				setupLtpaTokenGenerator(archive);
			} else if(tokens.getTokenList().get(0).equals(Constants.AUTH_WSS_USERNAMETOKEN)) {
				//TODO: To be implemented
			}
		}
	}
	
	private static void setupLtpaTokenGenerator(Archive archive) {
		try {
			String tokenPartReference = "LTPATokenPartRef";
			IbmWebServiceClientExtEditor wscExt = new IbmWebServiceClientExtEditor(archive);
			wscExt.addRequestGeneratorLTPA(tokenPartReference);
			wscExt.save();
			IbmWebServiceClientBndEditor wscBnd = new IbmWebServiceClientBndEditor(archive);
			wscBnd.addRequestTokenGeneratorLTPA(tokenPartReference);
			wscBnd.save();
		} catch (IOException e) {
			throw new RuntimeException("An error occured injecting LTPA Token generator", e);
		}		
	}
}
