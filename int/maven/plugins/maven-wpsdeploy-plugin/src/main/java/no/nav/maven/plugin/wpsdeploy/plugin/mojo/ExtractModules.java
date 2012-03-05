package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.ArgumentUtil;
import no.nav.maven.plugin.wpsdeploy.plugin.utils.XMLUtils;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.InvalidRepositoryException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Repository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.xml.sax.SAXException;

/**
 * Goal that extracts the defined DeployArtifacts
 * 
 * @author test@example.com
 * 
 * @goal extract-module-poms
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
	private MavenProjectBuilder mavenProjectBuilder;

	private Set<Artifact> allArtifacts; 

	/**
	 * Takes a comma separated string of pom files and downloads the dependencies to the EARFilesToDeploy folder
	 * @param wsadminCommandLine
	 * @param poms
	 * @param mvnSnapshotRepo 
	 * @param mvnSnapshotRepo 
	 */
	private void downloadEARFiles(Commandline wsadminCommandLine, String poms, String mvnLocalRepo, String mvnRepo, String mvnSnapshotRepo){
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("DownloadEARFiles.py " + deployableArtifactsHome + " " +poms + " " + mvnLocalRepo + " " + mvnRepo + " " + mvnSnapshotRepo);
		wsadminCommandLine.addArg(arg);
		executeCommand(wsadminCommandLine);
	}	

	@SuppressWarnings("unchecked")
	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		try {
			allArtifacts = new HashSet<Artifact>();

			List<?> remoteRepos = ProjectUtils.buildArtifactRepositories(repositories, artifactRepositoryFactory, mavenSession.getContainer());
			ArrayList<String> poms = new ArrayList<String>();
			
			for (DeployArtifact da : artifacts){

				if (da.getVersion() == null || da.getVersion().startsWith("$")){
					getLog().info("Skipping " + da.getGroupId() + ":" + da.getArtifactId() + ", no version specified.");
					continue;
				}

				Artifact pomArtifact = artifactFactory.createArtifact(da.getGroupId(), da.getArtifactId(), da.getVersion(), null, "pom");
				mavenProjectBuilder.buildFromRepository( pomArtifact, remoteRepos, localRepository);
				poms.add(pomArtifact.getFile().toString());
				
				HashSet<Artifact> artifacts = XMLUtils.parsePomDependencies(pomArtifact.getFile(), artifactFactory);
				allArtifacts.addAll(artifacts);
				
				getLog().info("Successfully extracted "+ artifacts.size() +" dependency artifacts of " + da.toString() + " into \"allArtifacts\" list.");
			}
			
			for (Repository r : (List<Repository>) repositories)
				getLog().info("Available repository: "+ r.getUrl()); 
					
			project.setArtifacts(allArtifacts);
			project.setDependencyArtifacts(allArtifacts);

			String mvnLocalRepo = localRepository.getUrl();
			String mvnSnapshotRepo = ((Repository)repositories.get(0)).getUrl();
			String mvnRepo = mvnSnapshotRepo.replace("-snapshots", "");
			downloadEARFiles(wsadminCommandLine, ArgumentUtil.arrayListToDelimitedString(poms, ","), mvnLocalRepo, mvnRepo, mvnSnapshotRepo);
			
		} catch (ProjectBuildingException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (InvalidRepositoryException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (SAXException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (ParserConfigurationException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (FactoryConfigurationError e) {
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