package no.nav.maven.plugin.sca;

import java.io.File;
import java.util.Collection;

import org.apache.maven.scm.ScmException;

/**
 * @author test@example.com
 * 
 * @goal update
 * @requiresDependencyResolution compile
 */
@SuppressWarnings("unchecked")
public class UpdateMojo extends VerticalMojo {
	protected void execute(Collection<ScmProject> scmProjects) throws ScmException {
		for (ScmProject scmProject : scmProjects) {
			File basedir = scmProject.getScmFileSet().getBasedir();
			if (basedir.exists()) {
				scmManager.update(scmProject.getScmRepository(), scmProject.getScmFileSet());
			}
		}
	}
}
