package no.nav.maven.plugin.sca;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.AbstractArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;

/**
 * This plugin builds a zip-file (Project Interchange) that can be used as input
 * to serviceDeploy.
 * 
 * @author test@example.com
 * 
 * @goal service-deploy-project-interchange-assembly
 * @requiresDependencyResolution
 */
public class ServiceDeployProjectInterchangeAssemblyMojo extends AbstractMojo {
	private static final String PROJECT_INTERCHANGE_ARTIFACT_TYPE = "project-interchange";
	private static final String PROJECT_INTERCHANGE_CLASSIFIER = "pi";

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
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException {
		try {
			Build build = project.getBuild();
			File outputFile = new File(build.getDirectory(), build.getFinalName() + ".zip");
			archiver.setDestFile(outputFile);

			new ServiceDeployAssemblyBuilder(archiver).build();

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

		public void build() throws ArchiverException, MojoExecutionException {
			addProjectArtifact();
			addDependencyArtifacts();
		}

		@SuppressWarnings("unchecked")
		private void addProjectArtifact() throws ArchiverException {
			Collection<Artifact> attachedArtifacts = project.getAttachedArtifacts();
			for (Artifact attachedArtifact : attachedArtifacts) {
				if ("pi".equals(attachedArtifact.getClassifier())) {
					addArtifact(attachedArtifact);
					break;
				}
			}
		}

		@SuppressWarnings("unchecked")
		private void addDependencyArtifacts() throws MojoExecutionException, ArchiverException {
			Collection<Dependency> runtimeDependencies = project.getRuntimeDependencies();
			for (Dependency runtimeDependency : runtimeDependencies) {
				Artifact artifact = artifactFactory.createArtifactWithClassifier(runtimeDependency.getGroupId(),
						runtimeDependency.getArtifactId(), runtimeDependency.getVersion(), PROJECT_INTERCHANGE_ARTIFACT_TYPE,
						PROJECT_INTERCHANGE_CLASSIFIER);
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
