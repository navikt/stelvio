package no.nav.maven.plugin.sca;

import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.repository.ScmRepository;

public class ScmProject {
	private ScmRepository scmRepository;
	private ScmFileSet scmFileSet;

	public ScmProject(ScmRepository scmRepository, ScmFileSet scmFileSet) {
		this.scmRepository = scmRepository;
		this.scmFileSet = scmFileSet;
	}

	public ScmRepository getScmRepository() {
		return scmRepository;
	}

	public ScmFileSet getScmFileSet() {
		return scmFileSet;
	}
}
