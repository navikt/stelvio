package no.nav.maven.plugin.artifact.modifier.utils;

import java.io.IOException;

import no.nav.maven.commons.constants.Constants;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmWebServiceClientBndEditor;
import no.nav.maven.plugins.descriptor.websphere.ext.IbmWebServiceClientExtEditor;
import no.nav.pensjonsprogrammet.wpsconfiguration.TokenType;
import no.nav.pensjonsprogrammet.wpsconfiguration.TokensType;

import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;

/** 
 * @author test@example.com 
 */
public final class OutboundWSSecurity {

	public static void injectTokens(TokensType tokens, Archive archive) {
		if((tokens.getTokenList() == null) || (tokens.getTokenList().size() == 0)) {
			return;
		}
		
		for(TokenType t : tokens.getTokenList()) {
			if(Constants.AUTH_WSS_LTPATOKEN.equals(t.getName())) {
				setupLtpaTokenGenerator(archive);
			} else if(Constants.AUTH_WSS_USERNAMETOKEN.equals(t.getName())) {
				setupUsernameTokenGenerator(archive, t);
			} else if(Constants.AUTH_BASICAUTHENTICATION.equals(t.getName())) {
				setupBasicAuthentication(archive, t);
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

	private static void setupUsernameTokenGenerator(Archive archive, TokenType token) {
		try {
			String tokenPartReference = "UsernameTokenPartRef";
			IbmWebServiceClientExtEditor wscExt = new IbmWebServiceClientExtEditor(archive);
			wscExt.addRequestGeneratorUsername(tokenPartReference);
			wscExt.save();
			IbmWebServiceClientBndEditor wscBnd = new IbmWebServiceClientBndEditor(archive);
			wscBnd.addRequestTokenGeneratorUsername(tokenPartReference, token.getUsername(), token.getPassword());
			wscBnd.save();
		} catch (IOException e) {
			throw new RuntimeException("An error occured injecting Username Token generator", e);
		}		
	}

	private static void setupBasicAuthentication(Archive archive, TokenType token) {
		try {
			IbmWebServiceClientBndEditor wscBnd = new IbmWebServiceClientBndEditor(archive);
			wscBnd.addBasicAuth(token.getUsername(), token.getPassword());
			wscBnd.save();
		} catch (IOException e) {
			throw new RuntimeException("An error occured injecting Username Token generator", e);
		}		
	}
}
