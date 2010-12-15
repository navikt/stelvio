package no.nav.maven.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import no.nav.devarch.utils.Application;
import no.nav.devarch.utils.ApplicationConfig;

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
import org.apache.maven.project.ProjectUtils;
import org.codehaus.plexus.archiver.UnArchiver;
import org.xml.sax.SAXException;

/**
 * Goal that compares two property files and detects missing properties.
 * 
 * @goal verify
 * 
 * @author test@example.com
 */
public class VerifyConfigMojo extends AbstractMojo {

	/**
	 * @parameter expression="${session}"
	 * @readonly
	 * @required
	 */
	private MavenSession mavenSession;

	/**
	 * The remote repositories used as specified in your POM.
	 * 
	 * @parameter expression="${project.repositories}"
	 * @readonly
	 * @required
	 */
	private List<?> repositories;

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
	 * @parameter expression="${appVersion}"
	 * @required
	 */
	private String appVersion;

	/**
	 * @parameter expression="${configVersion}"
	 * @required
	 */
	private String configVersion;

	/**
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private String buildDirectoryPath;

	private boolean foundMissing = false;

	public void execute() throws MojoExecutionException {
		try {

			// Instantiate the remote repositories object
			List<?> remoteRepos = ProjectUtils.buildArtifactRepositories(repositories, artifactRepositoryFactory, mavenSession.getContainer());
			Artifact devArchConfig = resolveRemoteArtifact(remoteRepos, artifactFactory.createArtifact("no.nav.maven.devarch", "devarch-config", configVersion, null, "jar"));
			File buildDirectory = new File(buildDirectoryPath);
			
			buildDirectory.mkdirs();
			
			File daExtDir = Archiver.extractArchive(devArchConfig.getFile(), buildDirectory, unArchiver);
			HashMap<String, Application> apps = ApplicationConfig.getApplications(daExtDir.getAbsolutePath() + "/misc/config/applicationConfig.xml");

			Application app = apps.get(application);
			String envConfigFile = app.getEnvConfigFile();

			if (envConfigFile == null) {
				getLog().info("No environment specific configuration file detected.");
				return;
			}

			no.nav.devarch.utils.Artifact daConfigArtifact = null;

			for (no.nav.devarch.utils.Artifact a : app.getArtifacts()) {
				if (a.getType().equals("config")) {
					daConfigArtifact = a;
				}
			}

			Artifact configArtifact = resolveRemoteArtifact(remoteRepos, artifactFactory.createArtifact(daConfigArtifact.getGroupId(), daConfigArtifact.getArtifactId(), appVersion, null, "jar"));

			File devExtDir = Archiver.extractArchive(configArtifact.getFile(), buildDirectory, unArchiver);
			File devFile = new File(devExtDir + "/" + envConfigFile);
			File ourFile = new File(daExtDir + "/module_config/" + application + "/spring/" + envConfigFile);

			HashMap<String, String> dev_our_missing = findMissing(devFile, ourFile);
			HashMap<String, String> our_dev_missing = findMissing(ourFile, devFile);
			
			getLog().info("");
			getLog().info("------------------------------------------------------------------");
			getLog().info("Comparing the development file against our DevArch-config file ...");
			getLog().info("------------------------------------------------------------------");
			
			if (!dev_our_missing.isEmpty()){
				getLog().info("");
				getLog().info("Found the following missing properties in our DevArch-config file:");
				getLog().info("");
				printReport(dev_our_missing, devFile, ourFile);
			} else {
				getLog().info("");
				getLog().info("No missing properties found, all OK.");
				getLog().info("");
			}
			
			getLog().info("-------------------------------------------------------------------");
			getLog().info("Comparing our DevArch-config file against the development file ... ");
			getLog().info("-------------------------------------------------------------------");
			
			if (!our_dev_missing.isEmpty()){
				getLog().info("");
				getLog().info("Found the following missing properties in the development file:");
				getLog().info("");
				printReport(our_dev_missing, ourFile, devFile);
			} else {
				getLog().info("");
				getLog().info("No missing properties found, all OK.");
				getLog().info("");
			}
			
			if (!foundMissing) {
				getLog().info("");
				getLog().info("Everything is fine.");
				getLog().info("");
			} else {
				throw new MojoExecutionException("Found missing properties, fix it.");
			}

		} catch (InvalidRepositoryException e) {
			throw new MojoExecutionException("[ERROR] Error building remote repositories", e);
		} catch (SAXException e1) {
			throw new MojoExecutionException("[ERROR]: " + e1);
		} catch (ParserConfigurationException e1) {
			throw new MojoExecutionException("[ERROR]: " + e1);
		} catch (IOException e1) {
			throw new MojoExecutionException("[ERROR]: " + e1);
		}

	}

	private void printReport(HashMap<String, String> missing, File baseFile, File checkFile) {

		for (String key : missing.keySet()) {
			getLog().info(key + " = " + missing.get(key));
		}
		
		getLog().info("");
	}

	private HashMap<String, String> findMissing(File baseFile, File checkFile) throws MojoExecutionException {

		getLog().debug("Matching " + baseFile.getAbsolutePath() + " against " + checkFile.getAbsolutePath());

		HashMap<String, String> missing = new HashMap<String, String>();
		HashMap<String, String> baseProps = loadProperties(baseFile);
		HashMap<String, String> checkProps = loadProperties(checkFile);

		for (String key : baseProps.keySet()) {
			if (checkProps.containsKey(key)) {
				getLog().debug("Key [" + key + "] matched.");
			} else {
				foundMissing = true;
				missing.put(key, baseProps.get(key));
			}
		}

		return missing;
	}

	private HashMap<String, String> loadProperties(File f) throws MojoExecutionException {

		Properties p = new Properties();
		HashMap<String, String> map = new HashMap<String, String>();

		try {
			FileInputStream inputStream = new FileInputStream(f);
			p.load(inputStream);
			inputStream.close();

		} catch (IOException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		}

		for (Object o : p.keySet()) {
			String key = (String) o;
			map.put(key, p.getProperty(key));
		}

		return map;
	}

	// Resolves the artifact using the given repositories
	public Artifact resolveRemoteArtifact(List<?> remoteRepos, Artifact artifact) throws MojoExecutionException {

		try {
			artifactResolver.resolve(artifact, remoteRepos, localRepository);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException("[ERROR] Error downloading artifact.", e);
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException("[ERROR] Resource can not be found.", e);
		}

		return artifact;
	}

}
