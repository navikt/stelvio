package no.nav.maven.plugins.descriptor.mojo;

import no.nav.maven.plugins.descriptor.config.EarConfig;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.OpenFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.ReopenException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.SaveFailureException;

import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
/**
 * @goal addSecurityRoleBindings
 */
public class SecurityRoleBindingMojo extends AbstractDeploymentDescriptorMojo {
	
	public void executeDescriptorOperations(EARFile earFile, EarConfig earConfig) throws MojoExecutionException {
		addSecurityRoleBindings(earFile, earConfig);
	}

}
