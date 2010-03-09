package no.nav.maven.plugins;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.filtering.PropertyUtils;

/**
 * Goal which loads properties from a list of files and system properties into
 * project scope
 * 
 * The properties are interpolated with existing project properties, system
 * properties and other properties from the file. Only the format
 * ${propertyname} is recognized for cross-property references.
 * 
 * Existing properties are overwritten, and system properties overwrites all
 * other properties.
 * 
 * Missing files are ignored with an info message.
 * 
 * @goal load-properties
 * 
 * @author Øystein Gisnås, Accenture
 */
public class LoadPropertiesMojo extends AbstractMojo {

	/**
	 * The maven project
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * Property files to load properties from
	 * 
	 * @parameter
	 * @required
	 */
	private File[] files;

	public void execute() throws MojoExecutionException, MojoFailureException {
		for (File file : files) {
			if (file.exists()) {
				getLog().info("Loading property file: " + file);

				try {
					project.getProperties().putAll(System.getProperties());
					project.getProperties().putAll(PropertyUtils.loadPropertyFile(file, project.getProperties()));
					project.getProperties().putAll(System.getProperties());
				} catch (IOException e) {
					throw new MojoExecutionException("An error occured while loading properties from " + file, e);
				}
			} else {
				getLog().info("Ignoring missing properties file: " + file.getAbsolutePath());
			}
		}
	}
}
