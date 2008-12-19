package no.nav.maven.plugin.sca;

import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which sets the (sca) artifact handler. Workaround for a Maven bug:
 * Custom artifact handlers are not loaded automatically for custom packaging
 * types.
 * 
 * @author test@example.com
 * 
 * @goal set-artifact-handler
 */
public class SetScaArtifactHandlerMojo extends AbstractMojo {
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${component.org.apache.maven.artifact.handler.ArtifactHandler#sca-library-jar}"
	 * @required
	 * @readonly
	 */
	private ArtifactHandler scaLibraryArtifactHandler;

	/**
	 * @parameter expression="${component.org.apache.maven.artifact.handler.ArtifactHandler#sca-module-ear}"
	 * @required
	 * @readonly
	 */
	private ArtifactHandler scaModuleArtifactHandler;

	/**
	 * {@inheritDoc}
	 */
	public void execute() throws MojoExecutionException {
		getLog().info("Attempt to set artifact handler");
		String packaging = project.getPackaging();
		if ("sca-library-jar".equals(packaging)) {
			project.getArtifact().setArtifactHandler(scaLibraryArtifactHandler);
		} else if ("sca-module-ear".equals(packaging)) {
			project.getArtifact().setArtifactHandler(scaModuleArtifactHandler);
		} else {
			getLog().warn(
					"Packaging <" + packaging + "> is an unsupported packaging for this plugin. Artifact handler not set.");
		}
	}
}
