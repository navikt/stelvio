package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.File;
import java.util.List;

import no.nav.maven.plugin.artifact.modifier.utils.EarFile;
import no.nav.maven.plugin.artifact.modifier.utils.InboundWSSecurity;
import no.nav.maven.plugin.artifact.modifier.utils.OutboundWSSecurity;
import no.nav.maven.plugin.artifact.modifier.utils.RoleSecurity;
import no.nav.pensjonsprogrammet.wpsconfiguration.AuthenticationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.AuthorizationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.InboundType;
import no.nav.pensjonsprogrammet.wpsconfiguration.OutboundType;
import no.nav.pensjonsprogrammet.wpsconfiguration.RolesType;
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

	protected final void applyConfiguration(File artifact, ConfigurationType configuration) {
		if(configuration.getSecurity() != null) {
			EARFile earFile = EarFile.openEarFile(artifact.getAbsolutePath());
			updateSecurity(earFile, configuration.getSecurity());
			EarFile.closeEarFile(earFile);
		}
	}
	
	private final void updateSecurity(final EARFile earFile, final SecurityType securityConfiguration) {
		AuthenticationType authentication = securityConfiguration.getAuthentication();
		if(authentication != null) {
			updateAuthentication((EJBJarFile)earFile.getEJBJarFiles().get(0), authentication);
		}
		
		AuthorizationType authorization = securityConfiguration.getAuthorization();
		if(authorization != null) {
			updateAuthorization( earFile, authorization);
		}
	}
	
	private final void updateAuthentication(final Archive ejbFile, final AuthenticationType authentication) {
		InboundType inbound = authentication.getInbound();
		if(inbound != null) {
			updateInboundAuthentication(ejbFile, inbound);
		}
		
		OutboundType outbound = authentication.getOutbound();
		if(outbound != null) {
			updateOutboundAuthentication(ejbFile, outbound);	
		}
	}

	private final void updateOutboundAuthentication(final Archive ejbFile, final OutboundType outbound) {
		TokensType tokens = outbound.getTokens();
		if(tokens != null) {
			OutboundWSSecurity.injectTokens(tokens, ejbFile);
		}
	}

	private final void updateInboundAuthentication(final Archive ejbFile, final InboundType inbound) {
		TokensType tokens = inbound.getTokens();
		if(tokens != null) {
			InboundWSSecurity.injectTokens(tokens, ejbFile);
		}
	}
	
	private final void updateAuthorization(final EARFile earFile, final AuthorizationType authorization) {
		
		RolesType rolesInConfig = authorization.getRoles();
		if(rolesInConfig != null && rolesInConfig.sizeOfRoleArray() > 0) {
			/* TODO: Check if it is enough to use the "getSecurityRoles" from the ibm assembly descriptor java classes */
			List<String> rolesInEjb = RoleSecurity.getSecurityRoles((EJBJarFile)earFile.getEJBJarFiles().get(0));
			if(rolesInEjb != null && rolesInEjb.size() > 0) {
				RoleSecurity.updateRoleBindings(earFile, rolesInConfig, rolesInEjb);
				RoleSecurity.updateRunAsBindings(earFile, rolesInConfig, rolesInEjb);
			}
		}
	}
}
