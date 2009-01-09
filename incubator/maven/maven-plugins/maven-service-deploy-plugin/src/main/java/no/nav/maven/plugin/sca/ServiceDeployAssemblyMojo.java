package no.nav.maven.plugin.sca;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;

/**
 * This plugin builds a zip-file (containing jar-files) that can be used as
 * input to serviceDeploy.
 * 
 * @author test@example.com
 * 
 * @goal service-deploy-assembly
 * @requiresDependencyResolution
 */
public class ServiceDeployAssemblyMojo extends AbstractMojo {
	private static final String JAR_CLASSIFIER = "jar";

	/**
	 * @component roleHint="zip"
	 * @required
	 * @readonly
	 */
	private Archiver archiver;

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
			File outputFile = new File(build.getDirectory(), build.getFinalName() + "-sd" + ".zip");
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

		public void build() throws ArchiverException {
			addProjectArtifact();
			addDependencyArtifacts();
		}

		@SuppressWarnings("unchecked")
		private void addProjectArtifact() throws ArchiverException {
			Collection<Artifact> attachedArtifacts = project.getAttachedArtifacts();
			for (Artifact attachedArtifact : attachedArtifacts) {
				if (JAR_CLASSIFIER.equals(attachedArtifact.getClassifier())) {
					addArtifact(attachedArtifact);
					break;
				}
			}
		}

		@SuppressWarnings("unchecked")
		private void addDependencyArtifacts() throws ArchiverException {
			Collection<Artifact> runtimeArtifacts = project.getRuntimeArtifacts();
			for (Artifact runtimeArtifact : runtimeArtifacts) {
				addArtifact(runtimeArtifact);
			}
		}

		private void addArtifact(Artifact artifact) throws ArchiverException {
			getLog().debug("Adding artifact " + artifact + " to assembly");
			archiver.addFile(artifact.getFile(), artifact.getArtifactId() + "." + artifact.getArtifactHandler().getExtension());
		}
	}
}
