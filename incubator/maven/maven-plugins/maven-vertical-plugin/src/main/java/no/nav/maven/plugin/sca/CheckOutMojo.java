package no.nav.maven.plugin.sca;

import java.util.Collection;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.scm.ScmException;

/**
 * @author test@example.com
 * 
 * @goal checkout
 * @requiresDependencyResolution compile
 */
@SuppressWarnings("unchecked")
public class CheckOutMojo extends VerticalMojo {
	protected void execute(Collection<ScmProject> scmProjects) throws MojoExecutionException {
		try {
			for (ScmProject scmProject : scmProjects) {
				scmProject.getScmFileSet().getBasedir().mkdirs();
				scmManager.checkOut(scmProject.getScmRepository(), scmProject.getScmFileSet());
			}
		} catch (ScmException e) {
			throw new MojoExecutionException("Error checking out projects", e);
		}
	}
}
