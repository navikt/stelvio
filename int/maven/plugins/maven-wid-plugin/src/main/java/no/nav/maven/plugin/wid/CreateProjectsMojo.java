package no.nav.maven.plugin.wid;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.InvalidRepositoryException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;

/**
 * Generates the wid configuration files.
 * 
 * @author <a href="mailto:test@example.com">Erik Godding Boye</a>
 * @goal create-projects
 * @requiresDependencyResolution compile
 * @execute goal="wid"
 * @aggregator
 */
public class CreateProjectsMojo extends AbstractMojo {
	private static final String PACKAGING_WPS_MODULE_EAR = "wps-module-ear";
	private static final String PACKAGING_WPS_LIBRARY_JAR = "wps-library-jar";

	/**
	 * Artifact repository factory component.
	 * 
	 * @component
	 * @readonly
	 * @required
	 */
	private ArtifactRepositoryFactory artifactRepositoryFactory;

	/**
	 * Artifact factory, needed to create artifacts.
	 * 
	 * @component
	 * @readonly
	 * @required
	 */
	private ArtifactFactory artifactFactory;

	/**
	 * @component
	 * @readonly
	 * @required
	 */
	private ArtifactResolver artifactResolver;

	/**
	 * @component roleHint="zip"
	 */
	private UnArchiver unarchiver;

	/**
	 * @component
	 */
	private Invoker invoker;

	/**
	 * The Maven session.
	 * 
	 * @parameter expression="${session}"
	 * @readonly
	 * @required
	 */
	private MavenSession mavenSession;

	/**
	 * @parameter expression="${reactorProjects}"
	 * @required
	 * @readonly
	 */
	private Collection<MavenProject> reactorProjects;

	/**
	 * The remote repositories used as specified in your POM.
	 * 
	 * @parameter expression="${project.repositories}"
	 * @readonly
	 * @required
	 */
	private List repositories;

	/**
	 * The local repository taken from Maven's runtime. Typically $HOME/.m2/repository.
	 * 
	 * @parameter expression="${localRepository}"
	 * @readonly
	 * @required
	 */
	private ArtifactRepository localRepository;

	/**
	 * @parameter default-value="${project.basedir}/libraries"
	 * @required
	 */
	private File dependencyProjectsDirectory;

	public void execute() throws MojoExecutionException, MojoFailureException {
		Collection<String> reactorProjectIds = new HashSet<String>(reactorProjects.size());
		for (MavenProject reactorProject : reactorProjects) {
			reactorProjectIds.add(reactorProject.getId());
		}

		// Using a HashSet to eliminate any duplicate artifacts (relying on Artifact.hashCode)
		Collection<Artifact> dependencyArtifacts = new HashSet<Artifact>();
		for (MavenProject reactorProject : reactorProjects) {
			String packaging = reactorProject.getPackaging();
			if (PACKAGING_WPS_MODULE_EAR.equals(packaging) || PACKAGING_WPS_LIBRARY_JAR.equals(packaging)) {
				for (Artifact dependencyArtifact : (Collection<Artifact>) reactorProject.getArtifacts()) {
					// TODO: For now we only support dependencies of type wps-library-jar
					if (PACKAGING_WPS_LIBRARY_JAR.equals(dependencyArtifact.getType())) {
						String dependencyArtifactId = dependencyArtifact.getId();
						// Only add dependency artifacts that are not part of the reactor
						if (!reactorProjectIds.contains(dependencyArtifactId)) {
							dependencyArtifacts.add(dependencyArtifact);
						}
					}
				}
			}
		}

		List remoteRepos = buildRemoteRepositories();

		for (Artifact dependencyArtifact : dependencyArtifacts) {
			createProject(remoteRepos, dependencyArtifact);
		}
	}

	private List buildRemoteRepositories() throws MojoExecutionException {
		try {
			return ProjectUtils.buildArtifactRepositories(repositories, artifactRepositoryFactory, mavenSession.getContainer());
		} catch (InvalidRepositoryException e) {
			throw new MojoExecutionException("Error building remote repositories", e);
		}
	}

	private void createProject(List remoteRepos, Artifact artifact) throws MojoExecutionException {
		Artifact sourceArtifact = artifactFactory.createArtifactWithClassifier(artifact.getGroupId(), artifact.getArtifactId(),
				artifact.getVersion(), artifact.getType(), "sources");
		sourceArtifact = resolveArtifact(remoteRepos, sourceArtifact);
		File extractDirectory = extractArtifact(sourceArtifact);
		invokeWidPlugin(extractDirectory);
	}

	private void invokeWidPlugin(File baseDirectory) throws MojoExecutionException {
		try {
			InvocationRequest request = new DefaultInvocationRequest();
			request.setBaseDirectory(baseDirectory);
			request.setGoals(Collections.singletonList("wid:wid"));

			invoker.execute(request);
		} catch (MavenInvocationException e) {
			throw new MojoExecutionException("Error invoking Maven", e);
		}
	}

	@SuppressWarnings("unchecked")
	public Artifact resolveArtifact(List remoteRepos, Artifact artifact) throws MojoExecutionException {
		try {
			artifactResolver.resolve(artifact, remoteRepos, localRepository);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException("Error downloading wsdl artifact.", e);
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException("Resource can not be found.", e);
		}

		return artifact;
	}

	private File extractArtifact(Artifact artifact) throws MojoExecutionException {
		try {
			File extractDirectory = new File(dependencyProjectsDirectory, getExtractDirectoryPath(artifact));
			extractDirectory.mkdirs();
			unarchiver.setDestDirectory(extractDirectory);
			unarchiver.setSourceFile(artifact.getFile());
			unarchiver.extract();
			return extractDirectory;
		} catch (ArchiverException e) {
			throw new MojoExecutionException("Error extracting archive", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error extracting archive", e);
		}
	}

	private String getExtractDirectoryPath(Artifact artifact) {
		StringBuilder extractDirectoryPath = new StringBuilder();
		extractDirectoryPath.append(artifact.getGroupId()).append(File.separator);
		extractDirectoryPath.append(artifact.getArtifactId()).append(File.separator);
		extractDirectoryPath.append(artifact.getVersion());
		return extractDirectoryPath.toString();
	}
}
