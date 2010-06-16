package no.nav.maven.plugin.sca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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

public abstract class VerticalMojo extends AbstractMojo {
	/**
	 * The working directory.
	 * 
	 * @parameter expression="${workingDirectory}"
	 *            default-value="${project.basedir}"
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
	protected ScmManager scmManager;

	/**
	 * @parameter expression="${project}"
	 */
	protected MavenProject project;

	/**
	 * @parameter
	 * @required
	 */
	protected String stelvioCommonsLibSCMURL;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException {
		getLog().info("stelvioCommonsLibSCMURL" + stelvioCommonsLibSCMURL);

		Collection<MavenProject> projects = getProjects();
		StringBuilder projectNames = new StringBuilder();
		for (MavenProject project : projects) {
			projectNames.append(project.getGroupId() + ":" + project.getArtifactId() + "  ");
		}

		List<String> modules = project.getModules();
		getLog().info("Resolved " + modules.size() + " modules");
		getLog().info("Resolved the following modules " + modules.toString());

		String baseRepository = project.getScm().getDeveloperConnection().concat("/../../layers");

		Collection<ScmProject> scmProjects = new ArrayList<ScmProject>(projects.size());
		for (String moduleName : modules) {
			getLog().info("Working with " + moduleName);

			File projectDirectory = new File(workingDirectory, moduleName);
			String repositoryLocation = null;

			if (moduleName.contains("stelvio-commons-lib")) {
				// Use alternate repository
				repositoryLocation = stelvioCommonsLibSCMURL;
				// Override the output folder for Stelvio Commons Lib
				projectDirectory = new File(workingDirectory + File.separator + "libs", "stelvio-commons-lib");
			} else {
				repositoryLocation = baseRepository.concat("/" + moduleName);
				getLog().info("repositoryLocation for " + moduleName + " is " + repositoryLocation);
			}

			ScmFileSet scmFileSet = new ScmFileSet(projectDirectory);
			ScmRepository scmRepository = getScmRepository(repositoryLocation);
			ScmProject scmProject = new ScmProject(scmRepository, scmFileSet);
			scmProjects.add(scmProject);
		}

		try {
			execute(scmProjects);
		} catch (ScmException e) {
			throw new MojoExecutionException("Error running SCM command", e);
		}
	}

	private ScmRepository getScmRepository(String scmUrl) throws MojoExecutionException {
		try {
			return scmManager.makeScmRepository(scmUrl);
		} catch (ScmException e) {
			throw new MojoExecutionException("Error creating SCM repository", e);
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

	protected abstract void execute(Collection<ScmProject> scmProjects) throws ScmException, MojoExecutionException;
}