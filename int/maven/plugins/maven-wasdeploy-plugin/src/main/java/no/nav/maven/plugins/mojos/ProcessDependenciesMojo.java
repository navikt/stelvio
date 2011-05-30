package no.nav.maven.plugins.mojos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import no.nav.devarch.utils.Application;
import no.nav.devarch.utils.ApplicationConfig;
import no.nav.maven.plugins.utils.Archiver;
import no.nav.maven.plugins.utils.NativeOps;

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
import org.xml.sax.SAXException;

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
	 * @parameter expression="${applicationConfig}"
	 * @required
	 */
	private String applicationConfig;
	
	/**
	 * @parameter expression="${environment/satstabell}"
	 * @required
	 */
	private String satstabell_classifier;
	
	/**
	 * This parameter is supposed to decide the version of blaze artifact that is used during deploy.
	 * If not given any value from config, the latest released version is used.
	 * 
	 * @parameter expression="${environment/blaze-version}" default-value="RELEASE"
	 */
	private String blaze_version;

	/**
	 * Need to statically set the naming convention of the PSELV WAR file, without version. Full name would be PSELV_WAR_NAME +
	 * <VERSION> + ".war", this is only required for PSELV as of now.
	 */
	private final String PSELV_WAR_NAME = "nav-presentation-pensjon-pselv-web-";
	private static final String BLASE_ARTIFACT_ID = "nav-service-pensjon-blaze";
	
	public void execute() throws MojoExecutionException, MojoFailureException {

		try {

			if (!isSupported(applicationConfig, application)) {
				throw new MojoFailureException("[ERROR] The application, " + application
						+ " is not supported for deploy/bundle.");
			}

			if (!hasInternZone && zone.equals("intern")) {
				getLog().info("#########################################################################################");
				getLog().info("### Skipping deployment to intern zone, no definitions found in the environment file. ###");
				getLog().info("#########################################################################################");
				System.exit(10);
			}

			List remoteRepos;

			// Instantiate the remote repositories object
			try {
				remoteRepos = ProjectUtils.buildArtifactRepositories(repositories, artifactRepositoryFactory, mavenSession
						.getContainer());
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
			List<Artifact> artifacts = getApplicationArtifacts(applicationConfig, artifactFactory, application, version, satstabell_classifier, blaze_version);
			String configDir = null; // need to take care of cofig path that is cofigured below
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
						configDir = extDirPath;
					}
					// Exposing batch directory to maven context
					if (extDirPath.contains("nav-batch-")) {
						exposeMavenProperty("batchDir", extDirPath);
					}
				}
				if (artifact.getArtifactId().equalsIgnoreCase(BLASE_ARTIFACT_ID)){
					// don't need to unpack jar filen, just upload to config folder
					File blazeDest = new File(configDir + "/" + stripVersionAndClassifier(artifact.getFile().getName()));
					NativeOps.copy(artifact.getFile(), blazeDest);
				}
			}

		} catch (SAXException e1) {
			throw new MojoExecutionException("[ERROR]: " + e1);
		} catch (IOException e1) {
			throw new MojoExecutionException("[ERROR]: " + e1);
		} catch (ParserConfigurationException e1) {
			throw new MojoExecutionException("[ERROR]: " + e1);
		}

	}
	
	private static String stripVersionAndClassifier(String artifactName){
		Pattern pattern = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+");
		Matcher matcher = pattern.matcher(artifactName);
		
		int versionStart = -1;
		while (matcher.find()) versionStart = matcher.start();
		return versionStart <=0 ? artifactName : artifactName.substring(0, versionStart-1) + ".jar";
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

	public static List<Artifact> getApplicationArtifacts(String applicationConfig, ArtifactFactory artifactory,
			String application, String version, String satsTabellClassifier, String blazeVersion) throws SAXException, IOException, ParserConfigurationException {

		List<Artifact> artifacts = new ArrayList<Artifact>();
		HashMap<String, Application> applications = ApplicationConfig.getApplications(applicationConfig);
		Application app = applications.get(application);

		for (no.nav.devarch.utils.Artifact a : app.getArtifacts()) {

			String type = "";
			if (a.getType().equals("ear")) {
				type = "ear";
			} else {
				type = "jar";
			}
			if (a.getArtifactId().equalsIgnoreCase(BLASE_ARTIFACT_ID)){
				if (blazeVersion.equalsIgnoreCase("")) blazeVersion = "RELEASE";
				if (blazeVersion.equalsIgnoreCase("RELEASE")) System.out.println("[INFO] The version of the " + BLASE_ARTIFACT_ID +" artifact was not configured. The latest released version will be used.");
				artifacts.add(artifactory.createArtifactWithClassifier(a.getGroupId(), a.getArtifactId(), blazeVersion, type, satsTabellClassifier));
			}
			else {
				artifacts.add(artifactory.createArtifact(a.getGroupId(), a.getArtifactId(), version, null, type));
			}
		}

		if (application.equals("psak") || application.equals("pselv")) {

			Application pen = applications.get("pen");

			for (no.nav.devarch.utils.Artifact a : pen.getArtifacts()) {
				if (a.getType().equals("config")) {
					artifacts.add(artifactory.createArtifact(a.getGroupId(), a.getArtifactId(), version, null, "jar"));
				}
			}
		}

		return artifacts;
	}

	// Returns true or false based on whether the application contains batch
	public static boolean isBatch(String applicationConfig, String application) throws SAXException, IOException,
			ParserConfigurationException {
		return ApplicationConfig.getBatchApplications(applicationConfig).contains(application);
	}

	// Returns true or false based on whether the application is supporte
	public static boolean isSupported(String applicationConfig, String application) throws SAXException, IOException,
			ParserConfigurationException {
		return ApplicationConfig.getAllSupportApplications(applicationConfig).contains(application);
	}

}
