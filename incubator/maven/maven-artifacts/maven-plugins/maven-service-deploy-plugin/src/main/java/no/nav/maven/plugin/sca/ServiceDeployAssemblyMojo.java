package no.nav.maven.plugin.sca;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipArchiver;

/**
 * This plugin builds a zip-file that can be used as input to serviceDeploy.
 * 
 * @author test@example.com
 * 
 * @goal service-deploy-assembly
 * @phase package
 * @requiresDependencyResolution
 */
public class ServiceDeployAssemblyMojo extends AbstractMojo {
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
			Archiver archiver = new ZipArchiver();

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
		public void build() throws ArchiverException {
			Collection<Artifact> attachedArtifacts = project.getAttachedArtifacts();
			for (Artifact attachedArtifact : attachedArtifacts) {
				if ("jar".equals(attachedArtifact.getClassifier())) {
					addArtifact(attachedArtifact);
					break;
				}
			}
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
