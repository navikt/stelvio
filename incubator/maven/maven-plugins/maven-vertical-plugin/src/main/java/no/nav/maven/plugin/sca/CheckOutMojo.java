package no.nav.maven.plugin.sca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
 * @requiresDependencyResolution
 */
@SuppressWarnings("unchecked")
public class CheckOutMojo extends AbstractMojo {
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @component
	 */
	private MavenProjectBuilder projectBuilder;

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
	private ScmManager scmManager;

	/**
	 * {@inheritDoc}
	 */
	public void execute() throws MojoExecutionException {
		Collection<MavenProject> projects = getProjects();
		checkOut(projects);
	}

	private void checkOut(Collection<MavenProject> projects) throws MojoExecutionException {
		try {
			File parentCheckOutDir = getParentCheckOutDir();
			for (MavenProject p : projects) {
				ScmRepository scmRepository = scmManager.makeScmRepository(p.getScm().getDeveloperConnection());
				File checkoutDir = new File(parentCheckOutDir, p.getArtifactId());
				checkoutDir.mkdirs();
				scmManager.checkOut(scmRepository, new ScmFileSet(checkoutDir));
			}
		} catch (ScmException e) {
			throw new MojoExecutionException("Error checking out projects", e);
		}
	}

	private File getParentCheckOutDir() throws MojoExecutionException {
		return new File(project.getBasedir(), "workspace");
	}

	private Collection<MavenProject> getProjects() throws MojoExecutionException {
		try {
			Collection<MavenProject> projects = new ArrayList<MavenProject>();
			Set<Artifact> artifacts = project.getArtifacts();
			for (Artifact artifact : artifacts) {
				projects.add(projectBuilder.buildFromRepository(artifact, remoteArtifactRepositories, localRepository));
			}
			return projects;
		} catch (ProjectBuildingException e) {
			throw new MojoExecutionException("Error building projects from repository", e);
		}
	}
}
