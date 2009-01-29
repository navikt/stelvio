package no.nav.maven.plugin.sca;

import java.io.File;
import java.util.Collection;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.status.StatusScmResult;

/**
 * @author test@example.com
 * 
 * @goal clean
 * @requiresDependencyResolution compile
 */
@SuppressWarnings("unchecked")
public class CleanMojo extends VerticalMojo {
	protected void execute(Collection<ScmProject> scmProjects) throws ScmException, MojoExecutionException {
		validate(scmProjects);
		for (ScmProject scmProject : scmProjects) {
			File basedir = scmProject.getScmFileSet().getBasedir();
			deleteDirectory(basedir);
		}
	}

	private void validate(Collection<ScmProject> scmProjects) throws ScmException, MojoExecutionException {
		for (ScmProject scmProject : scmProjects) {
			File basedir = scmProject.getScmFileSet().getBasedir();
			if (basedir.exists()) {
				StatusScmResult statusScmResult = scmManager.status(scmProject.getScmRepository(), scmProject.getScmFileSet());
				if (!statusScmResult.getChangedFiles().isEmpty()) {
					throw new MojoExecutionException("You have local modifications. Cannot clean...");
				}
			}
		}
	}

	private void deleteDirectory(File directory) {
		if (directory.exists()) {
			for (File file : directory.listFiles()) {
				if (file.isDirectory()) {
					deleteDirectory(file);
				} else {
					file.delete();
				}
			}
			directory.delete();
		}
	}
}
