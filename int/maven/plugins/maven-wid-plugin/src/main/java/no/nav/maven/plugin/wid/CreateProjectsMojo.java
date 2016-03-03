package no.nav.maven.plugin.wid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

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
	private static final String PACKAGING_SERVICE_SPECIFICATION = "service-specification";
	private static final String PACKAGING_MESSAGE_SPECIFICATION = "message-specification";
	
	private static final Object OLD_SERVICE_SPECIFICATION_POM_GROUP_ID = "no.nav.tjenester";
	private static final Object OLD_SERVICE_SPECIFICATION_POM_ARTIFACT_ID = "service-specification-pom";

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
	 * @component
	 * @readonly
	 * @required
	 */
	private MavenProjectBuilder projectBuilder;

	/**
	 * @parameter default-value="${project.basedir}/libraries"
	 * @required
	 */
	private File dependencyProjectsDirectory;
	
	/**
	 * @parameter
	 */
	private Parent wpsLibraryParent;

	public void execute() throws MojoExecutionException, MojoFailureException {
		Collection<String> reactorProjectIds = new HashSet<String>(reactorProjects.size());
		for (MavenProject reactorProject : reactorProjects) {
			getLog().info("TKN - execute() - reactorProject.getId(): " + reactorProject.getId());
			reactorProjectIds.add(reactorProject.getId());
		}
		
		// Using a HashSet to eliminate any duplicate artifacts (relying on Artifact.hashCode)
		Collection<Artifact> dependencyArtifacts = new HashSet<Artifact>();
		for (MavenProject reactorProject : reactorProjects) {
			String packaging = reactorProject.getPackaging();
			if (PACKAGING_WPS_MODULE_EAR.equals(packaging) || PACKAGING_WPS_LIBRARY_JAR.equals(packaging) || PACKAGING_SERVICE_SPECIFICATION.equals(packaging) || PACKAGING_MESSAGE_SPECIFICATION.equals(packaging)) {
				for (Artifact dependencyArtifact : (Collection<Artifact>) reactorProject.getArtifacts()) {
					// TODO: For now we only support dependencies of type wps-library-jar, service-specification and message-spesification
					if (PACKAGING_WPS_LIBRARY_JAR.equals(dependencyArtifact.getType()) || PACKAGING_SERVICE_SPECIFICATION.equals(dependencyArtifact.getType()) || PACKAGING_MESSAGE_SPECIFICATION.equals(dependencyArtifact.getType())) {
						String dependencyArtifactId = dependencyArtifact.getId();
						// Only add dependency artifacts that are not part of the reactor
						if (!reactorProjectIds.contains(dependencyArtifactId)) {
							getLog().info("TKN - execute() - !reactorProjectIds.contains(dependencyArtifactId): " + dependencyArtifactId);
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
		getLog().info("TKN - createProject() - artifact.getType(): " + artifact.getType());
		
		if (PACKAGING_WPS_LIBRARY_JAR.equals(artifact.getType())) {
			Artifact sourceArtifact = artifactFactory.createArtifactWithClassifier(artifact.getGroupId(), artifact.getArtifactId(),
					artifact.getVersion(), artifact.getType(), "sources");
				artifact = resolveArtifact(remoteRepos, sourceArtifact);
		}
		
		File extractDirectory = extractArtifact(artifact);
		if (PACKAGING_SERVICE_SPECIFICATION.equals(artifact.getType()) || PACKAGING_MESSAGE_SPECIFICATION.equals(artifact.getType())) {
			// Create temporary pom.xml based on the service-specification pom
			File tempPom = new File(extractDirectory, "pom.xml");
			try {
				MavenProject project = projectBuilder.buildFromRepository(artifact, remoteRepos, localRepository);
				// Use default value to simplify usage
				if (wpsLibraryParent == null) {
					wpsLibraryParent = new Parent();
					wpsLibraryParent.setGroupId("no.stelvio.maven.poms");
					wpsLibraryParent.setArtifactId("maven-wps-library-pom");
					wpsLibraryParent.setVersion("2.1.14");
				}
				project.getOriginalModel().setParent(wpsLibraryParent);
				project.getOriginalModel().setPackaging(PACKAGING_WPS_LIBRARY_JAR);
				project.writeOriginalModel(new FileWriter(tempPom));
			} catch (ProjectBuildingException e) {
				throw new MojoExecutionException("Unable to load project pom for dependent project", e);
			} catch (IOException e) {
				throw new MojoExecutionException("Unable to write temporary project pom " + tempPom.getAbsolutePath(), e);
			}
		} else if (PACKAGING_WPS_LIBRARY_JAR.equals(artifact.getType())) {
			// If artifact is a library then we want to run the latest wid plugin on it. 
			Model libraryModel = readModelFromPomFileIn(extractDirectory);
			if (isParentOldServiceSpecification(libraryModel)) {
				// Many old service specification are never changed and use the old wid plugin. 
				// This part forces the old service specifications to use the wps-library parent defined in  
				// wid plugin configuration so that they can run newer wid plugin, just as newer service specifications
				getLog().info("Found old service specification, setting parent to "+wpsLibraryParent.getId());
				libraryModel.setParent(wpsLibraryParent);
				writeModelToPomFile(libraryModel,extractDirectory);
			} else {
				
				///For other libraries latest wid plugin is achieved by simply updating to latest parent pom. 
				getLog().info("Updating library to latest parent pom");
				invokeUpdateParent(extractDirectory);
			}

		}
		
		invokeWidPlugin(extractDirectory);
	}

	private boolean isParentOldServiceSpecification(Model model) {
		Parent parent = model.getParent();
		return parent.getArtifactId().equals(OLD_SERVICE_SPECIFICATION_POM_ARTIFACT_ID) &&
				parent.getGroupId().equals(OLD_SERVICE_SPECIFICATION_POM_GROUP_ID);
	}

	private void invokeUpdateParent(File baseDirectory) throws MojoExecutionException {
		try {
			InvocationRequest request = new DefaultInvocationRequest();
			request.setBaseDirectory(baseDirectory);
			request.setGoals(Collections.singletonList("versions:update-parent"));
			invoker.execute(request);
		} catch (MavenInvocationException e) {
			throw new MojoExecutionException("Error invoking Maven", e);
		}		
	}

	private Model readModelFromPomFileIn(File directory) throws MojoExecutionException {
		FileReader fileReader = null;
		try {
			File pomFile = new File(directory, "pom.xml");
			Model model = null;
			try {
				fileReader = new FileReader(pomFile);
				model = new MavenXpp3Reader().read(fileReader);
			} catch (XmlPullParserException e) {
				throw new MojoExecutionException("Unable to read or write to parse pom file in "+directory , e);
			} finally {
				fileReader.close();
			}
			
			return model;

			
		} catch (FileNotFoundException e) {
			throw new MojoExecutionException("Unable to find pom file in "+directory , e);
		} catch (IOException e) {
			throw new MojoExecutionException("Unable to read pom file in "+directory , e);
		} 
	}

	private void writeModelToPomFile(Model model, File directory) throws MojoExecutionException{
		FileWriter fileWriter = null;
		try {
			try {
				File pomFile = new File(directory, "pom.xml");
				fileWriter = new FileWriter(pomFile);
				new MavenXpp3Writer().write(fileWriter, model);
				
			} finally {
				fileWriter.close();
			}
			
		} catch (FileNotFoundException e) {
			throw new MojoExecutionException("Unable to find pom file in "+directory , e);
		} catch (IOException e) {
			throw new MojoExecutionException("Unable to write to pom file in "+directory , e);
		} 
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
