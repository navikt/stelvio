package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.FactoryConfigurationError;

import no.nav.maven.plugin.wpsdeploy.plugin.models.DeployArtifact;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.InvalidRepositoryException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectUtils;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;
import org.apache.maven.project.artifact.MavenMetadataSource;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that extracts the defined DeployArtifacts
 * 
 * @goal extract-module-poms
 * @requiresDependencyResolution
 */
public class ExtractModulesMojo extends WebsphereUpdaterMojo {

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
	 * The local repository taken from Maven's runtime. Typically $HOME/.m2/repository.
	 * 
	 * @parameter expression="${localRepository}"
	 * @readonly
	 * @required
	 */
	private ArtifactRepository localRepository;

	/** 
	 * @parameter
	 * @required
	 */
	private DeployArtifact[] artifacts;

	/** @component */
	private MavenProjectBuilder mavenProjectBuilder;

	/**
	 * @parameter expression="${module.groupId}"
	 */
	private String moduleGroupId;

	/**
	 * @parameter expression="${module.artifactId}"
	 */
	private String moduleArtifactId;

	/**
	 * @parameter expression="${module.version}"
	 */
	private String moduleVersion;

	/**
	 * @parameter expression="${module.type}"
	 */
	private String moduleType;

	private Set<Artifact> allArtifacts;

	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		try {
			allArtifacts = new HashSet<Artifact>();

			List<?> remoteRepos = ProjectUtils.buildArtifactRepositories(repositories, artifactRepositoryFactory, mavenSession.getContainer());

			BufferedWriter installationRecepies = new BufferedWriter(new FileWriter(deployDependencies));
			installationRecepies.write("Name,Version,Path,Install_application,Deploy_resources,Uninstall_old_version\n");

			if((moduleGroupId != null) && (moduleArtifactId != null) && (moduleVersion != null) && (moduleType != null)){
				Artifact artifact = artifactFactory.createArtifact(moduleGroupId, moduleArtifactId, moduleVersion, null, moduleType);
				addOneArtifact(artifact, remoteRepos, installationRecepies);
			} else {
				addAllArtifacts(remoteRepos, installationRecepies);
			}

			installationRecepies.close();
			createJythonDeployDependenciesPathPathScript(deployDependencies);

			if(allArtifacts.isEmpty()){
				throw new IllegalArgumentException("allArtifacts variabelen kan ikke være tom!\nSjekk at buss versjonen du forsøker å deploye eksisterer.");
			}
			
			project.setArtifacts(allArtifacts);
			project.setDependencyArtifacts(allArtifacts);

		} catch (ProjectBuildingException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (InvalidRepositoryException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (FactoryConfigurationError e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (IOException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (InvalidDependencyVersionException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException("[ERROR] Error downloading artifact.", e);
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException("[ERROR] Resource can not be found.", e);
		}
	}

	private void addOneArtifact(Artifact artifact, List<?> remoteRepos, BufferedWriter installationRecepies) throws ArtifactResolutionException, ArtifactNotFoundException, IOException{
		artifactResolver.resolve(artifact, remoteRepos, localRepository);
		allArtifacts.add(artifact);

		addArtifactToInstallationRecepies(artifact, installationRecepies);

		getLog().info(String.format("Successfully added %s-%s to \"allArtifacts\" list.", artifact.getArtifactId(), artifact.getVersion()));
	}

	@SuppressWarnings("unchecked")
	private void addAllArtifacts(List<?> remoteRepos, BufferedWriter installationRecepies) throws ProjectBuildingException,
			InvalidDependencyVersionException, ArtifactResolutionException,
			ArtifactNotFoundException, IOException {
		for (DeployArtifact da : artifacts){
			if (da.getVersion() == null || da.getVersion().startsWith("$")){
				getLog().info("Skipping " + da.getGroupId() + ":" + da.getArtifactId() + ", no version specified.");
				continue;
			}

			Artifact pomArtifact = artifactFactory.createArtifact(da.getGroupId(), da.getArtifactId(), da.getVersion(), null, "pom");
			MavenProject p = mavenProjectBuilder.buildFromRepository( pomArtifact, remoteRepos, localRepository);
			Set<Artifact> artifacts = MavenMetadataSource.createArtifacts(artifactFactory, p.getOriginalModel().getDependencies(), null, null, p);
			for (Artifact a : artifacts) {

				artifactResolver.resolve(a, remoteRepos, localRepository);
				allArtifacts.add(a);

				addArtifactToInstallationRecepies(a, installationRecepies);
			}

			getLog().info("Successfully extracted "+ artifacts.size() +" dependency artifacts of " + da.toString() + " into \"allArtifacts\" list.");
		}
	}

	private void addArtifactToInstallationRecepies(Artifact artifact, BufferedWriter installationRecepies) throws IOException {
		installationRecepies.write(artifact.getArtifactId() +","+ artifact.getVersion() +","+ artifact.getFile().getAbsolutePath() +",False,False,False\n");
	}

	private void createJythonDeployDependenciesPathPathScript(String installRecepiesScriptPath) throws MojoExecutionException {
		String generatedJythonScriptPath = jythonScriptsDirectory +"/lib/deployDependenciesPath.py"; //Change the name of the empty placeholder script in the lib folder if you change this name
		
		String script = "def getPath(): return '"+ installRecepiesScriptPath +"'";

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(generatedJythonScriptPath));
			try{
				bw.write(script);
			} finally {
				bw.close();
			}
		} catch (IOException e) {
			throw new MojoExecutionException("[ERROR generating jython script]: " + e);
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Download EAR files";
	}
}