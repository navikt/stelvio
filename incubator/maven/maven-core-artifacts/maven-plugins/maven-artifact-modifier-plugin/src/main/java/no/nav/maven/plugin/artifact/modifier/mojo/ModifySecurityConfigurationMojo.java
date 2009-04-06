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

import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EJBJarFile;

/**
 * Goal that updates the security settings in and artifact based on the configuration.
 * 
 * @author test@example.com
 * 
 * @goal modify-security-configuration
 * @requiresDependencyResolution
 */
public final class ModifySecurityConfigurationMojo extends ArtifactModifierConfigurerMojo {
	
	/**
	 * Method called in order to inject security configuration into one artifact. A preprocessor
	 * will copy the artifact from the local repository to the target directory of this project before modifying
	 * the configuration.
	 *
	 * @param artifact The artifact in question
	 * @param configuration This contains the configuration for the artifact built by xmlbeans
	 */
	protected final void applyConfiguration(File artifact, ConfigurationType configuration) {
		if(configuration.getSecurity() != null) {
			EARFile earFile = EarFile.openEarFile(artifact.getAbsolutePath());
			updateSecurity(earFile, configuration.getSecurity());
			EarFile.closeEarFile(earFile);
		}
	}
	
	/**
	 * Method that will parse the configuration and apply authentication and authorization elements
	 * to teh artifact if these configuration elements exist.
	 *
	 * @param earFile An EARFile object
	 * @param securityConfiguration The security configuration for the artifact
	 */
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
	
	/**
	 * Method that will parse the configuration and apply authentication elements
	 * to the artifact if these configuration elements exist. It will parse the authentication and apply 
	 * inbound and outbound security settings.
	 *
	 * @param ejbFile An archive object
	 * @param authentication The authentication configuration for this artifact.
	 */
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

	/**
	 * Method that will update the outbound authentication of the artifact
	 *
	 * @param ejbFile An archive object
	 * @param outbound The outbound authentication configuration for this artifact.
	 */
	private final void updateOutboundAuthentication(final Archive ejbFile, final OutboundType outbound) {
		TokensType tokens = outbound.getTokens();
		if(tokens != null) {
			OutboundWSSecurity.injectTokens(tokens, ejbFile);
		}
	}

	/**
	 * Method that will update the inbound authentication of the artifact
	 *
	 * @param ejbFile An archive object
	 * @param outbound The inbound authentication configuration for this artifact.
	 */
	private final void updateInboundAuthentication(final Archive ejbFile, final InboundType inbound) {
		TokensType tokens = inbound.getTokens();
		if(tokens != null) {
			InboundWSSecurity.injectTokens(tokens, ejbFile);
		}
	}
	
	/**
	 * Method that will update the authorization elements of the artifact. The method will throw an exception
	 * if the module exposes web services, but no security roles are configured.
	 *
	 * @param earFile An EARFile object
	 * @param authorization The authorization configuration for this artifact.
	 */
	private final void updateAuthorization(final EARFile earFile, final AuthorizationType authorization) {
		
		RolesType rolesInConfig = authorization.getRoles();
		if(rolesInConfig != null && rolesInConfig.sizeOfRoleArray() > 0) {
			List<String> rolesInEjb = RoleSecurity.getSecurityRoles((EJBJarFile)earFile.getEJBJarFiles().get(0));
			if(rolesInEjb != null && rolesInEjb.size() > 0) {
				RoleSecurity.updateRoleBindings(earFile, rolesInConfig, rolesInEjb);
				RoleSecurity.updateRunAsBindings(earFile, rolesInConfig, rolesInEjb);
			} else {
				if(InboundWSSecurity.isExposingWebServices((EJBJarFile)earFile.getEJBJarFiles().get(0)) == true) {
					throw new RuntimeException("The ejb exposes web services but no security roles are configured!");
				}
			}
		}
	}
}
