package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.InvalidRepositoryException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectUtils;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that extracts the defined DeployArtifacts
 * 
 * @author test@example.com
 * 
 * @goal extract-esb-release
 * @requiresDependencyResolution
 */
public class ExtractModules extends WebsphereUpdaterMojo {

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
	private ArtifactResolver resolver;
	
	/** @component */
	private MavenProjectBuilder mavenProjectBuilder;
	
	/** @component */
	private ArtifactMetadataSource artifactMetadataSource;

	@Override
	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {

		try {
			
			List<?> remoteRepos = ProjectUtils.buildArtifactRepositories(repositories, artifactRepositoryFactory, mavenSession.getContainer());
			
			for (DeployArtifact da : artifacts){
				
				if (da.getVersion() == null){
					getLog().info("Skipping " + da.getGroupId() + ":" + da.getArtifactId() + ", no version specified.");
					break;
				}
				
				Artifact pomArtifact = artifactFactory.createArtifact(da.getGroupId(), da.getArtifactId(), da.getVersion(), null, "pom");
				MavenProject pomProject = mavenProjectBuilder.buildFromRepository( pomArtifact, remoteRepos, localRepository);
				Set<?> artifacts = pomProject.createArtifacts(this.artifactFactory, null, null);
				ArtifactResolutionResult arr = resolver.resolveTransitively(artifacts, pomArtifact, remoteRepos, localRepository, artifactMetadataSource);
				
				if (arr.getArtifacts().size() == 0){
					throw new MojoExecutionException("Unable to retrieve artifact, " + da.toString());
				}
				
				Iterator<?> iter = arr.getArtifacts().iterator(); 
				
				while (iter.hasNext()){
					Artifact a = (Artifact) iter.next();
					File src = new File(a.getFile().getAbsolutePath());
					FileUtils.copyFileToDirectory(src, new File(deployableArtifactsHome));
				}
				
				getLog().info("Successfully extracted dependency artifacts of " + da.toString() + " into " + deployableArtifactsHome);
				
			}
		} catch (InvalidRepositoryException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (InvalidDependencyVersionException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (ProjectBuildingException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (IOException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		}
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

	@Override
	protected String getGoalPrettyPrint() {
		return "Extract ESB release";
	}
	

	
}
