package no.nav.maven.plugins.mojos;

import java.io.File;
import java.util.List;

import no.nav.maven.plugins.utils.ApplicationArtifactDependency;
import no.nav.maven.plugins.utils.Archiver;

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
import org.codehaus.plexus.archiver.UnArchiver;

/**
 * Goal that resolves and unpacks the dependencies application you are deploying
 * 
 * @goal process-dependencies
 * 
 * @author test@example.com
 * 
 */
public class ProcessDependenciesMojo extends AbstractMojo {

	/**
	 * @parameter expression="${session}"
	 * @readonly
	 * @required
	 */
	private MavenSession mavenSession;

	/**
	 * @parameter expression="${project}"
	 * @readonly
	 * @required
	 */
	private MavenProject project;

	/**
	 * The remote repositories used as specified in your POM.
	 * 
	 * @parameter expression="${project.repositories}"
	 * @readonly
	 * @required
	 */
	private List repositories;

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
	 * @required
	 * @readonly
	 */
	private UnArchiver unArchiver;

	/**
	 * The local repository taken from Maven's runtime. Typically $HOME/.m2/repository.
	 * 
	 * @parameter expression="${localRepository}"
	 * @readonly
	 * @required
	 */
	private ArtifactRepository localRepository;

	/**
	 * @parameter expression="${app}"
	 * @required
	 */
	private String application;

	/**
	 * @parameter expression="${version}"
	 * @required
	 */
	private String version;

	/**
	 * @parameter expression="${stagingArea}"
	 * @required
	 */
	private String stagingArea;
	
	/**
	 * @parameter expression="${zone}"
	 * @required
	 */
	private String zone;
	
	/**
	 * @parameter expression="${hasInternZone}"
	 * @required
	 */
	private boolean hasInternZone;
	

   /**
	* Need to statically set the naming convention of the PSELV WAR file, without version.
	* Full name would be PSELV_WAR_NAME + <VERSION> + ".war", this is only required for PSELV as of now.
	*/
	private final String PSELV_WAR_NAME = "nav-presentation-pensjon-pselv-web-";

	public void execute() throws MojoExecutionException, MojoFailureException {
		
		if (!ApplicationArtifactDependency.isSupported(application))
			throw new MojoFailureException("[ERROR] The application, " + application + " is not supported for deploy/bundle.");
		
		if (!hasInternZone && zone.equals("intern")){
			getLog().info("#########################################################################################");
			getLog().info("### Skipping deployment to intern zone, no definitions found in the environment file. ###");
			getLog().info("#########################################################################################");
			System.exit(10);
		}
		
		List remoteRepos;

		// Instantiate the remote repositories object
		try {
			remoteRepos = ProjectUtils.buildArtifactRepositories(repositories, artifactRepositoryFactory, mavenSession.getContainer());
		} catch (InvalidRepositoryException e) {
			throw new MojoExecutionException("[ERROR] Error building remote repositories", e);
		}

		// Destination for folder extraction
		File stagingDirectory = new File(stagingArea);

		// Make sure the folder structure exists
		stagingDirectory.mkdirs();

		getLog().info("################################################");
		getLog().info("### Downloading and extracting artifacts ... ###");
		getLog().info("################################################");

		// Retrieving the list of artifacts for the given application and version
		List<Artifact> artifacts = ApplicationArtifactDependency.getApplicationArtifacts(artifactFactory, application, version);

		// For each artifact, the artifact is resolved and extracted to the given staging directory
		for (Artifact a : artifacts) {
			Artifact artifact = resolveRemoteArtifact(remoteRepos, a);
			File extDir = Archiver.extractArchive(artifact.getFile(), stagingDirectory, unArchiver);
			String extDirPath = extDir.getPath();

			// We expose the different properties in order to be able to operate on these in the same Maven session
			if (application.equals("psak") || application.equals("pselv")) {

				// Exposing config directory to maven context
				if (extDirPath.contains("nav-config-pensjon-psak") || extDirPath.contains("nav-config-pensjon-pselv")) {
					exposeMavenProperty("configDir", extDirPath);
				}
				// Exposing batch directory to maven context
				if (extDirPath.contains("nav-batch-")) {
					exposeMavenProperty("batchDir", extDirPath);
				}
				// Exposing pen config directory to maven context
				if (extDirPath.contains("nav-config-pensjon-pen")) {
					exposeMavenProperty("penConfigDir", extDirPath);
				}
				// Exposing EAR directory to maven context
				if (extDirPath.contains("-jee-")) {
					exposeMavenProperty("earDir", extDirPath);
				}

				// If the extracted resource is the pselv EAR file, we extract the WAR inside it
				// and expose that directory to the maven context
				if (extDirPath.contains("nav-pensjon-pselv-jee")) {
					
					File pselvWAR = new File(extDir.getPath() + "/" + PSELV_WAR_NAME + version + ".war");
					File pselvWARDir = new File(extDir.getPath());

					// Making sure we have the destination folder
					pselvWARDir.delete();
					pselvWARDir.mkdir();

					String destPath = Archiver.extractArchive(pselvWAR, pselvWARDir, unArchiver).getPath();
					exposeMavenProperty("pselvWARDir", destPath);
				}

			} else {

				// Exposing EAR directory to maven context
				if (extDirPath.contains("-jee-")) {
					exposeMavenProperty("earDir", extDirPath);
				}
				// Exposing config directory to maven context
				if (extDirPath.contains("nav-config-")) {
					exposeMavenProperty("configDir", extDirPath);
				}
				// Exposing batch directory to maven context
				if (extDirPath.contains("nav-batch-")) {
					exposeMavenProperty("batchDir", extDirPath);
				}
			}
		}

	}

	// Resolves the artifact using the given repositories
	public Artifact resolveRemoteArtifact(List remoteRepos, Artifact artifact) throws MojoExecutionException {

		try {
			artifactResolver.resolve(artifact, remoteRepos, localRepository);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException("[ERROR] Error downloading artifact.", e);
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException("[ERROR] Resource can not be found.", e);
		}

		return artifact;
	}

	public void exposeMavenProperty(String key, String value) {
		getLog().info("EXPOSED MAVEN PROPERTY: " + key + " = " + value);
		project.getProperties().put(key, value);
	}

}
