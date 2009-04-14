package no.nav.maven.plugin.websphere.plugin.utils;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;

/* This is the experimental execute-a-goal-programatically code :) 
 * TODO: Remove all hardcoded stuff
 */
public class MojoLauncher {
	
	public static final void executePropertiesGeneratorMojo(final MavenProject project, final MavenSession session, final PluginManager pluginManager)  throws MojoExecutionException, MojoFailureException {
		MojoExecutor.executeMojo(
			MojoExecutor.plugin(
        		   MojoExecutor.groupId("nav.maven.plugins"),
        		   MojoExecutor.artifactId("maven-propertiesgenerator-plugin"),
        		   MojoExecutor.version("1.4")
           ),
           MojoExecutor.goal("generate"),
           MojoExecutor.configuration(
        		   MojoExecutor.element(MojoExecutor.name("templateDir"), "${basedir}/src/main/scripts/templates"),
        		   MojoExecutor.element(MojoExecutor.name("environmentName"), "${environment}"),
        		   MojoExecutor.element(MojoExecutor.name("outputDir"), "${basedir}/src/main/scripts/app_props"),
        		   MojoExecutor.element(MojoExecutor.name("environmentDir"), "${basedir}/src/main/scripts/environments")
           ),
           MojoExecutor.executionEnvironment(
                   project,
                   session,
                   pluginManager
           )
       );
	}

	public static final void executeLoadWebsphereConfigurationMojo(final MavenProject project, final MavenSession session, final PluginManager pluginManager)  throws MojoExecutionException, MojoFailureException {
		MojoExecutor.executeMojo(
			MojoExecutor.plugin(
        		   MojoExecutor.groupId("no.nav.maven.plugins"),
        		   MojoExecutor.artifactId("maven-websphere-plugin"),
        		   null
           ),
           MojoExecutor.goal("load-runtime-configuration"),
           null,
           MojoExecutor.executionEnvironment(
                   project,
                   session,
                   pluginManager
           )
       );
	}
}
