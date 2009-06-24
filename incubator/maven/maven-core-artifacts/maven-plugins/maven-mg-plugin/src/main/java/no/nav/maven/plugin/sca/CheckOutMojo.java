package no.nav.maven.plugin.sca;

import java.io.File;
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
	protected void execute(Collection<ScmProject> scmProjects) throws ScmException, MojoExecutionException {
		validate(scmProjects);
		for (ScmProject scmProject : scmProjects) {
			File basedir = scmProject.getScmFileSet().getBasedir();
			basedir.mkdirs();
			scmManager.checkOut(scmProject.getScmRepository(), scmProject.getScmFileSet());
		}
	}

	private void validate(Collection<ScmProject> scmProjects) throws ScmException, MojoExecutionException {
		for (ScmProject scmProject : scmProjects) {
			File basedir = scmProject.getScmFileSet().getBasedir();
			if (basedir.exists()) {
				getLog().warn(basedir.getName() + " already exists, and will not be updated. Please update with your SVN client");
//				throw new MojoExecutionException(basedir.getAbsolutePath() + " already exists, please run clean first...");
			}
		}
	}
}
