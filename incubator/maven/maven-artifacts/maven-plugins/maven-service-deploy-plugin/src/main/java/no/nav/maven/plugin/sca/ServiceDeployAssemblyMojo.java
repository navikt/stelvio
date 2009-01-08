package no.nav.maven.plugin.sca;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

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
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException {
		Build build = project.getBuild();
		try {
			File assemblyFile = new File(build.getDirectory(), build.getFinalName() + ".zip");
			getLog().debug("Initializing assembly: " + assemblyFile);

			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(assemblyFile)));
			out.setLevel(0);

			new ServiceDeployAssemblyBuilder(out).build();

			out.close();
			getLog().info("Successfully created assembly: " + assemblyFile);
		} catch (IOException ioe) {
			throw new MojoExecutionException("Error creating zip-file", ioe);
		}
	}

	private class ServiceDeployAssemblyBuilder {
		private static final int BUFFER_SIZE = 2048;

		private ZipOutputStream out;

		private byte[] data = new byte[BUFFER_SIZE];

		public ServiceDeployAssemblyBuilder(ZipOutputStream out) {
			this.out = out;
		}

		@SuppressWarnings("unchecked")
		public void build() throws IOException {
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

		private void addArtifact(Artifact artifact) throws IOException {
			getLog().debug("Adding artifact " + artifact + " to assembly");
			out.putNextEntry(new ZipEntry(artifact.getArtifactId() + "." + artifact.getArtifactHandler().getExtension()));
			writeFile(artifact.getFile());
		}

		private void writeFile(File file) throws FileNotFoundException, IOException {
			InputStream is = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
			int count;
			while ((count = is.read(data, 0, BUFFER_SIZE)) != -1) {
				out.write(data, 0, count);
			}
			is.close();
		}
	}
}
