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
		System.out.println("project: "+project);
		System.out.println("session: "+session);
		System.out.println("buildPluginManager: "+buildPluginManager);
		System.out.println("templatesPath: "+templatesPath);
		System.out.println("applicationPropertiesPath: "+applicationPropertiesPath);
		System.out.println("propertiesPath: "+propertiesPath);
		MojoExecutor.executeMojo(
				MojoExecutor.plugin(
						MojoExecutor.groupId("no.nav.maven.plugins"),
						MojoExecutor.artifactId("maven-propertiesgenerator-plugin"),
						MojoExecutor.version("3.0-SNAPSHOT")
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
							propertiesPath
						)
					),
					MojoExecutor.executionEnvironment(project, session, buildPluginManager)
				);
	}

	protected String getGoalPrettyPrint() {
		return "Interpolate configuration";
	}
}
