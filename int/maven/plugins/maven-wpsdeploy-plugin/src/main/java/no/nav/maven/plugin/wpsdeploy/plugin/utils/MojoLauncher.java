package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;

/* This is the experimental execute-a-goal-programatically code :) 
 * TODO: Remove all hardcoded stuff
 */
public class MojoLauncher {

	public static final void executePropertiesGeneratorMojo(final MavenProject project, final MavenSession session, final PluginManager pluginManager, String tmpTemplatesPath, String tmpApplicationPropertiesPath, String tmpEnvironmentPropertiesPath) throws MojoExecutionException,
	MojoFailureException {
		MojoExecutor.executeMojo(
				MojoExecutor.plugin(
						MojoExecutor.groupId("no.nav.maven.plugins"),
						MojoExecutor.artifactId("maven-propertiesgenerator-plugin"),
						MojoExecutor.version("2.0")
						),
						MojoExecutor.goal("generate"),
						MojoExecutor.configuration(
								MojoExecutor.element(
										MojoExecutor.name("templateDir"),
										tmpTemplatesPath
										),
										MojoExecutor.element(
												MojoExecutor.name("environmentName"),
												"${environment}"
												),
												MojoExecutor.element(
														MojoExecutor.name("outputDir"),
														tmpApplicationPropertiesPath
														),
														MojoExecutor.element(
																MojoExecutor.name("environmentDir"),
																tmpEnvironmentPropertiesPath
																)
								),
								MojoExecutor.executionEnvironment(
										project,
										session,
										pluginManager
										)
				);
	}
}
