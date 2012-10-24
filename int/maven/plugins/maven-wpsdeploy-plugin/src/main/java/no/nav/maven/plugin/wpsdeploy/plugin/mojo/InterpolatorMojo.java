package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.Commandline;
import org.twdata.maven.mojoexecutor.MojoExecutor;

/**
 * @author test@example.com
 * 
 * Executes the 'generate' goal on the maven-propertiesgenerator-plugin
 * 
 * @goal interpolate-config
 * @requiresDependencyResolution
 */
public class InterpolatorMojo extends WebsphereUpdaterMojo {
	/**
	 * The Maven Project Object
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;
	
	/**
	 * The Maven Session Object
	 * 
	 * @parameter expression="${session}"
	 * @required
	 * @readonly
	 */
	protected MavenSession session;
	
	/**
	 * The Maven BuildPluginManager Object
	 *  
	 * @component
	 * @required
	 */
	protected BuildPluginManager buildPluginManager;
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		MojoExecutor.executeMojo(
				MojoExecutor.plugin(
						MojoExecutor.groupId("no.nav.maven.plugins"),
						MojoExecutor.artifactId("maven-propertiesgenerator-plugin"),
						MojoExecutor.version("2.1")
					),
					MojoExecutor.goal("generate"),
					MojoExecutor.configuration(
						MojoExecutor.element(
							MojoExecutor.name("templateDir"),
							templatesPath
						),
						MojoExecutor.element(
							MojoExecutor.name("outputDir"),
							applicationPropertiesPath
						),
						MojoExecutor.element(
							MojoExecutor.name("environmentProperties"),
							mainPropertiesFilepath
						)
					),
					MojoExecutor.executionEnvironment(project, session, buildPluginManager)
				);
	}

	protected String getGoalPrettyPrint() {
		return "Interpolate configuration";
	}
}
