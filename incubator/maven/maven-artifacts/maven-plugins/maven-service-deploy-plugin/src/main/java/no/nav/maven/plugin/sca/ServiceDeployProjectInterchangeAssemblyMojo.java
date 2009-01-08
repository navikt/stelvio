package no.nav.maven.plugin.sca;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.AbstractArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;

/**
 * This plugin builds a zip-file that can be used as input to serviceDeploy.
 * 
 * @author test@example.com
 * 
 * @goal service-deploy-project-interchange-assembly
 * @requiresDependencyResolution
 */
public class ServiceDeployProjectInterchangeAssemblyMojo extends AbstractMojo {
	/**
	 * @component
	 */
	private ArtifactFactory artifactFactory;

	/**
	 * @component
	 */
	private ArtifactResolver artifactResolver;

	/**
	 * @component roleHint="zip"
	 * @required
	 * @readonly
	 */
	private Archiver archiver;

	/**
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 */
	private ArtifactRepository localRepository;

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${project.build.finalName}"
	 * @required
	 * @readonly
	 */
	private String finalName;

	/**
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException {
		try {
			new ServiceDeployAssemblyBuilder(archiver).build();

			File outputFile = new File(outputDirectory, finalName + ".zip");
			archiver.setDestFile(outputFile);
			archiver.createArchive();
		} catch (ArchiverException e) {
			throw new MojoExecutionException("Error creating service deploy assembly", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating service deploy assembly", e);
		}
	}

	private class ServiceDeployAssemblyBuilder {
		private Archiver archiver;

		public ServiceDeployAssemblyBuilder(Archiver archiver) {
			this.archiver = archiver;
		}

		@SuppressWarnings("unchecked")
		public void build() throws ArchiverException, MojoExecutionException {
			Collection<Artifact> attachedArtifacts = project.getAttachedArtifacts();
			for (Artifact attachedArtifact : attachedArtifacts) {
				if ("pi".equals(attachedArtifact.getClassifier())) {
					addArtifact(attachedArtifact);
					break;
				}
			}
			Collection<Dependency> runtimeDependencies = project.getRuntimeDependencies();
			for (Dependency runtimeDependency : runtimeDependencies) {
				Artifact artifact = artifactFactory.createArtifactWithClassifier(runtimeDependency.getGroupId(),
						runtimeDependency.getArtifactId(), runtimeDependency.getVersion(), "project-interchange", "pi");
				try {
					artifactResolver.resolve(artifact, project.getRemoteArtifactRepositories(), localRepository);
				} catch (AbstractArtifactResolutionException e) {
					throw new MojoExecutionException("Error resolving artifact", e);
				}
				addArtifact(artifact);
			}
		}

		private void addArtifact(Artifact artifact) throws ArchiverException {
			getLog().debug("Adding artifact " + artifact + " to assembly");
			archiver.addArchivedFileSet(artifact.getFile());
		}
	}
}
