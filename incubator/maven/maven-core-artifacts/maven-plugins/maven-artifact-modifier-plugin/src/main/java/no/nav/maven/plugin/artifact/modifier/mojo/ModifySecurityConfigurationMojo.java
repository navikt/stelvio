package no.nav.maven.plugin.artifact.modifier.mojo;

import no.nav.maven.plugin.artifact.modifier.utils.EarFile;
import no.nav.maven.plugin.artifact.modifier.utils.InboundSecurity;
import no.nav.maven.plugin.artifact.modifier.utils.OutboundSecurity;
import no.nav.pensjonsprogrammet.wpsconfiguration.AuthenticationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.InboundType;
import no.nav.pensjonsprogrammet.wpsconfiguration.OutboundType;
import no.nav.pensjonsprogrammet.wpsconfiguration.SecurityType;
import no.nav.pensjonsprogrammet.wpsconfiguration.TokensType;

import org.apache.maven.artifact.Artifact;
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
public class ModifySecurityConfigurationMojo extends ArtifactModifierConfigurerMojo {

	protected final void applyConfiguration(Artifact artifact, ConfigurationType configuration) {
		if(configuration.getSecurity() != null) {
			EARFile earFile = EarFile.openEarFile(artifact.getFile().getAbsolutePath());
			updateSecurity((EJBJarFile)earFile.getEJBJarFiles().get(0), configuration.getSecurity());
			EarFile.closeEarFile(earFile);
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
