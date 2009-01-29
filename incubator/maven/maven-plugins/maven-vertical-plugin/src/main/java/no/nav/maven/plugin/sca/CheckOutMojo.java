package no.nav.maven.plugin.sca;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;

/**
 * @author test@example.com
 * 
 * @goal checkout
 * @requiresDependencyResolution compile
 */
@SuppressWarnings("unchecked")
public class CheckOutMojo extends AbstractMojo {
	private static final Collection<String> SUPPORTED_PACKAGINGS = new HashSet<String>(Arrays.asList(new String[] {
			"sca-module-ear", "sca-library-jar" }));

	/**
	 * The working directory
	 * 
	 * @parameter default-value="${project.basedir}"
	 */
	private File workingDirectory;

	/**
	 * @parameter expression="${project.artifacts}"
	 * @required
	 * @readonly
	 */
	private Collection<Artifact> artifacts;

	/**
	 * @parameter expression="${project.remoteArtifactRepositories}"
	 * @required
	 * @readonly
	 */
	private List remoteArtifactRepositories;

	/**
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 */
	private ArtifactRepository localRepository;

	/**
	 * @component
	 */
	private MavenProjectBuilder projectBuilder;

	/**
	 * @component
	 */
	private ScmManager scmManager;

	/**
	 * {@inheritDoc}
	 */
	public void execute() throws MojoExecutionException {
		Collection<MavenProject> projects = getProjects();
		Collection<ScmProject> scmProjects = new ArrayList<ScmProject>(projects.size());
		for (MavenProject project : projects) {
			String packaging = project.getPackaging();
			if (SUPPORTED_PACKAGINGS.contains(packaging)) {
				ScmRepository scmRepository = getScmRepository(project.getScm().getDeveloperConnection());
				File projectWorkingDirectory = new File(workingDirectory, project.getArtifactId());
				ScmFileSet scmFileSet = new ScmFileSet(projectWorkingDirectory);
				scmProjects.add(new ScmProject(scmRepository, scmFileSet));
			}
		}
		checkOut(scmProjects);
	}

	private ScmRepository getScmRepository(String scmUrl) throws MojoExecutionException {
		try {
			return scmManager.makeScmRepository(scmUrl);
		} catch (ScmException e) {
			throw new MojoExecutionException("Error creating SCM repository", e);
		}
	}

	private void checkOut(Collection<ScmProject> scmProjects) throws MojoExecutionException {
		try {
			for (ScmProject scmProject : scmProjects) {
				scmProject.getScmFileSet().getBasedir().mkdirs();
				scmManager.checkOut(scmProject.getScmRepository(), scmProject.getScmFileSet());
			}
		} catch (ScmException e) {
			throw new MojoExecutionException("Error checking out projects", e);
		}
	}

	private Collection<MavenProject> getProjects() throws MojoExecutionException {
		try {
			Collection<MavenProject> projects = new ArrayList<MavenProject>();
			for (Artifact artifact : artifacts) {
				projects.add(projectBuilder.buildFromRepository(artifact, remoteArtifactRepositories, localRepository));
			}
			return projects;
		} catch (ProjectBuildingException e) {
			throw new MojoExecutionException("Error building projects from repository", e);
		}
	}
}
