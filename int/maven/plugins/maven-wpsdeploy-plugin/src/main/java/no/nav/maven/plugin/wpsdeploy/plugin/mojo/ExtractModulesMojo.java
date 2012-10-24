package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.FactoryConfigurationError;

import no.nav.maven.plugin.wpsdeploy.plugin.models.DeployArtifact;
import no.nav.maven.utils.ArtifactUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.util.artifact.DefaultArtifact;

/**
 * Goal that extracts the defined DeployArtifacts
 * 
 * @goal extract-module-poms
 * @requiresDependencyResolution
 */
public class ExtractModulesMojo extends WebsphereUpdaterMojo {

	/** 
	 * @parameter
	 * @required
	 */
	private DeployArtifact[] artifacts;

	/** 
	 * The entry point to Aether, i.e. the component doing all the work.      
	 * 
	 * @required
	 * @component
	 */    
	private RepositorySystem repoSystem;

	/**
	 * The current repository/network configuration of Maven.
	 *
	 * @parameter default-value="${repositorySystemSession}"
	 * @required
	 * @readonly
	 */
	private RepositorySystemSession repoSession;

	/**     
	 * The project's remote repositories to use for the resolution of plugins and their dependencies.     
	 * @parameter default-value="${project.remotePluginRepositories}"
	 * @readonly     
	 */    
	private List<RemoteRepository>  repositories;

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

	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		try {
			Set<Artifact> resolvedArtifacts = new HashSet<Artifact>();

			BufferedWriter installationRecepie = new BufferedWriter(new FileWriter(deployDependencies));
			installationRecepie.write("Name,Version,Path,Install_application,Deploy_resources,Uninstall_old_version\n");

			if(isOneModuleDeploy()){
				// Used in case this is a one module deploy (IRPORT-2102)
				DefaultArtifact unresolvedArtifact = new DefaultArtifact(moduleGroupId, moduleArtifactId, moduleType, moduleVersion);
				Artifact resolvedArtifact = resolveArtifact(unresolvedArtifact);
				resolvedArtifacts.add(resolvedArtifact);
			} else {
				resolvedArtifacts = getResolvedProjectArtifacts();
			}
			
			writeArtifactsToinstallationRecepie(resolvedArtifacts, installationRecepie);

			installationRecepie.close();

			if(resolvedArtifacts.isEmpty()){
				throw new IllegalArgumentException("resolvedArtifacts variabelen kan ikke være tom!\nSjekk at buss versjonen du forsøker å deploye eksisterer.");
			} else {
				getLog().info("Continuing with "+ resolvedArtifacts.size() +" dependency artifacts!");
			}

		} catch (FactoryConfigurationError e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (IOException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		}
	}

	private boolean isOneModuleDeploy() {
		return (moduleGroupId != null) && (moduleArtifactId != null) && (moduleVersion != null) && (moduleType != null);
	}

	private Artifact resolveArtifact(DefaultArtifact unresolvedArtifact) throws MojoExecutionException {
		Artifact resolvedArtifact = ArtifactUtils.resolveArtifact(repositories, repoSystem, repoSession, unresolvedArtifact);
		return resolvedArtifact;
	}

	private Set<Artifact> getResolvedProjectArtifacts() throws IOException, MojoExecutionException {
		Set<Artifact> output = new HashSet<Artifact>();
		for (DeployArtifact da : artifacts){
			Artifact esbBus = deployArtifactToAetherArtifact(da);
			int originalSize = output.size();

			getLog().info("Resolving "+ esbBus.getArtifactId() +":");
			for(Artifact artifact : resolveDependencies(esbBus)){
				if("ear".equals(artifact.getExtension())){
					output.add(artifact);
				}
			}

			getLog().info("Successfully extracted "+ (output.size() - originalSize) +" dependency artifacts of " + da.toString() + " into \"output\" list.");
		}
		return output;
	}

	private Artifact deployArtifactToAetherArtifact(DeployArtifact artifact)
	{
		return new DefaultArtifact(artifact.getGroupId(), artifact.getArtifactId(),"pom", artifact.getVersion());
	}


	private List<Artifact> resolveDependencies(Artifact artifact) throws MojoExecutionException{
		return ArtifactUtils.resolveDependencies(repositories, repoSystem, repoSession, artifact);
	}

	private void writeArtifactsToinstallationRecepie(Set<Artifact> resolvedArtifacts, BufferedWriter installationRecepie) throws IOException {
		for(Artifact artifact : resolvedArtifacts){
			installationRecepie.write(artifact.getArtifactId() +","+ artifact.getVersion() +","+ artifact.getFile().getAbsolutePath() +",False,False,False\n");
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Download EAR files";
	}
}