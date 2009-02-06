package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import no.nav.busconfiguration.configuration.ArtifactConfiguration;
import no.nav.busconfiguration.constants.Constants;
import no.nav.maven.plugin.artifact.modifier.utils.EarFile;
import no.nav.maven.plugin.artifact.modifier.utils.InboundSecurity;
import no.nav.maven.plugin.artifact.modifier.utils.OutboundSecurity;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmWebServiceClientBndEditor;
import no.nav.pensjonsprogrammet.wpsconfiguration.AuthenticationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.EndpointType;
import no.nav.pensjonsprogrammet.wpsconfiguration.EndpointsType;
import no.nav.pensjonsprogrammet.wpsconfiguration.InboundType;
import no.nav.pensjonsprogrammet.wpsconfiguration.OutboundType;
import no.nav.pensjonsprogrammet.wpsconfiguration.SecurityType;
import no.nav.pensjonsprogrammet.wpsconfiguration.TokenType;
import no.nav.pensjonsprogrammet.wpsconfiguration.TokensType;
import no.nav.pensjonsprogrammet.wpsconfiguration.WebservicesType;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EJBJarFile;

/**
 * Goal that updates the security settings in the artifacts
 * 
 * @author test@example.com
 * 
 * @goal modify-security-configuration
 * @requiresDependencyResolution
 */
public class ModifySecurityConfigurationMojo extends ArtifactModifierMojo {

	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
				
		for(Artifact a : artifacts) {
			if(a.getType().equals(Constants.EAR_ARTIFACT_TYPE)) {
				ConfigurationType configuration = ArtifactConfiguration.getConfiguration(a.getArtifactId());
				if(configuration != null && configuration.getSecurity() != null) {
					EARFile earFile = EarFile.openEarFile(a.getFile().getAbsolutePath());
					updateSecurity((EJBJarFile)earFile.getEJBJarFiles().get(0), configuration.getSecurity());
					EarFile.closeEarFile(earFile);
				}
			}
		}
	}
	
	private final void updateSecurity(final Archive ejbFile, final SecurityType securityConfiguration) {
		AuthenticationType authentication = securityConfiguration.getAuthentication();
		if(authentication != null) {
			updateAuthentication(ejbFile, authentication);
		}
	}
	
	private final void updateAuthentication(final Archive ejbFile, final AuthenticationType authentication) {
		
		InboundType inbound = authentication.getInbound();
		if(inbound != null) {
			updateInboundAuthentication(ejbFile, inbound);
		}
		
		OutboundType outbound = authentication.getOutbound();
		if(inbound != null) {
			updateOutboundAuthentication(ejbFile, outbound);	
		}
	}

	private final void updateOutboundAuthentication(final Archive ejbFile, final OutboundType outbound) {
		TokensType tokens = outbound.getTokens();
		if(tokens != null) {
			OutboundSecurity.injectTokens(tokens, ejbFile);
		}
	}

	private final void updateInboundAuthentication(final Archive ejbFile, final InboundType inbound) {
		TokensType tokens = inbound.getTokens();
		if(tokens != null) {
			InboundSecurity.injectTokens(tokens, ejbFile);
		}
	}
}
