package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that makes sure BPROC's are uninstalled if they are part of the deploy.
 * 
 * @author test@example.com
 * 
 * 
 * @goal remove-bpel
 * @requiresDependencyResolution
 */
public class RemoveBPELMojo extends WebsphereUpdaterMojo {
	/**
	 * @parameter expression="${project.properties}"
	 * @required
	 */
	protected Properties properties;

	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		File modules = new File(deployableArtifactsHome);
		String[] bpFiles = modules.list(new FilenameFilter() {
			public boolean accept(File file, String name) {
				return name.endsWith(".bp");
			}
		});

		List<String> bpFilesList = Arrays.asList(bpFiles);
		System.out.println(bpFilesList);
		for (Artifact a : artifacts) {
			String fullArtifactName = a.getArtifactId() + "-" + a.getVersion();
			if (bpFilesList.contains(fullArtifactName + ".bp")) {
				/* TODO: Follow the uninstall logic used for "regular" modules */
				removeBPEL(commandLine, fullArtifactName);
			}
		}
	}

	private final void removeBPEL(final Commandline commandLine, final String artifactId) {
		getLog().info("Stopping and uninstalling: " + artifactId);
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/bpcTemplates.jacl -uninstall " + artifactId);
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Remove BPEL";
	}
}