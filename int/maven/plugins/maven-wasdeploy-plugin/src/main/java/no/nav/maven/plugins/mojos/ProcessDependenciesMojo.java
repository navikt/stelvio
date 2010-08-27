package no.nav.maven.plugins.mojos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	 * @parameter expression="${mooseBuildId}"
	 */
	private String mooseBuildId;

	/**
	 * @parameter expression="${version}"
	 */
	private String version;

	/**
	 * @parameter expression="${mapFile}"
	 * @required
	 */
	private String mapFile;

	/**
	 * @parameter expression="${stagingArea}"
	 * @required
	 */
	private String stagingArea;

	private String mavenVersion;

   /**
	* Need to statically set the naming convention of the PSELV WAR file, without version.
	* Full name would be PSELV_WAR_NAME + <VERSION> + ".war", this is only required for PSELV as of now.
	*/
	private final String PSELV_WAR_NAME = "nav-presentation-pensjon-pselv-web-";

	public void execute() throws MojoExecutionException, MojoFailureException {
		
		if (!ApplicationArtifactDependency.isSupported(application))
			throw new MojoFailureException("[ERROR] The application, " + application + " is not supported for deploy/bundle.");
		
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

		// Determine whether to resolve the mooseBuildId or to use the version provided
		if (version != null) {
			mavenVersion = version;
		} else {
			mavenVersion = getMavenVersion(mooseBuildId);
		}

		getLog().info("################################################");
		getLog().info("### Downloading and extracting artifacts ... ###");
		getLog().info("################################################");

		// Retrieving the list of artifacts for the given application and Moose build ID
		List<Artifact> artifacts = ApplicationArtifactDependency.getApplicationArtifacts(artifactFactory, application, mavenVersion);

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
					
					File pselvWAR = new File(extDir.getPath() + "/" + PSELV_WAR_NAME + mavenVersion + ".war");
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

	// *MOOSE SPECIFIC* - Returns the maven version based on the Moose build ID
	private String getMavenVersion(String mooseBuildId) throws MojoFailureException {

		try {

			getLog().info("######################################################");
			getLog().info("### Converting Moose build ID to Maven version ... ###");
			getLog().info("######################################################");

			if (!new File(mapFile).exists()) {
				throw new IOException("[ERROR] The Moose => Maven version mapping " + "file does not exist at the following location: " + mapFile);
			}

			String mavenVersion = null;
			String line;
			BufferedReader r = new BufferedReader(new FileReader(mapFile));

			while ((line = r.readLine()) != null) {
				if (line.startsWith(mooseBuildId)) {
					mavenVersion = line.substring(mooseBuildId.length() + 1);
					getLog().info("Found version match! MOOSE_BUILD_ID:" + mooseBuildId + " => " + " MAVEN_VERSION:" + mavenVersion);
					r.close();
					return mavenVersion;
				}
			}
			if (mavenVersion == null) {
				r.close();
				throw new MojoFailureException("[ERROR] No corresponding Maven version found for Moose build ID: " + mooseBuildId);
			}

		} catch (IOException e) {
			throw new MojoFailureException("[ERROR] Could not resolve Maven version " + e);
		}

		return null;
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
